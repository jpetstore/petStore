package org.csu.mypetstore;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.persistence.OrderMapper;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.List;


@SpringBootTest
class MyPetStoreApplicationTests {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void contextLoads() {
        List<Category> allCategoryList = catalogService.getAllCategoryList();
        Iterator<Category>  CategoryIterator = allCategoryList.iterator();

        while (CategoryIterator.hasNext()){
            System.out.println(CategoryIterator.next());
        }

    }

    @Test
    void getOrderById(){
        List<Order> list = orderMapper.getOrdersByUserId("123");
        for (Order order:
             list) {
            System.out.println(order.getOrderId());
        }
    }

}
