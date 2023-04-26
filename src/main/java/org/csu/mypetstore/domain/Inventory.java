package org.csu.mypetstore.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Inventory implements Serializable {
    String itemId;
    int qty;
    int qty_sold;
    int qty_pre;

    public Inventory() {

    }

    public Inventory(String itemId, int qty, int qty_sold, int qty_pre, int qty_order) {
        this.itemId = itemId;
        this.qty = qty;
        this.qty_sold = qty_sold;
        this.qty_pre = qty_pre;
        this.qty_order = qty_order;
    }

    int qty_order;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getQty_sold() {
        return qty_sold;
    }

    public void setQty_sold(int qty_sold) {
        this.qty_sold = qty_sold;
    }

    public int getQty_pre() {
        return qty_pre;
    }

    public void setQty_pre(int qty_pre) {
        this.qty_pre = qty_pre;
    }

    public int getQty_order() {
        return qty_order;
    }

    public void setQty_order(int qty_order) {
        this.qty_order = qty_order;
    }
}
