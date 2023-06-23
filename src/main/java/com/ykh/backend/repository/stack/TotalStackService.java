package com.ykh.backend.repository.stack;

import com.ykh.backend.entity.stack.TotalStack;

import java.util.List;
import java.util.Optional;

public interface TotalStackService {
    List<TotalStack> findAll();
    Optional<TotalStack> findTotalStackById(Long id);
}
