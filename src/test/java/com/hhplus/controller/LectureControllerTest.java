package com.hhplus.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.api.dto.AvailableLectureResponseDTO;
import com.hhplus.api.dto.LectureApplyRequestDTO;
import com.hhplus.api.dto.UserLectureHistoryResponseDTO;
import com.hhplus.application.LectureApplyService;
import com.hhplus.application.vo.LectureApplyVO;
import com.hhplus.common.dto.ResponsePageDTO;
import com.hhplus.domain.entity.Lecture;
import com.hhplus.domain.entity.LectureSchedule;
import com.hhplus.domain.entity.User;
import com.hhplus.domain.entity.UserLectureHistory;
import com.hhplus.repository.LectureRepository;
import com.hhplus.repository.LectureScheduleRepository;
import com.hhplus.repository.UserLectureHistoryRepository;
import com.hhplus.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    private UserLectureHistoryRepository userLectureHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureApplyService lectureApplyService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("수강 신청 가능한 특강 조회 테스트")
    void getAvailableLectureList() throws Exception {

        Lecture lecture = addLecture("Test", "Teacher");

        addLectureSchedule(LectureSchedule.builder()
                .lecture(lecture)
                .applyOpenDate(LocalDate.of(2026, 1, 1))
                .applyCloseDate(LocalDate.of(2026, 12, 31))
                .currentCapacity(0)
                .maxCapacity(30)
                .build());

        // 날짜가 해당 되지 않아 제외
        addLectureSchedule(LectureSchedule.builder()
                .lecture(lecture)
                .applyOpenDate(LocalDate.of(2025, 1, 1))
                .applyCloseDate(LocalDate.of(2025, 12, 31))
                .currentCapacity(0)
                .maxCapacity(30)
                .build());

        String url = "/lectures/available";

        MvcResult result = mockMvc.perform(get(url)
                        .param("applyOpenDate", "2026-03-03")
                        .param("size", "100")
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andReturn();

        ResponsePageDTO<AvailableLectureResponseDTO> responsePageDTO
                = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ResponsePageDTO<AvailableLectureResponseDTO>>() {
        });
        assertEquals(1, responsePageDTO.getData().getAvailableLectureList().get(0).getLectureScheduleList().size());
    }

    @Test
    @DisplayName("회원 수강 신청 목록 조회 테스트")
    void getUserLectureHistory() throws Exception {

        User user = addUser("Test");
        Lecture lecture = addLecture("Test", "Teacher");

        LectureSchedule lectureSchedule = addLectureSchedule(LectureSchedule.builder()
                .lecture(lecture)
                .applyOpenDate(LocalDate.of(2027, 1, 1))
                .applyCloseDate(LocalDate.of(2027, 12, 31))
                .currentCapacity(0)
                .maxCapacity(30)
                .build());

        LectureApplyVO lectureApplyVO =
                LectureApplyVO.builder()
                        .lectureScheduleUid(lectureSchedule.getUid())
                        .userId(user.getUserId())
                        .build();
        lectureApplyService.applyLecture(lectureApplyVO);

        String url = "/lectures/" + user.getUserId();

        MvcResult result = mockMvc.perform(get(url)
                        .param("size", "100")
                        .param("page", "0")
                )
                .andExpect(status().isOk())
                .andReturn();

        ResponsePageDTO<List<UserLectureHistoryResponseDTO>> responsePageDTO
                = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<ResponsePageDTO<List<UserLectureHistoryResponseDTO>>>() {
        });
        assertTrue(responsePageDTO.getData().size() == 1);
    }

    @Test
    @DisplayName("회원 수강 신청 테스트 1")
    void applyLecture_case1() throws Exception {

        User user = addUser("Test");
        Lecture lecture = addLecture("Test", "Teacher");

        LectureSchedule lectureSchedule = addLectureSchedule(LectureSchedule.builder()
                .lecture(lecture)
                .applyOpenDate(LocalDate.of(2023, 1, 1))
                .applyCloseDate(LocalDate.of(2023, 12, 31))
                .currentCapacity(0)
                .maxCapacity(30)
                .build());

        LectureApplyRequestDTO requestDTO = LectureApplyRequestDTO.builder()
                .lectureScheduleUid(lectureSchedule.getUid())
                .userId(user.getUserId())
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDTO);
        String url = "/lectures/apply";
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<UserLectureHistory> userLectureHistoryList
                = userLectureHistoryRepository.findByUserUserId(user.getUserId(), PageRequest.of(0, 100));

        assertEquals(1, userLectureHistoryList.size());
    }

    @Test
    @DisplayName("회원 수강 신청 테스트 2 - 40명 특강 신청, 30명만 성공")
    void applyLecture_case2() {

        List<User> users = new ArrayList<>();

        // User 40명 추가
        for (int i = 1; i < 40; i++) {
            User user = addUser("Test" + i);
            users.add(user);
        }

        Lecture lecture = addLecture("Test", "Teacher");

        LectureSchedule lectureSchedule = addLectureSchedule(LectureSchedule.builder()
                .lecture(lecture)
                .applyOpenDate(LocalDate.of(2024, 1, 1))
                .applyCloseDate(LocalDate.of(2024, 12, 31))
                .currentCapacity(0)
                .maxCapacity(30)
                .build());

        // 비동기 thread pool 생성
        ExecutorService executor = Executors.newFixedThreadPool(40);

        // 비동기 요청 시작
        List<CompletableFuture<Void>> futures = users.stream()
                .map(user -> CompletableFuture.runAsync(() -> {
                    try {
                        applyLecture(user, lectureSchedule);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 작업 완료 후 ExecutorService를 종료.
        executor.shutdown();

        List<UserLectureHistory> userLectureHistoryList
                = userLectureHistoryRepository.findByLectureScheduleUid(lectureSchedule.getUid());

        assertEquals(30, userLectureHistoryList.size());
    }

    @Test
    @DisplayName("회원 수강 신청 테스트 3 - 동일한 유저 정보로 같은 특강을 5번 신청했을 때, 1번만 성공")
    void applyLecture_case3() {

        User user = addUser("Test");
        Lecture lecture = addLecture("Test", "Teacher");

        LectureSchedule lectureSchedule = addLectureSchedule(LectureSchedule.builder()
                .lecture(lecture)
                .applyOpenDate(LocalDate.of(2024, 1, 1))
                .applyCloseDate(LocalDate.of(2024, 12, 31))
                .currentCapacity(0)
                .maxCapacity(30)
                .build());

        // 비동기 thread pool 생성
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 비동기 요청 시작
        List<CompletableFuture<Void>> futures = IntStream.range(0, 5)
                .mapToObj(index -> CompletableFuture.runAsync(() -> {
                    try {
                        applyLecture(user, lectureSchedule);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 작업 완료 후 ExecutorService를 종료.
        executor.shutdown();

        List<UserLectureHistory> userLectureHistoryList
                = userLectureHistoryRepository.findByLectureScheduleUidAndUserUserId(lectureSchedule.getUid(), user.getUserId());

        assertEquals(1, userLectureHistoryList.size());
    }

    private void applyLecture(User user, LectureSchedule lectureSchedule) throws Exception {

        LectureApplyRequestDTO requestDTO = LectureApplyRequestDTO.builder()
                .lectureScheduleUid(lectureSchedule.getUid())
                .userId(user.getUserId())
                .build();

        String requestBody = objectMapper.writeValueAsString(requestDTO);
        String url = "/lectures/apply";
        mockMvc.perform(post(url)
                .contentType("application/json")
                .content(requestBody)
        );
    }

    private User addUser(String name) {
        User user = User.builder()
                .name(name)
                .build();
        return userRepository.save(user);
    }

    private Lecture addLecture(String title, String teacherName) {
        Lecture lecture = Lecture.builder()
                .title(title)
                .teacherName(teacherName)
                .build();
        return lectureRepository.save(lecture);
    }

    private LectureSchedule addLectureSchedule(LectureSchedule lectureSchedule) {
        return lectureScheduleRepository.save(lectureSchedule);
    }
}