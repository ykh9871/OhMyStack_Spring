package com.ykh.backend.entity.recruit;

import com.ykh.backend.converter.LongArrayConverter;
import com.ykh.backend.entity.recruit.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recruit")
public class Recruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="position_id", nullable=false)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(columnDefinition = "text")
    private String title;

    @Column(name = "recruit_stacks", columnDefinition = "text")
    @Convert(converter = LongArrayConverter.class)
    private List<Long> recruitStacks;

    @Column(columnDefinition = "text")
    private String mainBusiness;

    @Column(columnDefinition = "text")
    private String qualification;

    @Column(name = "preferences",columnDefinition = "text")
    private String preference;

    @ManyToOne
    @JoinColumn(name="career_id", nullable=false)
    private Career career;

    private String site;
    private String date;
}
