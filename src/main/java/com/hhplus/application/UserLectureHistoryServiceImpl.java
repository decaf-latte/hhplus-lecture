package com.hhplus.application;

import com.hhplus.domain.entity.UserLectureHistory;
import com.hhplus.repository.UserLectureHistoryRepository;
import com.hhplus.application.vo.UserLectureHistoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLectureHistoryServiceImpl implements UserLectureHistoryService {

    private final UserLectureHistoryRepository userLectureHistoryRepository;


    // 특강 신청 완료 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<UserLectureHistoryVO> getUserLectureHistory(Long userId, Pageable pageable) {

        // 사용자 강의 기록 조회
        List<UserLectureHistory> userLectureHistories = userLectureHistoryRepository.findByUserUserId(userId, pageable);

        // 결과 변환
        return userLectureHistories.stream().map(UserLectureHistoryVO::from).toList();
    }
}
