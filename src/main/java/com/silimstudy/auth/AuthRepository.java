package com.silimstudy.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Repository
public interface AuthRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByUsername(String username);
}
