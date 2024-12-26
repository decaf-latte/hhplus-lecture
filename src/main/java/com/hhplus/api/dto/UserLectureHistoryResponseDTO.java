package com.hhplus.api.dto;

import com.hhplus.application.vo.UserLectureHistoryVO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLectureHistoryResponseDTO {

    private Long userId;
    private Long lectureId;
    private String lectureTitle;
    private String lectureTeacherName;

    @Builder
    public UserLectureHistoryResponseDTO(Long userId, Long lectureId, String lectureTitle, String lectureTeacherName) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.lectureTitle = lectureTitle;
        this.lectureTeacherName = lectureTeacherName;
    }

    public static UserLectureHistoryResponseDTO from(UserLectureHistoryVO userLectureHistoryVO) {
        return UserLectureHistoryResponseDTO.builder()
                .userId(userLectureHistoryVO.getUser().getUserId())
                .lectureId(userLectureHistoryVO.getLectureSchedule().getLecture().getUid())
                .lectureTitle(userLectureHistoryVO.getLectureSchedule().getLecture().getTitle())
                .lectureTeacherName(userLectureHistoryVO.getLectureSchedule().getLecture().getTeacherName())
                .build();
    }
}
