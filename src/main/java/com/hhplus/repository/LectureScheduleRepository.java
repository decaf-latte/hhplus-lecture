package com.hhplus.repository;

import com.hhplus.domain.entity.LectureSchedule;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {

    // applyOpenDate와 applyCloseDate 사이에 현재 시간이 있고, currentCapacity < maxCapacity인 LectureSchedule을 조회한다.
    @Query("SELECT ls FROM LectureSchedule ls " +
            "WHERE ls.currentCapacity < ls.maxCapacity " +
            "AND :applyOpenDate BETWEEN ls.applyOpenDate AND ls.applyCloseDate " +
            "ORDER BY ls.applyOpenDate ASC")
    Page<LectureSchedule> findAvailableLectures(@Param("applyOpenDate") LocalDate applyOpenDate, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureSchedule> findByUid(Long uid);
}
