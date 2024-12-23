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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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
    @Async("threadPoolExecutor")
    public CompletableFuture<List<InventoryResponse>> getAllItems() throws InterruptedException{
        try{
            log.info("Fetching all inventory items");
//            Thread.sleep(1000);
            List<InventoryResponse> inventoryResponseList = inventoryCacheService.getAllItems();
            return CompletableFuture.completedFuture(inventoryResponseList);
        }
        catch (Exception e){
            log.error(e.toString());
            throw e;
        }

    }

    @Override
    @Async("threadPoolExecutor")
    public CompletableFuture<InventoryResponse> getItemDetail(Long id) {
        try{
            InventoryResponse inventoryResponse = inventoryCacheService.getItemDetail(id);
            if(inventoryResponse == null){
                return CompletableFuture.completedFuture(null);
            }
            return CompletableFuture.completedFuture(inventoryResponse);
        }
        catch(Exception e){
            log.error(e.toString());
            throw e;
        }

    }


    @Override
    @Async("threadPoolExecutor")
    public CompletableFuture<Boolean> addItem(InventoryAddRequest addRequest){
        try{
            boolean flag = inventoryCacheService.addItem(addRequest);
            if(!flag){
                log.error("Item not added");
                return CompletableFuture.completedFuture(Boolean.FALSE);
            }
            return CompletableFuture.completedFuture(Boolean.TRUE);
        }
        catch(Exception e){
            log.error(e.toString());
            throw e;
        }
    }

    @Override
    @Async("threadPoolExecutor")
    public CompletableFuture<Boolean> updateItem(Long id, InventoryUpdateRequest updateRequest){
        try{
            boolean flag = inventoryCacheService.updateItem(id,updateRequest);
            if(!flag){
                log.error("Item not found");
                return CompletableFuture.completedFuture(Boolean.FALSE);
            }
            return CompletableFuture.completedFuture(Boolean.TRUE);
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

    @Override
    @Async("threadPoolExecutor")
    public CompletableFuture<Boolean> deleteItem(Long id){
        boolean flag = inventoryCacheService.deleteItem(id);
        if(!flag){
            log.error("Item not found");
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        return CompletableFuture.completedFuture(Boolean.TRUE);
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
