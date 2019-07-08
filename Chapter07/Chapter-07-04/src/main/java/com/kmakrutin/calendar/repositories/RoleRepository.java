package com.kmakrutin.calendar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmakrutin.calendar.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>
{
  Role findByName( String name );
}
