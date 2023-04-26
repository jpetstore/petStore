package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.LineItem;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.domain.Sequence;

import org.csu.mypetstore.persistence.ItemMapper;
import org.csu.mypetstore.persistence.LineItemMapper;
import org.csu.mypetstore.persistence.OrderMapper;
import org.csu.mypetstore.persistence.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Autowired
    private LineItemMapper lineItemMapper;

    public OrderService(){

    }

    public void insertOrder(Order order) {
        order.setOrderId(getNextId("ordernum"));
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = (LineItem) order.getLineItems().get(i);
            String itemId = lineItem.getItemId();
            Integer increment = new Integer(lineItem.getQuantity());
            Map<String, Object> param = new HashMap<String, Object>(2);
            param.put("itemId", itemId);        //itemId当String放入increment当Integer放入Map
            param.put("increment", increment);  //increment当Integer放入Map
            itemMapper.updateInventoryQuantity(param);
        }

        orderMapper.insertOrder(order);
        orderMapper.insertOrderStatus(order);
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = (LineItem) order.getLineItems().get(i);
            lineItem.setOrderId(order.getOrderId());
            lineItemMapper.insertLineItem(lineItem);
        }
    }


    public Order getOrder(int orderId) {
        Order order = orderMapper.getOrder(orderId);
//        order.setLineItems(lineItemMapper.getLineItemsByOrderId(orderId));
//
//        for (int i = 0; i < order.getLineItems().size(); i++) {
//            LineItem lineItem = (LineItem) order.getLineItems().get(i);
//            Item item = itemMapper.getItem(lineItem.getItemId());
//            item.setQuantity(itemMapper.getInventoryQuantity(lineItem.getItemId()));
//            lineItem.setItem(item);
//        }

        return order;
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderMapper.getOrdersByUsername(username);
    }

    public int getNextId(String name) {
        Sequence sequence = new Sequence(name, -1);
        /**
         * @Author RessMatthew
         * @Description sequenceMapper空指针异常
         * @Date 6:57 下午 2022/3/11
         **/
        sequence = sequenceMapper.getSequence(sequence);
        if (sequence == null) {
            throw new RuntimeException("Error: A null sequence was returned from the database (could not get next " + name
                    + " sequence).");
        }
        Sequence parameterObject = new Sequence(name, sequence.getNextId() + 1);
        if(sequenceMapper.updateSequence(parameterObject)){
            return parameterObject.getNextId();
        }else {
            throw new RuntimeException("Can't updateSequence!");
        }

    }

    public List<Order> getOrdersByUserId(String userid){
        return orderMapper.getOrdersByUserId(userid);
    }

    public boolean updateRefundOrderStatus(Order order){ return orderMapper.updateRefundOrderStatus(order);}

}
