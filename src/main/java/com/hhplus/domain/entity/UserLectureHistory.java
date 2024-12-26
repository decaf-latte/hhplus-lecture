package com.hhplus.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity
@Table(name = "user_lecture_history")
@ToString
@NoArgsConstructor
public class UserLectureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", insertable = false, nullable = false)
    private Long uid;

    // lectureSchedule, user는 동시성 테스트를 위해서 unique, 복합키 설정하지 않음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_schedule_uid")
    private LectureSchedule lectureSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public UserLectureHistory(LectureSchedule lectureSchedule, User user) {
        this.lectureSchedule = lectureSchedule;
        this.user = user;
    }
}
