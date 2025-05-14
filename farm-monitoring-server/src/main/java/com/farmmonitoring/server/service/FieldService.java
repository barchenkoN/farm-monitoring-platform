package com.farmmonitoring.server.service;

import com.farmmonitoring.server.dto.FieldDto;
import com.farmmonitoring.server.model.Field;
import com.farmmonitoring.server.model.Farm;
import com.farmmonitoring.server.repository.FieldRepository;
import com.farmmonitoring.server.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldService {
    
    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;
      /**
     * Отримує список всіх полів
     */
    public List<FieldDto> getAllFields() {
        return fieldRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Конвертує сутність Field в DTO
     */
    private FieldDto convertToDto(Field field) {
        Farm farm = field.getFarm();
        String farmName = farm != null ? farm.getName() : "";
        
        return FieldDto.builder()
                .id(field.getId())
                .name(field.getName())
                .farmId(farm != null ? farm.getId() : null)
                .farmName(farmName)
                .area(field.getSizeHectares() != null ? field.getSizeHectares().doubleValue() : 0.0)
                .soilType("") // This field is not in the Field entity, so we set a default
                .currentCrop(field.getCropType())
                .status("ACTIVE") // Default status
                .coordinates("") // This field is not in the Field entity, so we set a default
                .sensorsCount(field.getSensors() != null ? field.getSensors().size() : 0)
                .build();
    }
      /**
     * Отримує поле за ідентифікатором
     */
    public Optional<FieldDto> getFieldById(Long id) {
        return fieldRepository.findById(id)
                .map(this::convertToDto);
    }
      /**
     * Отримує поля за ідентифікатором ферми
     */
    public List<FieldDto> getFieldsByFarmId(Long farmId) {
        return fieldRepository.findByFarmId(farmId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
      /**
     * Отримує кількість всіх полів
     */
    public int getFieldsCount() {
        return (int) fieldRepository.count();
    }
}
