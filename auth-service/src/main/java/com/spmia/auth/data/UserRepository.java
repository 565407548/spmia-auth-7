package com.spmia.auth.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 18-12-19 下午5:23
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
}
