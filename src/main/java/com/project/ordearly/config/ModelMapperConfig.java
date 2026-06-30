package com.project.ordearly.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.ordearly.order.domain.Order;
import com.project.ordearly.order.dto.OrderResponseDto;

@Configuration
public class ModelMapperConfig {
    @Bean
    ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        
        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Order.class, OrderResponseDto.class)
                .addMappings(mapper -> {
                    mapper.map(Order::getItems, OrderResponseDto::setItems);
                    mapper.map(Order::getPayments, OrderResponseDto::setPayments);
                });

        return modelMapper;
    }
}