package com.hhplus.domain.repository;

import com.hhplus.domain.entity.Lecture;
import com.hhplus.repository.LectureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ActiveProfiles("test")
@SpringBootTest
class LectureRepositoryTest {

    @Mock
    private LectureRepository lectureRepository;

    @Test
    @DisplayName("특강 저장 테스트")
    void savePersistsLecture() {
        Lecture lecture = Lecture.builder()
                .title("Test Lecture")
                .teacherName("Test Teacher")
                .build();
        Mockito.when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        Lecture savedLecture = lectureRepository.save(lecture);

        assertThat(savedLecture.getTitle()).isEqualTo("Test Lecture");
    }

    @Test
    @DisplayName("특강 조회 테스트")
    void findByIdReturnsLectureWhenExists() {
        Lecture lecture = Lecture.builder()
                .title("Test Lecture")
                .teacherName("Test Teacher")
                .build();
        Mockito.when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        Optional<Lecture> foundLecture = lectureRepository.findById(1L);

        assertThat(foundLecture).isPresent();
        assertThat(foundLecture.get().getTitle()).isEqualTo("Test Lecture");
    }

    @Test
    @DisplayName("특강 조회 실패 테스트")
    void findByIdReturnsEmptyWhenLectureDoesNotExist() {
        Mockito.when(lectureRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Lecture> foundLecture = lectureRepository.findById(999L);

        assertThat(foundLecture).isNotPresent();
    }

    @Test
    @DisplayName("특강 삭제 테스트")
    void deleteRemovesLecture() {
        Lecture lecture = Lecture.builder()
                .title("Test Lecture")
                .teacherName("Test Teacher")
                .build();
        Mockito.doNothing().when(lectureRepository).delete(any(Lecture.class));
        Mockito.when(lectureRepository.findById(anyLong())).thenReturn(Optional.empty());

        lectureRepository.delete(lecture);
        Optional<Lecture> foundLecture = lectureRepository.findById(lecture.getUid());

        assertThat(foundLecture).isNotPresent();
    }
}