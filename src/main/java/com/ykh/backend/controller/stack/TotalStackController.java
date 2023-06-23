package com.ykh.backend.controller.stack;

import com.ykh.backend.entity.stack.TotalStack;
import com.ykh.backend.repository.stack.TotalStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class TotalStackController {

    private final TotalStackService totalStackService;

    // 모든 기술 스택 조회
    @GetMapping("/total_stack")
    public ResponseEntity<List<TotalStack>> findAll() {
        return ResponseEntity.ok(totalStackService.findAll());
    }

    // 특정 기술 스택 조회
    @GetMapping("/total_stack/{id}")
    public ResponseEntity<TotalStack> findTotalStackById(@PathVariable Long id){
        Optional<TotalStack> totalStack = totalStackService.findTotalStackById(id);
        return totalStack.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
