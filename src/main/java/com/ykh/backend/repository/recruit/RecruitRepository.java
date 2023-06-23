package com.ykh.backend.repository.recruit;

import com.ykh.backend.entity.recruit.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}
