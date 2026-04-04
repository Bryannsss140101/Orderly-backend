package com.project.login_system.user.domain;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.login_system.exceptions.DuplicateResourceException;
import com.project.login_system.exceptions.ResourceNotFoundException;
import com.project.login_system.security.auth.utils.SecurityUtils;
import com.project.login_system.user.dto.UserResponseDto;
import com.project.login_system.user.dto.UserUpdateDto;
import com.project.login_system.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Read
    public UserResponseDto getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with %d", id));

        return modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto getByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with %s", username));

        return modelMapper.map(user, UserResponseDto.class);
    }

    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    public UserResponseDto getCurrentUser() {
        var user = securityUtils.getCurrentUser();
        return modelMapper.map(user, UserResponseDto.class);
    }

    // Update
    public UserResponseDto update(Long id, UserUpdateDto updateDto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with %d", id));

        if (!user.getEmail().equals(updateDto.getEmail()) &&
                userRepository.existsByEmail(updateDto.getEmail()))
            throw new DuplicateResourceException("%s already exists", updateDto.getEmail());

        if (!user.getUsername().equals(updateDto.getUsername()) &&
                userRepository.existsByUsername(updateDto.getUsername()))
            throw new DuplicateResourceException("%s already exist", updateDto.getUsername());

        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setUsername(updateDto.getUsername());
        user.setEmail(updateDto.getEmail());

        return modelMapper.map(
                userRepository.save(user), UserResponseDto.class);
    }

    // Delete
    public void deleteById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with %d", id));

        userRepository.delete(user);
    }
}