package org.csu.mypetstore.persistence;


import org.csu.mypetstore.domain.Inventory;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMapper {

    void updateInventory(Inventory inventory);//修改
    Inventory getInventory(String itemId);//找到对应的item
}
