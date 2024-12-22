package com.example.inventory.controller;

import com.example.inventory.dto.request.InventoryAddRequest;
import com.example.inventory.dto.request.InventoryUpdateRequest;
import com.example.inventory.dto.response.InventoryResponse;
import com.example.inventory.service.interfaces.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @GetMapping()
    public ResponseEntity<List<InventoryResponse>> getAllItems() throws ExecutionException,InterruptedException {
        log.info("Entered getAllItems controller");
        CompletableFuture<List<InventoryResponse>> listCompletableFuture = inventoryService.getAllItems();
        List<InventoryResponse> inventoryResponseList = listCompletableFuture.get();
        return ResponseEntity.ok(inventoryResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getItemDetail(@PathVariable(name = "id") long itemId)
            throws ExecutionException,InterruptedException {
        log.info("Entered getItemDetails controller");
        CompletableFuture<InventoryResponse> listCompletableFuture = inventoryService.getItemDetail(itemId);
        return ResponseEntity.ok(listCompletableFuture.get());
    }

    @PostMapping("")
    public ResponseEntity<String> addItem(@RequestBody InventoryAddRequest addRequest)
            throws ExecutionException,InterruptedException{
        log.info("Entered addItem controller");
        CompletableFuture<Boolean> futureFlag= inventoryService.addItem(addRequest);
        Boolean futureResult = futureFlag.get();
        if((futureResult != null) && futureResult){
            return ResponseEntity.accepted().body("Record added");
        }
        else return ResponseEntity.internalServerError().body("Some Error occurred");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id, @RequestBody InventoryUpdateRequest updateRequest)
            throws ExecutionException,InterruptedException{
        log.info("Entered updateItem controller");
        CompletableFuture<Boolean> futureFlag= inventoryService.updateItem(id, updateRequest);
        Boolean futureResult = futureFlag.get();
        if((futureResult != null) && futureResult){
            return ResponseEntity.accepted().body("Record updated");
        }
        return ResponseEntity.internalServerError().body("Item not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable(name = "id") long itemId) throws ExecutionException,InterruptedException{
        CompletableFuture<Boolean> futureFlag= inventoryService.deleteItem(itemId);
        Boolean futureResult = futureFlag.get();
        if((futureResult != null) && futureResult){
            return ResponseEntity.accepted().body("Record deleted");
        }
        return ResponseEntity.internalServerError().body("Item not found");
    }

}
