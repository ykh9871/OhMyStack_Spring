package com.ykh.backend.service.stack;

import com.ykh.backend.entity.stack.TotalStack;
import com.ykh.backend.repository.stack.TotalStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStackService {

    private final TotalStackRepository totalStackRepository;

    public List<TotalStack> getUserStacks(List<Long> userStackIds) {
        return totalStackRepository.findAllById(userStackIds);
    }
}
