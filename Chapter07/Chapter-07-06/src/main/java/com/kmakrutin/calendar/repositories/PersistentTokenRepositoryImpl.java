package com.kmakrutin.calendar.repositories;

import com.kmakrutin.calendar.domain.PersistentToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface PersistentTokenRepositoryImpl extends JpaRepository<PersistentToken, String> {

    @Transactional
    void deleteByUsername(String username);
}
