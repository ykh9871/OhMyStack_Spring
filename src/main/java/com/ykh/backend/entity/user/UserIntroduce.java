package com.ykh.backend.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_introduce")
public class UserIntroduce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Include
    @JoinColumn(name="user_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "represent")
    private boolean represent;

    @Column(name = "invisible")
    private boolean invisible;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime deletedAt;
}