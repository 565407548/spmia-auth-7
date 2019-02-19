package com.spmia.esservice.service;

import com.spmia.esservice.data.Goods;
import com.spmia.esservice.repo.ESRepo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 上午11:46
 */
@Service
public class GoodsService implements InitializingBean {

    @Autowired
    private ESRepo esRepo;

    private String index = "goods";

//    private String type = "doc";
    private String type = "";

    @Override
    public void afterPropertiesSet() throws Exception {
        esRepo.createIndexIfNotExist(index, type);
    }

    public void add(List<Goods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()) {
            return;
        }
        for (Goods goods : goodsList) {
            esRepo.addDocumentToBulkProcessor(index, type, goods);
        }
    }

    public void add(Goods goods) {
        esRepo.addDocument(index, type, goods);
    }

    public void delete(String id) {
        esRepo.deleteDocumentById(index, type, id);
    }

    public void update(Goods goods) {
        esRepo.updateDocument(index, type, String.valueOf(goods.getId()), goods);
    }

    public List<Goods> find(Goods goods) {
        return esRepo.queryDocumentByParam(
                index,
                type,
                goods,
                Goods.class);
    }
}
