package com.ykh.backend.repository.user;

import com.ykh.backend.entity.user.AcademicAbility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicAbilityRepository extends JpaRepository<AcademicAbility, Long> {
}
