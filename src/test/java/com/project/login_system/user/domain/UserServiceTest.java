package com.project.login_system.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.project.login_system.exceptions.DuplicateResourceException;
import com.project.login_system.exceptions.ResourceNotFoundException;
import com.project.login_system.user.dto.RegisterRequestDto;
import com.project.login_system.user.dto.UserResponseDto;
import com.project.login_system.user.infrastructure.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserResponseDto testUserResponseDto;
    private RegisterRequestDto testRegisterRequestDto;

    private Long userId = 1L;
    private String userFirstName = "Jhon";
    private String userLastName = "Doe";
    private String userUsername = "test";
    private String userEmail = "example@test.com";
    private String userPassword = "123456789";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername(userUsername);
        testUser.setEmail(userEmail);
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        testUserResponseDto = new UserResponseDto();
        testUserResponseDto.setId(userId);
        testUserResponseDto.setUsername(userUsername);
        testUserResponseDto.setEmail(userEmail);

        testRegisterRequestDto = new RegisterRequestDto();
        testRegisterRequestDto.setFirstName(userFirstName);
        testRegisterRequestDto.setLastName(userLastName);
        testRegisterRequestDto.setUsername(userUsername);
        testRegisterRequestDto.setEmail(userEmail);
        testRegisterRequestDto.setPassword(userPassword);
    }

    // Read

    @Test
    void shouldReturnUserResponseDtoWhenIdExists() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(testUser));

        when(modelMapper.map(testUser, UserResponseDto.class))
                .thenReturn(testUserResponseDto);

        var result = userService.getById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);

        verify(userRepository).findById(userId);
        verify(modelMapper).map(testUser, UserResponseDto.class);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found whit %d", 99L);

        verify(userRepository).findById(99L);
    }

    @Test
    void shouldReturnUserResponseDtoWhenUsernameExists() {
        when(userRepository.findByUsername(userUsername))
                .thenReturn(Optional.of(testUser));

        when(modelMapper.map(testUser, UserResponseDto.class))
                .thenReturn(testUserResponseDto);

        var result = userService.getByUsername(userUsername);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(userUsername);

        verify(userRepository).findByUsername(userUsername);
        verify(modelMapper).map(testUser, UserResponseDto.class);
    }

    @Test
    void shouldThrowExceptionWhenUsernameDoesNotExist() {
        when(userRepository.findByUsername("Username"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByUsername("Username"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource not found whit Username");

        verify(userRepository).findByUsername("Username");
    }

    @Test
    void shouldReturnPageOfUsers() {
        var page = new PageImpl<>(List.of(testUser));

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(modelMapper.map(testUser, UserResponseDto.class))
                .thenReturn(testUserResponseDto);

        var result = userService.getAll(PageRequest.of(0, 10));

        assertThat(result).isNotEmpty();
    }

    // Create

    @Test
    void shouldCreateAndReturnUserResponseDtoWhenRequestIsValid() {
        when(userRepository.existsByUsername(userUsername))
                .thenReturn(false);

        when(userRepository.existsByEmail(userEmail))
                .thenReturn(false);

        when(modelMapper.map(testRegisterRequestDto, User.class))
                .thenReturn(testUser);

        when(userRepository.save(testUser))
                .thenReturn(testUser);

        when(modelMapper.map(testUser, UserResponseDto.class))
                .thenReturn(testUserResponseDto);

        var result = userService.create(testRegisterRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo(userUsername);
        assertThat(result.getEmail()).isEqualTo(userEmail);

        verify(userRepository).existsByUsername(userUsername);
        verify(userRepository).existsByEmail(userEmail);
        verify(userRepository).save(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername(userUsername))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.create(testRegisterRequestDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("%s already exists", userUsername);

        verify(userRepository).existsByUsername(userUsername);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(userEmail))
                .thenReturn(true);

        assertThatThrownBy(() -> userService.create(testRegisterRequestDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("%s already exists", userEmail);

        verify(userRepository).existsByEmail(userEmail);
    }
}