package com.example.inventory.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryUpdateRequest {

    private String name;

    private String quantity;

}
