package com.example.inventory.service.interfaces;

import com.example.inventory.dto.request.InventoryAddRequest;
import com.example.inventory.dto.request.InventoryUpdateRequest;
import com.example.inventory.dto.response.InventoryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InventoryService {
    ResponseEntity<List<InventoryResponse>> getAllItems();
    ResponseEntity<InventoryResponse> getItemDetail(Long id);
    ResponseEntity addItem(InventoryAddRequest addRequest);
    ResponseEntity updateItem(Long id, InventoryUpdateRequest updateRequest);
    ResponseEntity deleteItem(Long id);
}
