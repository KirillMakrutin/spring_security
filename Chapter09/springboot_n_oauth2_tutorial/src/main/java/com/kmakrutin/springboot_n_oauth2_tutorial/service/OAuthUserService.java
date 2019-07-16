package com.kmakrutin.springboot_n_oauth2_tutorial.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.kmakrutin.springboot_n_oauth2_tutorial.model.OAuthUser;

public interface OAuthUserService extends UserDetailsService
{
  OAuthUser saveOrUpdateUser( OAuthUser user );

  OAuthUser saveOrUpdateAdmin( OAuthUser user );
}
