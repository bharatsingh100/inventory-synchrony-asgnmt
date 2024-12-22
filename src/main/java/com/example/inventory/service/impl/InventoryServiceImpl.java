package com.example.inventory.service.impl;

import com.example.inventory.dto.request.InventoryAddRequest;
import com.example.inventory.dto.request.InventoryUpdateRequest;
import com.example.inventory.dto.response.InventoryResponse;
import com.example.inventory.persistence.model.Inventory;
import com.example.inventory.persistence.repository.InventoryRepository;
import com.example.inventory.service.interfaces.InventoryService;
import jakarta.transaction.Transactional;
import jakarta.transaction.TransactionalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    InventoryCacheServiceImpl inventoryCacheService;

    @Override
    public ResponseEntity<List<InventoryResponse>> getAllItems(){
        try{
            log.info("Fetching all inventory items");
            List<InventoryResponse> inventoryResponseList = inventoryCacheService.getAllItems();
            return ResponseEntity.ok(inventoryResponseList);
        }
        catch (Exception e){
            log.error(e.toString());
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @Override
    public ResponseEntity<InventoryResponse> getItemDetail(Long id) {
        try{
            InventoryResponse inventoryResponse = inventoryCacheService.getItemDetail(id);
            if(inventoryResponse == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(inventoryResponse);
        }
        catch(Exception e){
            log.error(e.toString());
            return ResponseEntity.internalServerError().body(null);
        }

    }



    @Override
    public ResponseEntity addItem(InventoryAddRequest addRequest){
        try{
            boolean flag = inventoryCacheService.addItem(addRequest);
            if(!flag){
                log.error("Item not added");
                return ResponseEntity.internalServerError().body("Some error occurred");
            }
            return ResponseEntity.accepted().body("Record added");
        }
        catch(Exception e){
            log.error(e.toString());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @Override
    public ResponseEntity updateItem(Long id, InventoryUpdateRequest updateRequest){
        try{
            boolean flag = inventoryCacheService.updateItem(id,updateRequest);
            if(!flag){
                log.error("Item not found");
                return ResponseEntity.internalServerError().body("Item not found");
            }
            return ResponseEntity.accepted().body("Record updated");
        }
        catch (TransactionalException tx){
            log.error("Error occurred while updating on request : {}, transaction rolled back",updateRequest);
            throw tx;
        }
        catch(Exception e){
            log.error(e.toString());
            throw e;
        }
    }

    public ResponseEntity deleteItem(Long id){
        boolean flag = inventoryCacheService.deleteItem(id);
        if(!flag){
            log.error("Item not found");
            return ResponseEntity.internalServerError().body("Item not found");
        }
        return ResponseEntity.accepted().body("Record deleted");
    }



    private static InventoryResponse getInventoryResponse(Inventory inventory) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setId(inventory.getId());
        inventoryResponse.setName(inventory.getName());
        inventoryResponse.setQuantity(inventory.getQuantity());
        inventoryResponse.setCreatedBy(inventory.getCreatedBy());
        inventoryResponse.setUpdatedBy(inventory.getUpdatedBy());
        inventoryResponse.setCreatedDate(inventory.getCreatedDate());
        inventoryResponse.setUpdatedDate(inventory.getCreatedDate());
        return inventoryResponse;
    }
}
