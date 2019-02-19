package com.spmia.esservice;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 下午6:13
 */
public interface ESCallback<T> {
    void onSuccess(T t);

    void onFail();
}
