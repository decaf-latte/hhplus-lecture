package com.hhplus.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LectureApplyRequestDTO {

    private Long lectureScheduleUid;
    private Long userId;

    @Builder
    public LectureApplyRequestDTO(Long lectureScheduleUid, Long userId) {
        this.lectureScheduleUid = lectureScheduleUid;
        this.userId = userId;
    }
}
