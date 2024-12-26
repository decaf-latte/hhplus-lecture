package com.hhplus.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.hhplus.domain.entity.User;
import com.hhplus.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 조회 테스트")
    void findByIdReturnsUserWhenUserExists() {
        User user = User.builder()
                .name("Test User")
                .build();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findById(1L);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("사용자 조회 실패 테스트")
    void findByIdReturnsEmptyWhenUserDoesNotExist() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findById(999L);

        assertThat(foundUser).isNotPresent();
    }

    @Test
    @DisplayName("사용자 저장 테스트")
    void savePersistsUser() {
        User user = User.builder()
                .name("Test User")
                .build();
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteRemovesUser() {
        User user = User.builder()
                .name("Test User")
                .build();
        Mockito.doNothing().when(userRepository).delete(any(User.class));
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findById(user.getUserId());

        assertThat(foundUser).isNotPresent();
    }
}