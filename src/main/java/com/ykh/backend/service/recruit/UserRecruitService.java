package com.ykh.backend.service.recruit;

import com.ykh.backend.dto.recruit.RecruitDto;
import com.ykh.backend.entity.recruit.Recruit;
import com.ykh.backend.entity.stack.TotalStack;
import com.ykh.backend.repository.recruit.RecruitRepository;
import com.ykh.backend.service.stack.UserStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRecruitService {

    private final RecruitRepository recruitRepository;
    private final UserStackService userStackService;

    public List<RecruitDto> getUserRecruits(List<Long> userRecruitIds) {
        List<Recruit> recruits = recruitRepository.findAllById(userRecruitIds);
        return recruits.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RecruitDto convertToDto(Recruit recruit) {
        RecruitDto recruitDto = new RecruitDto();
        recruitDto.setId(recruit.getId());
        recruitDto.setPosition(recruit.getPosition());
        recruitDto.setCompany(recruit.getCompany());
        recruitDto.setTitle(recruit.getTitle());

        List<TotalStack> stacks = userStackService.getUserStacks(recruit.getRecruitStacks());
        recruitDto.setRecruitStacks(stacks);

        recruitDto.setMainBusiness(recruit.getMainBusiness());
        recruitDto.setQualification(recruit.getQualification());
        recruitDto.setPreference(recruit.getPreference());
        recruitDto.setCareer(recruit.getCareer());
        recruitDto.setSite(recruit.getSite());
        recruitDto.setDate(recruit.getDate());
        return recruitDto;
    }
}
