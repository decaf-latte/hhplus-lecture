package com.hhplus.api.dto;

import com.hhplus.application.vo.LectureScheduleVO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AvailableLectureResponseDTO {

    private List<AvailableLectureListDTO> availableLectureList;

    @Builder
    public AvailableLectureResponseDTO(List<AvailableLectureListDTO> availableLectureList) {
        this.availableLectureList = availableLectureList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AvailableLectureListDTO {

        private String openDate;
        private List<LectureScheduleDTO> lectureScheduleList;

        @Builder
        public AvailableLectureListDTO(String openDate, List<LectureScheduleVO> lectureScheduleVOS) {
            this.openDate = openDate;
            this.lectureScheduleList = LectureScheduleDTO.from(lectureScheduleVOS);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LectureScheduleDTO {
        private Long uid;
        private String title;
        private String teacherName;
        private String applyOpenDate;
        private String applyCloseDate;
        private Integer maxCapacity;
        private Integer currentCapacity;

        @Builder
        public LectureScheduleDTO(Long uid, String title, String teacherName, String applyOpenDate,
                                  String applyCloseDate, Integer maxCapacity, Integer currentCapacity) {
            this.uid = uid;
            this.title = title;
            this.teacherName = teacherName;
            this.applyOpenDate = applyOpenDate;
            this.applyCloseDate = applyCloseDate;
            this.maxCapacity = maxCapacity;
            this.currentCapacity = currentCapacity;
        }

        public static List<LectureScheduleDTO> from(List<LectureScheduleVO> lectureScheduleVOS) {
            return lectureScheduleVOS.stream()
                    .map(lectureScheduleVO -> LectureScheduleDTO.builder()
                            .uid(lectureScheduleVO.getUid())
                            .title(lectureScheduleVO.getLecture().getTitle())
                            .teacherName(lectureScheduleVO.getLecture().getTeacherName())
                            .applyOpenDate(lectureScheduleVO.getApplyOpenDate().toString())
                            .applyCloseDate(lectureScheduleVO.getApplyCloseDate().toString())
                            .maxCapacity(lectureScheduleVO.getMaxCapacity())
                            .currentCapacity(lectureScheduleVO.getCurrentCapacity())
                            .build())
                    .toList();
        }
    }
}
