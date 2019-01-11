package com.interest.model;

import lombok.Data;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-11 上午11:05
 */
@Data
public class UserRoleEntity {
    private Integer userId;
    private Integer roleId;

    public UserRoleEntity(Integer userId,Integer roleId){
        this.userId=userId;
        this.roleId=roleId;
    }
}
