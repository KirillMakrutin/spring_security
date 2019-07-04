package com.kmakrutin.calendar.repositories;

import com.kmakrutin.calendar.domain.CalendarUserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarUserAuthorityRepository extends JpaRepository<CalendarUserAuthority, Integer> {
}
