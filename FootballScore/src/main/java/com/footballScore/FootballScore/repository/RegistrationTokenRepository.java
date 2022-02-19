package com.footballScore.FootballScore.repository;

import com.footballScore.FootballScore.entity.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken,Long> {
}
