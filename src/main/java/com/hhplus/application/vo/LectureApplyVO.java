package com.hhplus.application.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LectureApplyVO {

    private Long lectureScheduleUid;
    private Long userId;

    @Builder
    public LectureApplyVO(Long lectureScheduleUid, Long userId) {
        this.lectureScheduleUid = lectureScheduleUid;
        this.userId = userId;
    }
}
