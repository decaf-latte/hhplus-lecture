package com.hhplus.application;

import com.hhplus.application.vo.LectureScheduleVO;
import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.repository.LectureScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureScheduleServiceImpl implements LectureScheduleService {

  private final LectureScheduleRepository lectureScheduleRepository;

  @Override
  @Transactional(readOnly = true)
  public List<LectureScheduleVO> getAvailableLectureList(LocalDate applyOpenDate, Pageable pageable) {

    Page<LectureSchedule> lectureScheduleList = lectureScheduleRepository.findAvailableLectures(applyOpenDate, pageable);

    return lectureScheduleList.stream()
            .map(LectureScheduleVO::from)
            .toList();
  }
}