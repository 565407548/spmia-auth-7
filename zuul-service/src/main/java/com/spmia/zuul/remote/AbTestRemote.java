package com.spmia.zuul.remote;

import com.spmia.zuul.model.AbTestingRoute;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description: 获取abtesting数据。
 * 网络请求出现错误，比如服务无响应、数据解析出错，会由 AbTestHystrix 处理
 * @author: Zheng Jim
 * @date: 19-1-31 上午11:33
 */
@FeignClient(value = "specialroutes-service", configuration = FeignConfig.class, fallback = AbTestHystrix.class)
public interface AbTestRemote {
    @GetMapping(value = "/v1/route/abtesting/{serviceName}")
    AbTestingRoute getAbTestRoute(@PathVariable("serviceName") String serviceName);
}
