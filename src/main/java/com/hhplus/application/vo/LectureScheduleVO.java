package com.hhplus.application.vo;

import com.hhplus.domain.entity.LectureSchedule;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureScheduleVO {

    private Long uid;
    private LectureVO lecture;
    private LocalDate applyOpenDate;
    private LocalDate applyCloseDate;
    private Integer maxCapacity;
    private Integer currentCapacity;

    @Builder
    public LectureScheduleVO(Long uid, LectureVO lecture, LocalDate applyOpenDate,
                             LocalDate applyCloseDate, Integer maxCapacity, Integer currentCapacity) {
        this.uid = uid;
        this.lecture = lecture;
        this.applyOpenDate = applyOpenDate;
        this.applyCloseDate = applyCloseDate;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
    }

    public static LectureScheduleVO from(LectureSchedule lectureSchedule) {
        return LectureScheduleVO.builder()
            .uid(lectureSchedule.getUid())
            .lecture(LectureVO.from(lectureSchedule.getLecture()))
            .applyOpenDate(lectureSchedule.getApplyOpenDate())
            .applyCloseDate(lectureSchedule.getApplyCloseDate())
            .maxCapacity(lectureSchedule.getMaxCapacity())
            .currentCapacity(lectureSchedule.getCurrentCapacity())
            .build();
    }
}
