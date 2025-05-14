package com.farmmonitoring.server.repository;

import com.farmmonitoring.server.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    List<Field> findByFarmId(Long farmId);
} 