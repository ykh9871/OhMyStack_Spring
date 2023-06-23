package com.ykh.backend.controller.recruit;

import com.ykh.backend.dto.recruit.RecruitDto;
import com.ykh.backend.service.recruit.RecruitServiceImpls;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitServiceImpls recruitServiceImpls;

    // 모든 채용공고 가져오기
    @GetMapping("/recruit")
    public List<RecruitDto> getAllRecruits() {
        return recruitServiceImpls.getAllRecruits();
    }
}
