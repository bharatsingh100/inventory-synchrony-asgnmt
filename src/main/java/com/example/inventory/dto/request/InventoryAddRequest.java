package com.example.inventory.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class InventoryAddRequest {

    private String name;

    private String quantity;

}
