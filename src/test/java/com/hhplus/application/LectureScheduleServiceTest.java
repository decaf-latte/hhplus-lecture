package com.hhplus.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hhplus.application.vo.LectureScheduleVO;
import com.hhplus.domain.entity.Lecture;
import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.repository.LectureScheduleRepository;
import java.time.LocalDate;
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
class LectureScheduleServiceTest {

    @InjectMocks
    private LectureScheduleServiceImpl lectureScheduleService;

    @Mock
    private LectureScheduleRepository lectureScheduleRepository;

    @Test
    @DisplayName("사용 가능한 강의 목록을 성공적으로 반환")
    void availableLecturesAreReturnedSuccessfully() {
        // Arrange
        LocalDate applyOpenDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);

        Lecture lecture1 = Lecture.builder().title("Lecture 1").teacherName("Teacher 1").build();
        Lecture lecture2 = Lecture.builder().title("Lecture 2").teacherName("Teacher 2").build();

        List<LectureSchedule> lectureSchedules = List.of(
                LectureSchedule.builder().lecture(lecture1).maxCapacity(10).currentCapacity(5).build(),
                LectureSchedule.builder().lecture(lecture2).maxCapacity(15).currentCapacity(10).build()
        );
        Page<LectureSchedule> lectureSchedulePage = new PageImpl<>(lectureSchedules);

        when(lectureScheduleRepository.findAvailableLectures(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(lectureSchedulePage);

        // Act
        List<LectureScheduleVO> result = lectureScheduleService.getAvailableLectureList(applyOpenDate, pageable);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLecture().getTitle()).isEqualTo("Lecture 1");
        assertThat(result.get(1).getLecture().getTitle()).isEqualTo("Lecture 2");
    }

    @Test
    @DisplayName("사용 가능한 강의가 없을 때 빈 목록 반환")
    void noAvailableLecturesAreReturnedWhenNoneExist() {
        // Arrange
        LocalDate applyOpenDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);
        Page<LectureSchedule> lectureSchedulePage = new PageImpl<>(List.of());

        when(lectureScheduleRepository.findAvailableLectures(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(lectureSchedulePage);

        // Act
        List<LectureScheduleVO> result = lectureScheduleService.getAvailableLectureList(applyOpenDate, pageable);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("페이징된 사용 가능한 강의 목록 반환")
    void availableLecturesAreReturnedWithPagination() {
        // Arrange
        LocalDate applyOpenDate = LocalDate.now();
        Pageable pageable = PageRequest.of(1, 1);

        Lecture lecture = Lecture.builder().title("Lecture 3").teacherName("Teacher 3").build();

        List<LectureSchedule> lectureSchedules = List.of(
                LectureSchedule.builder().lecture(lecture).maxCapacity(15).currentCapacity(8).build()
        );
        Page<LectureSchedule> lectureSchedulePage = new PageImpl<>(lectureSchedules);

        when(lectureScheduleRepository.findAvailableLectures(any(LocalDate.class), any(Pageable.class)))
                .thenReturn(lectureSchedulePage);

        // Act
        List<LectureScheduleVO> result = lectureScheduleService.getAvailableLectureList(applyOpenDate, pageable);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLecture().getTitle()).isEqualTo("Lecture 3");
    }
}
