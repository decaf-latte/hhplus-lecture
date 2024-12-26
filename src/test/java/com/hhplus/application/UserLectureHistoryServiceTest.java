package com.hhplus.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.hhplus.application.vo.UserLectureHistoryVO;
import com.hhplus.domain.entity.Lecture;
import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.domain.entity.User;
import com.hhplus.domain.entity.UserLectureHistory;
import com.hhplus.repository.UserLectureHistoryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserLectureHistoryServiceTest {

    @InjectMocks
    private UserLectureHistoryServiceImpl userLectureHistoryService;

    @Mock
    private UserLectureHistoryRepository userLectureHistoryRepository;

    @Test
    @DisplayName("특강 신청 완료 목록 조회 성공")
    void userLectureHistoryIsReturnedSuccessfully() {
        // Given
        User user = User.builder().name("Test User").build();

        Lecture lecture1 = Lecture.builder().title("Lecture 1").teacherName("Teacher 1").build();
        Lecture lecture2 = Lecture.builder().title("Lecture 2").teacherName("Teacher 2").build();

        LectureSchedule lectureSchedule1 = LectureSchedule.builder().lecture(lecture1).maxCapacity(10).currentCapacity(5).build();
        LectureSchedule lectureSchedule2 = LectureSchedule.builder().lecture(lecture2).maxCapacity(15).currentCapacity(10).build();

        UserLectureHistory history1 = UserLectureHistory.builder().user(user).lectureSchedule(lectureSchedule1).build();
        UserLectureHistory history2 = UserLectureHistory.builder().user(user).lectureSchedule(lectureSchedule2).build();

        List<UserLectureHistory> histories = List.of(history1, history2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<UserLectureHistory> historyPage = new PageImpl<>(histories);

        when(userLectureHistoryRepository.findByUserUserId(user.getUserId(), pageable)).thenReturn(historyPage.getContent());

        // When
        List<UserLectureHistoryVO> result = userLectureHistoryService.getUserLectureHistory(user.getUserId(), pageable);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLectureSchedule().getLecture().getTitle()).isEqualTo("Lecture 1");
        assertThat(result.get(1).getLectureSchedule().getLecture().getTitle()).isEqualTo("Lecture 2");
    }
}
