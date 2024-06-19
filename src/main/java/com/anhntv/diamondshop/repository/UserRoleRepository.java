package com.anhntv.diamondshop.repository;

import com.anhntv.diamondshop.security.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByAccountId(Long accountId);
}
