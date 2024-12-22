package com.example.inventory.controller;

import com.example.inventory.dto.request.InventoryAddRequest;
import com.example.inventory.dto.request.InventoryUpdateRequest;
import com.example.inventory.service.interfaces.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @GetMapping()
    public ResponseEntity getAllItems(){
        log.info("Entered getAllItems controller");
        return inventoryService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity getItemDetail(@PathVariable(name = "id") long itemId){
        return inventoryService.getItemDetail(itemId);
    }

    @PostMapping("")
    public ResponseEntity addItem(@RequestBody InventoryAddRequest addRequest){
        return inventoryService.addItem(addRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateItem(@PathVariable Long id, @RequestBody InventoryUpdateRequest updateRequest){
        return inventoryService.updateItem(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem(@PathVariable(name = "id") long itemId){
        return inventoryService.deleteItem(itemId);
    }

}
