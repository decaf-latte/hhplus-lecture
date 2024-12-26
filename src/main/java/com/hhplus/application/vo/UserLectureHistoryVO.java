package com.hhplus.application.vo;

import com.hhplus.domain.entity.UserLectureHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLectureHistoryVO {

    private Long uid;
    private LectureScheduleVO lectureSchedule;
    private UserVO user;

    @Builder
    public UserLectureHistoryVO(Long uid, LectureScheduleVO lectureSchedule, UserVO user) {
        this.uid = uid;
        this.lectureSchedule = lectureSchedule;
        this.user = user;
    }

    public static UserLectureHistoryVO from(UserLectureHistory userLectureHistory) {
        return UserLectureHistoryVO.builder()
            .uid(userLectureHistory.getUid())
            .lectureSchedule(LectureScheduleVO.from(userLectureHistory.getLectureSchedule()))
            .user(UserVO.from(userLectureHistory.getUser()))
            .build();
    }
}
