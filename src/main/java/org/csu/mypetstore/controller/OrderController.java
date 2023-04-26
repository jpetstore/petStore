package org.csu.mypetstore.controller;

import com.alipay.api.AlipayApiException;
import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.*;
import org.csu.mypetstore.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    @Autowired
    private InventoryService inventoryService;

    @Resource
    private PayService payService;//调用支付服务

    @Resource
    private RefundOrderService refundOrderService;

    @GetMapping("/viewOrderForm")
    public String viewOrderForm(HttpServletRequest request){
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null){
            cart = new ArrayList<>();
            session.setAttribute("cart",cart);
        }
        User account = (User)session.getAttribute("user");
        Order order = new Order();
        order.initOrder(account,cart);

        session.setAttribute("creditCardTypes",order.getCardType());
        session.setAttribute("order",order);
        return "/order/OrderForm";
    }

    @PostMapping("/confirmOrder")
    public String confirmOrder(HttpServletRequest request){
        if(request.getParameter("shippingAddressRequired") != null){
            String cardType = request.getParameter("cardType");
            String creditCard = request.getParameter("creditCard");
            String expiryDate = request.getParameter("expiryDate");
            String shipToFirstName = request.getParameter("shipToFirstName");
            String shipToLastName = request.getParameter("shipToLastName");
            String shipAddress1 = request.getParameter("shipAddress1");
            String shipAddress2 = request.getParameter("shipAddress2");
            String shipCity = request.getParameter("shipCity");
            String shipState = request.getParameter("shipState");
            String shipZip = request.getParameter("shipZip");
            String shipCountry = request.getParameter("shipCountry");
            HttpSession session = request.getSession();
            Order order = (Order)session.getAttribute("order");
            order.setCardType(cardType);
            order.setCreditCard(creditCard);
            order.setExpiryDate(expiryDate);
            order.setShipToFirstName(shipToFirstName);
            order.setShipToLastName(shipToLastName);
            order.setShipAddress1(shipAddress1);
            order.setShipAddress2(shipAddress2);
            order.setShipCity(shipCity);
            order.setShipState(shipState);
            order.setShipZip(shipZip);
            order.setShipCountry(shipCountry);
            //覆盖原来的order
            session.setAttribute("order",order);

            return "order/confirmOrder";
        }else{
            HttpSession session = request.getSession();
            String cardType = request.getParameter("cardType");
            String creditCard = request.getParameter("creditCard");
            String expiryDate = request.getParameter("expiryDate");
            User account = (User)session.getAttribute("user");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String country = request.getParameter("country");
            // 修改订单消息
            Order order = (Order)session.getAttribute("order");
            order.setCardType(cardType);
            order.setCreditCard(creditCard);
            order.setExpiryDate(expiryDate);
            order.setBillToFirstName(firstName);
            order.setBillToLastName(lastName);
            order.setShipToFirstName(account.getFirstname());

            order.setShipToLastName(account.getLastname());
            order.setBillAddress1(address1);
            order.setBillAddress2(address2);
            order.setShipAddress1(account.getAddress1());
            order.setShipAddress2(account.getAddress2());
            order.setBillCity(city);
            order.setShipCity(account.getCity());
            order.setBillState(state);
            order.setShipState(account.getState());
            order.setBillZip(zip);
            order.setShipZip(account.getZip());
            order.setBillCountry(country);
            order.setShipCountry(account.getCountry());

            session.setAttribute("order",order);
            return "order/confirmOrder";
        }
    }

    @PostMapping("/finalOrder")
    @ResponseBody
    public String order(HttpServletRequest request) throws AlipayApiException {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
//进入支付界面,item的order在线加一
       String itemId= order.getLineItems().get(0).getItemId();
     Inventory inventory= inventoryService.getInventory(itemId);
     inventory.setQty(inventory.getQty()-1);
     inventory.setQty_order(inventory.getQty_order()+1);
     inventoryService.updateInventory(inventory);

        return  payService.aliPay(new OrderVo()
                .setBody(order.getUsername())
                .setOut_trade_no(order.getLineItems().get(0).getItemId())
                .setTotal_amount(new StringBuffer().append(order.getTotalPrice()))
                .setSubject("MyPetStore Order"));
    }

    @GetMapping("/return")
    public String PayReturn(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        session.setAttribute("lineItems",order.getLineItems());
        orderService.insertOrder(order);
        User user = (User) session.getAttribute("user");
        String username = user.getUsername();
        List<CartItem> cart = (List<CartItem>)session.getAttribute("cart");
        for (int i = 0; i < cart.size(); i++){
            cartService.updateItemByItemIdAndPay(username, cart.get(i).getItem().getItemId(),true);
        }
        session.removeAttribute("cart");
        //加入
        String itemId= order.getLineItems().get(0).getItemId();
        Inventory inventory= inventoryService.getInventory(itemId);
        inventory.setQty_sold(inventory.getQty_sold()+1);
        inventory.setQty_order(inventory.getQty_order()-1);
        inventoryService.updateInventory(inventory);

//        Item item=new CatalogService().getItem(order.getLineItems().get(0).getItemId());
        model.addAttribute("product",(Product)session.getAttribute("product"));
        model.addAttribute("item",(Item)session.getAttribute("item"));
        return "/order/ViewOrder";
    }

    @GetMapping("/notify")
    public String PayNotify(){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        String itemId= order.getLineItems().get(0).getItemId();
        Inventory inventory= inventoryService.getInventory(itemId);
        inventory.setQty(inventory.getQty()+1);
        inventory.setQty_order(inventory.getQty_order()-1);
        inventoryService.updateInventory(inventory);

        return "支付失败";

    }
	
    @GetMapping("/refundOrder")
    public String Refund(String orderid, String msg){
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setOrderid(Integer.parseInt(orderid));
        Order order = orderService.getOrder(Integer.parseInt(orderid));
        orderService.updateRefundOrderStatus(order);
        refundOrder.setRefundAmount(order.getTotalPrice());
        refundOrder.setRefundReason(msg);
        refundOrderService.insertRefundOrder(refundOrder);
        return "forward:/account/allOrders";
    }

}
