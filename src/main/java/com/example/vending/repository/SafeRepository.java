package com.example.vending.repository;

import com.example.vending.entity.SafeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafeRepository extends JpaRepository<SafeEntity, String> {
}
