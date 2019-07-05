package com.kmakrutin.calendar.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kmakrutin.calendar.domain.Role;

public interface RoleRepository extends MongoRepository<Role, Integer>
{
  Role findByName( String name );
}
