package com.project.login_system.user.domain;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.login_system.exceptions.DuplicateResourceException;
import com.project.login_system.exceptions.ResourceNotFoundException;
import com.project.login_system.user.dto.RegisterRequestDto;
import com.project.login_system.user.dto.UpdateEmailDto;
import com.project.login_system.user.dto.UpdatePasswordDto;
import com.project.login_system.user.dto.UpdateUsernameDto;
import com.project.login_system.user.dto.UserResponseDto;
import com.project.login_system.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Read

    public UserResponseDto getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", id));

        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto getByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %s", username));

        return modelMapper.map(user, UserResponseDto.class);
    }

    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    // Create

    public UserResponseDto create(RegisterRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername()))
            throw new DuplicateResourceException("%s already exists", requestDto.getUsername());

        if (userRepository.existsByEmail(requestDto.getEmail()))
            throw new DuplicateResourceException("%s already exists", requestDto.getEmail());

        var user = modelMapper.map(requestDto, User.class);

        return modelMapper.map(
                userRepository.save(user), UserResponseDto.class);
    }

    // Update

    public UserResponseDto updateUsername(Long id, UpdateUsernameDto usernameDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", id));

        var username = usernameDto.getUsername();

        if (!user.getUsername().equals(username) &&
                userRepository.existsByUsername(username))
            throw new DuplicateResourceException("%s already exists", username);

        user.setUsername(username);

        return modelMapper.map(
                userRepository.save(user), UserResponseDto.class);
    }

    public UserResponseDto updateEmail(Long id, UpdateEmailDto emailDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", id));

        var email = emailDto.getEmail();

        if (!user.getEmail().equals(email) &&
                userRepository.existsByEmail(email))
            throw new DuplicateResourceException("%s already exists", email);

        user.setEmail(email);

        return modelMapper.map(
                userRepository.save(user), UserResponseDto.class);
    }

    public UserResponseDto updatePassword(Long id, UpdatePasswordDto passwordDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", id));

        user.setPassword(passwordDto.getPassword());

        return modelMapper.map(
                userRepository.save(user), UserResponseDto.class);
    }

    public UserResponseDto update(Long id, RegisterRequestDto requestDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", id));

        if (!user.getEmail().equals(requestDto.getEmail()) &&
                userRepository.existsByEmail(requestDto.getEmail()))
            throw new DuplicateResourceException("%s already exists", requestDto.getEmail());

        if (!user.getUsername().equals(requestDto.getUsername()) &&
                userRepository.existsByUsername(requestDto.getUsername()))
            throw new DuplicateResourceException("%s already exist", requestDto.getUsername());

        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        return modelMapper.map(
                userRepository.save(user), UserResponseDto.class);
    }

    // Delete

    public void deleteById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", id));

        userRepository.delete(user);
    }

    public void deleteByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found whit %d", username));

        userRepository.delete(user);
    }
}