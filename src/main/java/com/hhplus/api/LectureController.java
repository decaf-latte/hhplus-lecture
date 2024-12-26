package com.hhplus.api;

import com.hhplus.common.dto.ResponseDTO;
import com.hhplus.common.dto.ResponsePageDTO;
import com.hhplus.api.dto.AvailableLectureResponseDTO;
import com.hhplus.api.dto.LectureApplyRequestDTO;
import com.hhplus.api.dto.UserLectureHistoryResponseDTO;
import com.hhplus.application.LectureApplyService;
import com.hhplus.application.LectureScheduleService;
import com.hhplus.application.UserLectureHistoryService;
import com.hhplus.application.vo.LectureApplyVO;
import com.hhplus.application.vo.LectureScheduleVO;
import com.hhplus.application.vo.UserLectureHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LectureController {

  private final LectureScheduleService lectureScheduleService;
  private final UserLectureHistoryService userLectureHistoryService;
  private final LectureApplyService lectureApplyService;

  // 특강 신청 API
  @PostMapping("/lectures/apply")
  public ResponseDTO<Objects> applyLecture(@RequestBody LectureApplyRequestDTO requestDTO) {

    LectureApplyVO lectureApplyVO =
            LectureApplyVO.builder()
                    .lectureScheduleUid(requestDTO.getLectureScheduleUid())
                    .userId(requestDTO.getUserId())
                    .build();
    lectureApplyService.applyLecture(lectureApplyVO);

    return ResponseDTO.success(null);
  }

  // 날짜별 특강 신청 가능 목록 조회 API
  @GetMapping("/lectures/available")
  public ResponsePageDTO<AvailableLectureResponseDTO> getAvailableLectureList(
          @RequestParam("applyOpenDate") LocalDate applyOpenDate, Pageable pageable) {

    List<LectureScheduleVO> lectureScheduleVOList =
            lectureScheduleService.getAvailableLectureList(applyOpenDate, pageable);

    Map<LocalDate, List<LectureScheduleVO>> lectureScheduleVOMap =
            lectureScheduleVOList.stream()
                    .collect(Collectors.groupingBy(LectureScheduleVO::getApplyOpenDate));

    List<AvailableLectureResponseDTO.AvailableLectureListDTO> availableLectureList =
            lectureScheduleVOMap.entrySet().stream()
                    .map(
                            entry ->
                                    AvailableLectureResponseDTO.AvailableLectureListDTO.builder()
                                            .openDate(entry.getKey().toString())
                                            .lectureScheduleVOS(entry.getValue())
                                            .build())
                    .toList();

    return ResponsePageDTO.success(
            AvailableLectureResponseDTO.builder().availableLectureList(availableLectureList).build(),
            pageable.getPageNumber(),
            lectureScheduleVOList.size());
  }

  // 특강 신청 완료 목록 조회 API
  @GetMapping("/lectures/{userId}")
  public ResponsePageDTO<List<UserLectureHistoryResponseDTO>> getUserLectureHistory(
          @PathVariable Long userId, Pageable pageable) {

    List<UserLectureHistoryVO> userLectureHistoryVOList =
            userLectureHistoryService.getUserLectureHistory(userId, pageable);

    List<UserLectureHistoryResponseDTO> userLectureHistoryResponseDTOList =
            userLectureHistoryVOList.stream().map(UserLectureHistoryResponseDTO::from).toList();

    return ResponsePageDTO.success(
            userLectureHistoryResponseDTOList,
            pageable.getPageNumber(),
            userLectureHistoryVOList.size());
  }
}
