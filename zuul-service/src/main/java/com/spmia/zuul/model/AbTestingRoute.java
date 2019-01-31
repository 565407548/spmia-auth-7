package com.spmia.zuul.model;

import lombok.Data;

@Data
public class AbTestingRoute {
    String serviceName;
    Integer active;
    String endpoint;
    Integer weight;
}