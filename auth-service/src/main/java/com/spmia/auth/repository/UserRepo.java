package com.spmia.auth.repository;

import com.spmia.auth.model.DemoUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 18-12-19 下午5:23
 */
@Repository
public interface UserRepo extends CrudRepository<DemoUser,String> {
}
