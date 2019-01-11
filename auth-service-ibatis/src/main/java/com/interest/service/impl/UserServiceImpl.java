package com.interest.service.impl;

import com.interest.dao.RoleDao;
import com.interest.dao.UserDao;
import com.interest.model.UserEntity;
import com.interest.model.UserRoleEntity;
import com.interest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final int ROLE_ADMIN = 1;
    private static final int ROLE_USER = 2;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public void insert(UserEntity userEntity) {
        userDao.insert(userEntity);
    }

    @Override
    public void del(UserEntity userEntity) {
        userDao.del(userEntity);
    }

    @Override
    public UserEntity getUserEntityByLoginName(String loginName) {
        return userDao.getUserEntityByLoginName(loginName);
    }

    @Override
    public List<UserEntity> usersList(String name, int pageSize, int start) {
        return userDao.usersList(name, pageSize, start);
    }

    @Override
    public Integer usersSize(String name, int pageSize, int start) {
        return userDao.usersSize(name, pageSize, start);
    }

    @Override
    @Transactional
    public void insertUser(UserEntity userEntity) {
        userEntity.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(userEntity.getPassword()));
        userDao.insertUser(userEntity);

        UserRoleEntity userRoleEntity = new UserRoleEntity(userEntity.getId(), ROLE_USER);
        roleDao.insertUserRole(userRoleEntity);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        //userEntity.setPassword(new Md5PasswordEncoder().encodePassword(userEntity.getPassword(), null));
        if (userEntity.getId() != 8888) {
            userEntity.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(userEntity.getPassword()));
        }
        userDao.updateUser(userEntity);

        UserRoleEntity userRoleEntity = new UserRoleEntity(userEntity.getId(), ROLE_USER);
        roleDao.updateUserrole(userRoleEntity);
    }

    @Override
    public void deleteUsers(List<String> groupId) {
        userDao.deleteUsers(groupId);
    }

    @Override
    public UserEntity getEntityById(int userid) {
        return userDao.getUserEntityById(userid);
    }

}
