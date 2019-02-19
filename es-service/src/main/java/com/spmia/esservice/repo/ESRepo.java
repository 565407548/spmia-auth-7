package com.spmia.esservice.repo;

import com.spmia.esservice.JSONUtils;
import com.spmia.esservice.data.IQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 下午12:33
 */

@Slf4j
@Component
public class ESRepo {

    @Autowired
    private Client client;

    private BulkProcessor bulkProcessor;

    @PostConstruct
    public void initBulkProcessor() {

        bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {

            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                log.info("序号：{} 开始执行{} 条记录保存", executionId, request.numberOfActions());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                log.error(String.format("序号：%s 执行失败; 总记录数：%s", executionId, request.numberOfActions()), failure);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                log.info("序号：{} 执行{}条记录保存成功,耗时：{}毫秒,", executionId, request.numberOfActions(), response.getIngestTookInMillis());
            }
        }).setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                .setConcurrentRequests(4)
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(500), 3))
                .build();
    }


    @PreDestroy
    public void closeBulk() {
        if (bulkProcessor != null) {
            try {
                bulkProcessor.close();
            } catch (Exception e) {
                log.error("close bulkProcessor exception", e);
            }
        }
    }


    /**
     * 批量添加,性能最好
     */
    public void addDocumentToBulkProcessor(String indices, String type, Object object) {
        bulkProcessor.add(
                client.prepareIndex(indices, type)
                        .setSource(JSONUtils.beanToJson(object))
                        .request());
    }


    public void addDocument(String indices, String type, Object object) {
        IndexResponse resp = client.prepareIndex(indices, type)
                .setSource(JSONUtils.beanToJson(object))
                .get();
        log.info("添加结果：{}", resp.toString());
    }



    /**
     * 按id删除
     */
    public void deleteDocumentById(String index, String type, String id) {
        // new DeleteByQueryRequest(search);
        DeleteResponse resp = client.prepareDelete(index, type, id)
                .get();

        log.info("删除结果：{}", resp.toString());
    }

    /**
     * 按条件删除
     */
    public void deleteDocumentByQuery(String index, String type, IQueryParam param) {

        //DeleteByQueryRequestBuilder builder = new DeleteByQueryRequestBuilder(client,DeleteByQueryAction.INSTANCE);
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(client);

        //builder.filter(convertParam(param));
        builder.source()
                .setIndices(index)
                .setTypes(type)
                .setQuery(param.toQuery());
        BulkByScrollResponse resp = builder.get();
        log.info("删除结果：{}", resp.toString());
    }

    /**
     * 按ID更新
     */
    public void updateDocument(String indices, String type, String id, Object object) {
        UpdateResponse resp = client.prepareUpdate(indices, type, id)
                .setDoc(JSONUtils.beanToJson(object))
                .get();
        log.info("更新结果：{}", resp.toString());
    }


    /**
     * 按条件更新
     */
    public void updateDocumentByQuery(String indices, String type, Object object, IQueryParam param) {
        //UpdateByQueryRequestBuilder builder = new UpdateByQueryRequestBuilder(client,UpdateByQueryAction.INSTANCE);
        UpdateByQueryRequestBuilder builder = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        builder.source().setIndices(indices).setTypes(type).setQuery(param.toQuery());
    }


    public <T> List<T> queryDocumentByParam(String indices, String type, IQueryParam param, Class<T> clazz) {
        SearchRequestBuilder builder = buildRequest(indices, type);
        builder.addSort("birthday", SortOrder.ASC);
        builder.setQuery(param.toQuery());
        builder.setFrom(0).setSize(10);
        SearchResponse resp = builder.get();
        return convertResponse(resp, clazz);
    }


    /**
     * 通用的装换返回结果
     */
    public <T> List<T> convertResponse(SearchResponse response, Class<T> clazz) {
        List<T> list = new ArrayList<>(0);
        if (response != null && response.getHits() != null) {
            String result;
            T item = null;
            Field idField = ReflectionUtils.findField(clazz, "id");
            if (idField != null) {
                ReflectionUtils.makeAccessible(idField);
            }
            for (SearchHit hit : response.getHits()) {
                result = hit.getSourceAsString();
                if (StringUtils.hasText(result)) {
                    item = JSONUtils.parse(result, clazz);
                }
                if (item != null) {
                    if (idField != null) {
                        ReflectionUtils.setField(idField, item, hit.getId());
                    }
                    list.add(item);
                }
            }
        }
        return list;
    }

    public SearchRequestBuilder buildRequest(String indices, String type) {
        return client.prepareSearch(indices).setTypes(type);
    }

    /**
     * 不存在就创建索引
     */
    public boolean createIndexIfNotExist(String index, String type) {
        IndicesAdminClient adminClient = client.admin().indices();
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if (!response.isExists()) {
            return createIndex(index, type);
        }
        return true;
    }

    /**
     * 创建索引
     */
    public boolean createIndex(String index, String type) {
        XContentBuilder mappingBuilder;
        try {
            mappingBuilder = this.getMapping(type);
        } catch (Exception e) {
            log.error(String.format("创建Mapping 异常；index:%s type:%s,", index, type), e);
            return false;
        }
        Settings settings = Settings.builder()
                .put("index.number_of_shards", 2)
                .put("index.number_of_replicas", 1)
                .put("index.refresh_interval", "5s").build();
        IndicesAdminClient adminClient = client.admin().indices();
        CreateIndexRequestBuilder builder = adminClient.prepareCreate(index);
        builder.setSettings(settings);
        CreateIndexResponse response = builder.addMapping(type, mappingBuilder).get();
        log.info("创建索引：{} 类型:{} 是否成功：{}", index, type, response.isAcknowledged());
        return response.isAcknowledged();
    }

    /***
     * 创建索引的Mapping信息  注意声明的roles为nested类型
     */
    private XContentBuilder getMapping(String type) throws Exception {
        XContentBuilder mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(type)
                .startObject("_all").field("enabled", false).endObject()
                .startObject("properties")
                .startObject("userName").field("type", "keyword").endObject()
                .startObject("age").field("type", "integer").endObject()
                .startObject("birthday").field("type", "date").endObject()
                .startObject("description").field("type", "text").field("analyzer", "ik_smart").endObject()
                .startObject("roles").field("type", "nested")
                .startObject("properties")
                .startObject("createTime").field("type", "date").endObject()
                .startObject("name").field("type", "keyword").endObject()
                .startObject("description").field("type", "text").field("analyzer", "ik_smart").endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        return mappingBuilder;
    }
}
