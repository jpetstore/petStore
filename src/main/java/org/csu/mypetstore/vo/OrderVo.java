package org.csu.mypetstore.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/*订单对象*/
@Data
@Accessors(chain = true)
public class OrderVo {
    /*商户订单号，必填*/
    private String out_trade_no;
    /*订单名称，必填*/
    private String subject;
    /*付款金额，必填*/
    private StringBuffer total_amount;
    /*商品描述，可空*/
    private String body;
    /*超时时间参数*/
    private String timeout_express="10m";
    private String product_code="FAST_INSTANT_TRADE_PAY";

}

