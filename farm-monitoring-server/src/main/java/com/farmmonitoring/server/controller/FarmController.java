package com.farmmonitoring.server.controller;

import com.farmmonitoring.server.dto.FarmDto;
import com.farmmonitoring.server.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
@RequiredArgsConstructor
public class FarmController {
    private final FarmService farmService;

    @GetMapping
    public ResponseEntity<List<FarmDto>> getAllFarms() {
        return ResponseEntity.ok(farmService.getAllFarms());
    }
}
