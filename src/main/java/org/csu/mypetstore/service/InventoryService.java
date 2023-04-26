package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Inventory;
import org.csu.mypetstore.persistence.ImageMapper;
import org.csu.mypetstore.persistence.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;

@Service


public class InventoryService {
    @Autowired
    InventoryMapper inventoryMapper;

    @Autowired
    ImageMapper imageMapper;

public byte[] getimage(String productId){
    return imageMapper.getImage(productId).getImage();
}
    public void updateInventory(Inventory inventory)
    {
        inventoryMapper.updateInventory(inventory);
    }
    public Inventory getInventory(String itemId)
    {
        return inventoryMapper.getInventory(itemId);
    }
}
