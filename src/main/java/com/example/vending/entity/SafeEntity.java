package com.example.vending.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "safe")
public class SafeEntity {
    @Id
    @Column(unique = true, nullable = false, length = 50)
    private String id;
    private Double totalMoney;
}
