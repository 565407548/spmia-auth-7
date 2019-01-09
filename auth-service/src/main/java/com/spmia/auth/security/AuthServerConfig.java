package com.spmia.auth.security;

import com.spmia.auth.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-7 下午5:32
 */
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ServiceConfig serviceConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DemoUserDetailService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnection;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        /*设置签名*/
        accessTokenConverter.setSigningKey(serviceConfig.getJwtSigningKey());
//        accessTokenConverter.setSigningKey("smallsnail");
        return accessTokenConverter;
    }

    @Bean
    public ClientDetailsService myClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("eagleeye")
//                .secret("thisissecret")
//                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
//                .scopes("webclient", "mobileclient");
        clients.withClientDetails(myClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .tokenStore(new RedisTokenStore(redisConnection));
    }
}