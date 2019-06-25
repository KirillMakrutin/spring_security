package com.kmakrutin.chapter0100calendar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kmakrutin.chapter0100calendar.domain.CalendarUser;

public interface CalendarUserRepository extends JpaRepository<CalendarUser, Integer>
{
  CalendarUser findByEmail( String email );

  @Query( "select u from CalendarUser u where u.email like %?1%" )
  List<CalendarUser> findUsersByEmail( String partialEmail );
}
