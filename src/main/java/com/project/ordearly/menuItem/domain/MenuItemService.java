package com.project.ordearly.menuItem.domain;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordearly.exceptions.ResourceNotFoundException;
import com.project.ordearly.menuItem.dto.MenuItemRequestDto;
import com.project.ordearly.menuItem.dto.MenuItemResponseDto;
import com.project.ordearly.menuItem.infrastructure.MenuItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;

    public MenuItemResponseDto createProduct(MenuItemRequestDto requestDto) {
        var product = MenuItem.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .build();

        return modelMapper.map(menuItemRepository.save(product), MenuItemResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> listAvailable() {
        return menuItemRepository.findByAvailableTrue()
                .stream()
                .map(p -> modelMapper.map(p, MenuItemResponseDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponseDto> listAll() {
        return menuItemRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, MenuItemResponseDto.class))
                .toList();
    }

    public MenuItemResponseDto updateProduct(Long id, MenuItemRequestDto requestDto) {
        var product = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(requestDto.getName());
        product.setPrice(requestDto.getPrice());

        return modelMapper.map(menuItemRepository.save(product), MenuItemResponseDto.class);
    }

    public MenuItemResponseDto toggleAvailability(Long id) {
        var product = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setAvailable(!product.getAvailable());

        return modelMapper.map(menuItemRepository.save(product), MenuItemResponseDto.class);
    }
}