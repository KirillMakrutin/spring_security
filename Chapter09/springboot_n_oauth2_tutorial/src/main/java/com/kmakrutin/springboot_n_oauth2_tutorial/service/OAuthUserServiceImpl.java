package com.kmakrutin.springboot_n_oauth2_tutorial.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kmakrutin.springboot_n_oauth2_tutorial.model.OAuthUser;
import com.kmakrutin.springboot_n_oauth2_tutorial.repository.RoleRepository;
import com.kmakrutin.springboot_n_oauth2_tutorial.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthUserServiceImpl implements OAuthUserService
{
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Override
  public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
  {
    return userRepository.findByUsername( username );
  }

  @Override
  public OAuthUser saveOrUpdateUser( OAuthUser user )
  {
    user.getRoles().add( roleRepository.findByAuthority( "ROLE_USER" ) );

    return userRepository.save( user );
  }

  @Override
  public OAuthUser saveOrUpdateAdmin( OAuthUser user )
  {
    user.getRoles().add( roleRepository.findByAuthority( "ROLE_USER" ) );
    user.getRoles().add( roleRepository.findByAuthority( "ROLE_ADMIN" ) );

    return userRepository.save( user );
  }
}
