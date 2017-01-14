package com.silimstudy.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yeojung on 17. 1. 14.
 */
@Repository
public interface UserAuthorityRepository extends JpaRepository<Authority, Long> {
}
