package com.spmia.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServiceConfig {
  // config server
  @Value("${signing.key}")
  private String jwtSigningKey="";


  public String getJwtSigningKey() {
    return jwtSigningKey;
  }

}
