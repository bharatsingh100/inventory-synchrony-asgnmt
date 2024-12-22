package com.example.inventory.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InventoryResponse implements Serializable {
    private Long Id;

    private String name;

    private String quantity;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;
}
