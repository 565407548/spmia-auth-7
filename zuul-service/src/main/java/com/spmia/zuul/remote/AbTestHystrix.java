package com.spmia.zuul.remote;

import com.spmia.zuul.model.AbTestingRoute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-31 上午11:47
 */
@Slf4j
@Component
public class AbTestHystrix implements AbTestRemote {
    @Override
    public AbTestingRoute getAbTestRoute(String serviceName) {
        log.info("AbTestHystrix getAbTestRoute");
        return null;
    }
}
