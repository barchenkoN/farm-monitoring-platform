package com.farmmonitoring.server.controller;

import com.farmmonitoring.server.dto.FieldDto;
import com.farmmonitoring.server.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @GetMapping
    public ResponseEntity<List<FieldDto>> getAllFields() {
        return ResponseEntity.ok(fieldService.getAllFields());
    }
}
