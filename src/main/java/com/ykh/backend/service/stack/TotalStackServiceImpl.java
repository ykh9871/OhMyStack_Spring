package com.ykh.backend.service.stack;


import com.ykh.backend.entity.stack.TotalStack;
import com.ykh.backend.repository.stack.TotalStackRepository;
import com.ykh.backend.repository.stack.TotalStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TotalStackServiceImpl implements TotalStackService {

    private final TotalStackRepository totalStackRepository;

    @Override
    public List<TotalStack> findAll() {
        return totalStackRepository.findAll();
    }

    @Override
    public Optional<TotalStack> findTotalStackById(Long id) {
        return totalStackRepository.findById(id);
    }
}
