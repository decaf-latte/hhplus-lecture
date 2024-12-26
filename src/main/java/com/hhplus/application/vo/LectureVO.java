package com.hhplus.application.vo;

import com.hhplus.domain.entity.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureVO {

  private Long uid;
  private String title;
  private String teacherName;

  @Builder
  public LectureVO(Long uid, String title, String teacherName) {
    this.uid = uid;
    this.title = title;
    this.teacherName = teacherName;
  }

  public static LectureVO from(Lecture lecture) {
    return LectureVO.builder()
        .uid(lecture.getUid())
        .title(lecture.getTitle())
        .teacherName(lecture.getTeacherName())
        .build();
  }
}
