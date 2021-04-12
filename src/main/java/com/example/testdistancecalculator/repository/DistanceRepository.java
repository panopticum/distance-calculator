package com.example.testdistancecalculator.repository;

import com.example.testdistancecalculator.entity.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
}
