package com.spmia.auth.data;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 18-12-20 上午10:05
 */
public class DemoAuthority implements GrantedAuthority {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static DemoAuthority AUTHORITY_USER;
    private static DemoAuthority AUTHORITY_ADMIN;
    private String authority;

    private DemoAuthority(String authority) {
        if (ROLE_ADMIN.equals(authority)
                || ROLE_USER.equals(authority)) {
            this.authority = authority;
        } else {
            this.authority = ROLE_USER;
        }
    }

    public static DemoAuthority obtain(String authority) {
        if (ROLE_ADMIN.equals(authority)) {
            if (AUTHORITY_ADMIN == null) {
                AUTHORITY_ADMIN = new DemoAuthority(authority);
            }
            return AUTHORITY_ADMIN;
        } else {
            if (AUTHORITY_USER == null) {
                AUTHORITY_USER = new DemoAuthority(authority);
            }
            return AUTHORITY_USER;
        }
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(obtain(ROLE_USER));
        list.add(obtain(ROLE_ADMIN));
        return list;
    }
}
