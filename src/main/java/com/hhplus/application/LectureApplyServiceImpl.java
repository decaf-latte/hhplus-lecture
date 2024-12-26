package com.hhplus.application;

import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.domain.entity.User;
import com.hhplus.domain.entity.UserLectureHistory;
import com.hhplus.repository.LectureScheduleRepository;
import com.hhplus.repository.UserLectureHistoryRepository;
import com.hhplus.repository.UserRepository;
import com.hhplus.application.vo.LectureApplyVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureApplyServiceImpl implements LectureApplyService {

  private final UserRepository userRepository;
  private final LectureScheduleRepository lectureScheduleRepository;
  private final UserLectureHistoryRepository userLectureHistoryRepository;

  @Override
  @Transactional
  public void applyLecture(LectureApplyVO lectureApplyVO) {

    Long userId = lectureApplyVO.getUserId();
    User user = getUserByUserId(userId);


    Long lectureScheduleUid = lectureApplyVO.getLectureScheduleUid();
    LectureSchedule lectureSchedule = getLectureScheduleByUid(lectureScheduleUid);

    // 수강 신청 저장
    UserLectureHistory userLectureHistory =
            UserLectureHistory.builder().lectureSchedule(lectureSchedule).user(user).build();

    userLectureHistory = userLectureHistoryRepository.save(userLectureHistory);
    log.info("save userLectureHistory: {}", userLectureHistory);

    updateLectureSchedule(lectureSchedule);
  }

  private User getUserByUserId(Long userId) {
    return userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found."));
  }

  private LectureSchedule getLectureScheduleByUid(Long lectureScheduleUid) {
    return lectureScheduleRepository
            .findByUid(lectureScheduleUid)
            .orElseThrow(() -> new EntityNotFoundException("Lecture not found."));
  }

  private void updateLectureSchedule(LectureSchedule lectureSchedule) {
    lectureSchedule.plusCurrentCapacity();
    lectureScheduleRepository.save(lectureSchedule);
  }
}