package com.hhplus.application;

import com.hhplus.application.vo.UserLectureHistoryVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserLectureHistoryService {

    List<UserLectureHistoryVO> getUserLectureHistory(Long userId, Pageable pageable);
}

