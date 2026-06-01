package com.skbhati.microservices.inventory.entity;

import jakarta.persistence.*;

@Table(name = "t_inventory")
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skuCode;
    private int quantity;
}
