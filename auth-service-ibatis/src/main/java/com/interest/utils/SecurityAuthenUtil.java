package com.interest.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityAuthenUtil {

	/**
	 * 直接获取当前用户的登录账号
	 * @return
	 */
	public static int getId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		return Integer.valueOf(user.getUsername());
	}

	public static int getIdWithoutException() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User user = (User)authentication.getPrincipal();
			return Integer.valueOf(user.getUsername());
		}catch (Exception ex){
			return 0;
		}
	}

	/**
	 * 直接获取当前用户的登录账号
	 * @return
	 */
	public static String getLoginName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		return user.getUsername();
	}
	
	/**
	 * 直接获取当前用户的认证用户信息
	 * @return
	 */
	public static User getAuthenticationUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		return user;
	}
	

}
