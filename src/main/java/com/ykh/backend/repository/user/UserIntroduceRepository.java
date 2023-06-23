package com.ykh.backend.repository.user;

import com.ykh.backend.entity.user.UserIntroduce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserIntroduceRepository extends JpaRepository<UserIntroduce, Long> {
    List<UserIntroduce> findByUser_Id(Long userId);

    List<UserIntroduce> findByUser_IdAndInvisibleFalse(Long userId);

}
