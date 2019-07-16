package com.kmakrutin.springboot_n_oauth2_tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmakrutin.springboot_n_oauth2_tutorial.model.OAuthUser;

public interface UserRepository extends JpaRepository<OAuthUser, Integer>
{
  OAuthUser findByUsername( String username );
}
