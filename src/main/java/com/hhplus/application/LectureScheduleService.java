package com.hhplus.application;

import com.hhplus.application.vo.LectureScheduleVO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LectureScheduleService {

    List<LectureScheduleVO> getAvailableLectureList(LocalDate applyOpenDate, Pageable pageable);
}
