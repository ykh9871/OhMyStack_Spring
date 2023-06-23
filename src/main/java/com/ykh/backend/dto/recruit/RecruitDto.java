package com.ykh.backend.dto.recruit;

import com.ykh.backend.entity.recruit.Position;
import com.ykh.backend.entity.recruit.company.Company;
import com.ykh.backend.entity.recruit.Career;
import com.ykh.backend.entity.stack.TotalStack;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDto {
    private Long id;
    private Position position;
    private Company company;
    private String title;
    private List<TotalStack> recruitStacks;
    private String mainBusiness;
    private String qualification;
    private String preference;
    private Career career;
    private String site;
    private String date;
}
