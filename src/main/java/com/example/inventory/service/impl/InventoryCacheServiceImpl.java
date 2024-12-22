package com.example.inventory.service.impl;

import com.example.inventory.dto.request.InventoryAddRequest;
import com.example.inventory.dto.request.InventoryUpdateRequest;
import com.example.inventory.dto.response.InventoryResponse;
import com.example.inventory.persistence.model.Inventory;
import com.example.inventory.persistence.repository.InventoryRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryCacheServiceImpl {

    @Autowired
    InventoryRepository inventoryRepository;

    @Cacheable(value = "inventory")
    public List<InventoryResponse> getAllItems() {
        try {
            log.info("Fetching all inventory items from cache service");
            List<Inventory> inventoryList = inventoryRepository.findAll();
            return inventoryList
                    .stream()
                    .map(InventoryCacheServiceImpl::getInventoryResponse).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }

    }

    @Cacheable(value = "inventory", key = "#id")
    public InventoryResponse getItemDetail(Long id) {
        log.info("Fetching item details");
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);
        if (optionalInventory.isEmpty()) {
            return null;
        }
        Inventory inventory = optionalInventory.get();
        InventoryResponse inventoryResponse = getInventoryResponse(inventory);
        return inventoryResponse;

    }


    @CacheEvict(cacheNames = "inventory", allEntries = true)
    public boolean addItem(InventoryAddRequest addRequest) {
            Inventory inventory = new Inventory();
            inventory.setName(addRequest.getName());
            inventory.setQuantity(addRequest.getQuantity());
            inventoryRepository.save(inventory);
            return true;
    }


    @Transactional
    @CacheEvict(cacheNames = "inventory", key ="#id")
    public boolean updateItem(Long id, InventoryUpdateRequest updateRequest) {
            Optional<Inventory> optionalInventory = inventoryRepository.findById(id);
            if (optionalInventory.isEmpty()) {
                return false;
            }
            Inventory inventory = optionalInventory.get();
            inventory.setName(updateRequest.getName());
            inventory.setQuantity(updateRequest.getQuantity());
            inventoryRepository.save(inventory);
            return true;
    }

    @CacheEvict(cacheNames = "inventory", key ="#id")
    public boolean deleteItem(Long id) {
        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);
        if (optionalInventory.isEmpty()) {
            return false;
        }
        Inventory inventory = optionalInventory.get();
        inventoryRepository.delete(inventory);
        return true;
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
