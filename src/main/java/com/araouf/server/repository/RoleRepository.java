package com.araouf.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.araouf.server.domain.db.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
