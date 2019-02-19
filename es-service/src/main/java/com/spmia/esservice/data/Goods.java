package com.spmia.esservice.data;

import lombok.Data;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.StringUtils;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 下午12:52
 */
@Data
public class Goods implements IQueryParam{
    private long id;
    private String title;
    private String name;
    private String comment;
    private String introduce_special;
    private String preview_image;

    @Override
    public BoolQueryBuilder toQuery() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(name)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("name", name));
        }
        if (StringUtils.hasText(title)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", title));
        }
        if (StringUtils.hasText(name)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", name));
        }
        if (StringUtils.hasText(comment)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("comment", comment));
        }

        return boolQueryBuilder;
    }
}
