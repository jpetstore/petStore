package org.csu.mypetstore.persistence;

import org.aspectj.weaver.ast.Or;
import org.csu.mypetstore.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    List<Order> getOrdersByUsername(String username);

    Order getOrder(int orderId);

    boolean insertOrder(Order order);

    boolean insertOrderStatus(Order order);

    List<Order> getOrdersByUserId(String userid);

    boolean updateRefundOrderStatus(Order order);
}
