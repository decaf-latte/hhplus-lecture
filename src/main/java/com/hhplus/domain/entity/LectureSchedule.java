package com.hhplus.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "lecture_schedule")
@ToString
@NoArgsConstructor
public class LectureSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", insertable = false, nullable = false)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_uid")
    private Lecture lecture;

    @Column(name = "apply_open_date")
    private LocalDate applyOpenDate;

    @Column(name = "apply_close_date")
    private LocalDate applyCloseDate;

    @Column(name = "current_capacity")
    private Integer currentCapacity;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public LectureSchedule(Lecture lecture, LocalDate applyOpenDate,LocalDate applyCloseDate, Integer maxCapacity, Integer currentCapacity) {
        this.lecture = lecture;
        this.applyOpenDate = applyOpenDate;
        this.applyCloseDate = applyCloseDate;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    public void plusCurrentCapacity() {
        this.currentCapacity = this.currentCapacity + 1;
    }
}
