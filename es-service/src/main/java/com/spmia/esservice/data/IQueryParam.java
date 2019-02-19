package com.spmia.esservice.data;

import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 下午4:19
 */
public interface IQueryParam {
    BoolQueryBuilder toQuery();
}
