package com.spmia.organization.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 18-12-29 下午2:49
 */
@Component
//@ConfigurationProperties(prefix = "spmia.organization")
@Data
@Validated
public class Constant {
    @Min(value = 5,message = "must be between 5 and 25")
    @Max(value = 25,message = "must be between 5 and 25")
    private int pageSize;

    @Value("${spmia.organization.pageSize1:8}")
    private int pageSize1;
}
//end:validated[]
