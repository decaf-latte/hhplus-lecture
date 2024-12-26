package com.hhplus.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.hhplus.application.vo.LectureApplyVO;
import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.domain.entity.User;
import com.hhplus.domain.entity.UserLectureHistory;
import com.hhplus.repository.LectureScheduleRepository;
import com.hhplus.repository.UserLectureHistoryRepository;
import com.hhplus.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LectureApplyServiceTest {

    @InjectMocks
    private LectureApplyServiceImpl lectureApplyService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LectureScheduleRepository lectureScheduleRepository;

    @Mock
    private UserLectureHistoryRepository userLectureHistoryRepository;

    @Test
    @DisplayName("특강 신청 성공")
    void applyLectureSuccessfully() {
        // Arrange
        LectureApplyVO lectureApplyVO = new LectureApplyVO(1L, 1L);
        User user = User.builder().name("Test User").build();
        LectureSchedule lectureSchedule = LectureSchedule.builder()
                .currentCapacity(5)
                .maxCapacity(10)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureScheduleRepository.findByUid(anyLong())).thenReturn(Optional.of(lectureSchedule));

        // Act
        lectureApplyService.applyLecture(lectureApplyVO);

        // Assert
        verify(userLectureHistoryRepository, times(1)).save(any(UserLectureHistory.class));
        verify(lectureScheduleRepository, times(1)).save(any(LectureSchedule.class));
    }

    @Test
    @DisplayName("특강 신청 실패 - 사용자를 찾을 수 없음")
    void applyLectureFailsWhenUserNotFound() {
        // Arrange
        LectureApplyVO lectureApplyVO = new LectureApplyVO(1L, 1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> lectureApplyService.applyLecture(lectureApplyVO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found.");
    }

    @Test
    @DisplayName("특강 신청 실패 - 강의를 찾을 수 없음")
    void applyLectureFailsWhenLectureNotFound() {
        // Arrange
        LectureApplyVO lectureApplyVO = new LectureApplyVO(1L, 1L);
        User user = User.builder().name("Test User").build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureScheduleRepository.findByUid(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> lectureApplyService.applyLecture(lectureApplyVO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Lecture not found.");
    }
}
