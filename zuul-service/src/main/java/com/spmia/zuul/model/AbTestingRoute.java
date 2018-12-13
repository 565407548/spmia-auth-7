package com.spmia.zuul.model;

public class AbTestingRoute {
    String serviceName;
    // N or Y
    String active;
    // ???
    String endpoint;
    // 权重，用于标记多大概率请求导入到testing中
    Integer weight;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }


}