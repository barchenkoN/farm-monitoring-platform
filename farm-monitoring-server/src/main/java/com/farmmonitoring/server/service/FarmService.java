package com.farmmonitoring.server.service;

import com.farmmonitoring.server.dto.FarmDto;
import com.farmmonitoring.server.model.Farm;
import com.farmmonitoring.server.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// (Видалено непотрібні імпорти)
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmService {
    
    private final FarmRepository farmRepository;
      /**
     * Отримує список всіх ферм
     */
    public List<FarmDto> getAllFarms() {
        return farmRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Конвертує сутність Farm в DTO
     */
    private FarmDto convertToDto(Farm farm) {
        return FarmDto.builder()
                .id(farm.getId())
                .name(farm.getName())
                .location(farm.getLocation())
                .ownerName("") // This field is not in the Farm entity, so we set a default
                .contactInfo("") // This field is not in the Farm entity, so we set a default
                .totalArea(farm.getSizeHectares() != null ? farm.getSizeHectares().doubleValue() : 0.0)
                .description("") // This field is not in the Farm entity, so we set a default
                .status("ACTIVE") // Default status
                .build();
    }
      /**
     * Отримує ферму за ідентифікатором
     */
    public Optional<FarmDto> getFarmById(Long id) {
        return farmRepository.findById(id)
                .map(this::convertToDto);
    }
      /**
     * Отримує кількість всіх ферм
     */
    public int getFarmsCount() {
        return (int) farmRepository.count();
    }
}
