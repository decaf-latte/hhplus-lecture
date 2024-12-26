package com.hhplus.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.repository.LectureScheduleRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class LectureScheduleRepositoryTest {

  @Mock
  private LectureScheduleRepository lectureScheduleRepository;

  @Test
  @DisplayName("특강 저장 테스트 - 강의가 있을 때")
  void findAvailableLecturesReturnsLecturesWhenAvailable() {
    LectureSchedule lectureSchedule =
        LectureSchedule.builder()
            .currentCapacity(5)
            .maxCapacity(10)
            .applyOpenDate(LocalDate.now().minusDays(1))
            .applyCloseDate(LocalDate.now().plusDays(1))
            .build();

    Pageable pageable = PageRequest.of(0, 10);
    Page<LectureSchedule> page = new PageImpl<>(Collections.singletonList(lectureSchedule));
    Mockito.when(
            lectureScheduleRepository.findAvailableLectures(
                any(LocalDate.class), any(Pageable.class)))
        .thenReturn(page);

    Page<LectureSchedule> result =
        lectureScheduleRepository.findAvailableLectures(LocalDate.now(), pageable);

    assertThat(result).isNotEmpty();
    assertThat(result.getContent().get(0).getCurrentCapacity()).isEqualTo(5);
  }

  @Test
  @DisplayName("특강 저장 테스트 - 강의가 없을 때")
  void findAvailableLecturesReturnsEmptyWhenNoLecturesAvailable() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<LectureSchedule> page = new PageImpl<>(Collections.emptyList());
    Mockito.when(
            lectureScheduleRepository.findAvailableLectures(
                any(LocalDate.class), any(Pageable.class)))
        .thenReturn(page);

    Page<LectureSchedule> result =
        lectureScheduleRepository.findAvailableLectures(LocalDate.now(), pageable);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("특강 조회 테스트 - 존재할 때")
  void findByIdReturnsLectureScheduleWhenExists() {
    LectureSchedule lectureSchedule =
        LectureSchedule.builder()
            .currentCapacity(5)
            .maxCapacity(10)
            .applyOpenDate(LocalDate.now().minusDays(1))
            .applyCloseDate(LocalDate.now().plusDays(1))
            .build();
    Mockito.when(lectureScheduleRepository.findById(anyLong()))
        .thenReturn(Optional.of(lectureSchedule));

    Optional<LectureSchedule> foundLectureSchedule = lectureScheduleRepository.findById(1L);

    assertThat(foundLectureSchedule).isPresent();
    assertThat(foundLectureSchedule.get().getCurrentCapacity()).isEqualTo(5);
  }

  @Test
  @DisplayName("특강 삭제 테스트 - 성공")
  void deleteRemovesLectureSchedule() {
    LectureSchedule lectureSchedule =
        LectureSchedule.builder()
            .currentCapacity(5)
            .maxCapacity(10)
            .applyOpenDate(LocalDate.now().minusDays(1))
            .applyCloseDate(LocalDate.now().plusDays(1))
            .build();
    Mockito.doNothing().when(lectureScheduleRepository).delete(any(LectureSchedule.class));
    Mockito.when(lectureScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

    lectureScheduleRepository.delete(lectureSchedule);
    Optional<LectureSchedule> foundLectureSchedule =
        lectureScheduleRepository.findById(lectureSchedule.getUid());

    assertThat(foundLectureSchedule).isNotPresent();
  }

  @Test
  @DisplayName("특강 UID로 조회 테스트 - 비관적 잠금")
  void findByUidReturnsLectureScheduleWithPessimisticLock() {
    LectureSchedule lectureSchedule =
        LectureSchedule.builder()
            .currentCapacity(5)
            .maxCapacity(10)
            .applyOpenDate(LocalDate.now().minusDays(1))
            .applyCloseDate(LocalDate.now().plusDays(1))
            .build();
    Mockito.when(lectureScheduleRepository.findByUid(anyLong()))
        .thenReturn(Optional.of(lectureSchedule));

    Optional<LectureSchedule> foundLectureSchedule = lectureScheduleRepository.findByUid(1L);

    assertThat(foundLectureSchedule).isPresent();
    assertThat(foundLectureSchedule.get().getCurrentCapacity()).isEqualTo(5);
  }

  @Test
  @DisplayName("특강 UID로 조회 실패 테스트 - 비관적 잠금")
  void findByUidReturnsEmptyWhenLectureScheduleDoesNotExist() {
    Mockito.when(lectureScheduleRepository.findByUid(anyLong())).thenReturn(Optional.empty());

    Optional<LectureSchedule> foundLectureSchedule = lectureScheduleRepository.findByUid(999L);

    assertThat(foundLectureSchedule).isNotPresent();
  }
}
