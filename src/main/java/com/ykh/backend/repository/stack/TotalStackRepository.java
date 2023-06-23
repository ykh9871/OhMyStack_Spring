package com.ykh.backend.repository.stack;

import com.ykh.backend.entity.stack.TotalStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TotalStackRepository extends JpaRepository<TotalStack, Long> {

}
