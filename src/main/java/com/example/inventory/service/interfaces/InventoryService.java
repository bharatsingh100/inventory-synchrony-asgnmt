package com.example.inventory.service.interfaces;

import com.example.inventory.dto.request.InventoryAddRequest;
import com.example.inventory.dto.request.InventoryUpdateRequest;
import com.example.inventory.dto.response.InventoryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface InventoryService {
    CompletableFuture<List<InventoryResponse>> getAllItems() throws InterruptedException ;
    CompletableFuture<InventoryResponse> getItemDetail(Long id);
    CompletableFuture<Boolean> addItem(InventoryAddRequest addRequest);
    CompletableFuture<Boolean> updateItem(Long id, InventoryUpdateRequest updateRequest);
    CompletableFuture<Boolean> deleteItem(Long id);
}
