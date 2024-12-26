package com.hhplus.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.hhplus.domain.entity.Lecture;
import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.domain.entity.User;
import com.hhplus.domain.entity.UserLectureHistory;
import com.hhplus.repository.UserLectureHistoryRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserLectureHistoryRepositoryTest {

  @Mock private UserLectureHistoryRepository userLectureHistoryRepository;

  @Test
  @DisplayName("특강 이력 조회 테스트")
  void findByUserUserIdReturnsLectureHistoriesWhenUserHasHistories() {
    UserLectureHistory history =
        UserLectureHistory.builder()
            .user(User.builder().name("Test User").build())
            .lectureSchedule(
                LectureSchedule.builder()
                    .lecture(
                        Lecture.builder().title("Test Lecture").teacherName("Test Teacher").build())
                    .build())
            .build();

    Pageable pageable = PageRequest.of(0, 10);
    Mockito.when(userLectureHistoryRepository.findByUserUserId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.singletonList(history));

    List<UserLectureHistory> histories =
        userLectureHistoryRepository.findByUserUserId(1L, pageable);

    assertThat(histories).isNotEmpty();
    assertThat(histories.get(0).getUser().getName()).isEqualTo("Test User");
  }

  @Test
  @DisplayName("특강 이력 조회 실패 테스트")
  void findByUserUserIdReturnsEmptyWhenUserHasNoHistories() {
    Pageable pageable = PageRequest.of(0, 10);
    Mockito.when(userLectureHistoryRepository.findByUserUserId(anyLong(), any(Pageable.class)))
        .thenReturn(Collections.emptyList());

    List<UserLectureHistory> histories =
        userLectureHistoryRepository.findByUserUserId(999L, pageable);

    assertThat(histories).isEmpty();
  }
}
