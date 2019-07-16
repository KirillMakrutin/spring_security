package com.kmakrutin.springboot_n_oauth2_tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmakrutin.springboot_n_oauth2_tutorial.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>
{
  Role findByAuthority( String authority );
}
