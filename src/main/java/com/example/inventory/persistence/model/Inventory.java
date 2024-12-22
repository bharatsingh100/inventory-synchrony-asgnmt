package com.example.inventory.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Inventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String name;

    private String quantity;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;
}
