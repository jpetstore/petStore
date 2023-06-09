package org.csu.mypetstore.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import org.csu.mypetstore.config.PropertiesConfig;
import org.csu.mypetstore.vo.OrderVo;


/**
 * @Description
 * @Date 2022/3/16 11:52 下午
 * @Author RessMatthew
 * @Version 1.0
 **/

public class AlipayUtil {
    public static String connect(OrderVo orderVo) throws AlipayApiException {
        //1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                PropertiesConfig.getKey("gatewayUrl"),//支付宝网关
                PropertiesConfig.getKey("app_id"),//appid
                PropertiesConfig.getKey("merchant_private_key"),//商户私钥
                "json",
                PropertiesConfig.getKey("charset"),//字符编码格式
                PropertiesConfig.getKey("alipay_public_key"),//支付宝公钥
                PropertiesConfig.getKey("sign_type")//签名方式
        );
        //2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(PropertiesConfig.getKey("return_url"));
        // 服务器异步通知页面路径
        alipayRequest.setNotifyUrl(PropertiesConfig.getKey("notify_url"));
        //封装参数

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderVo.getOut_trade_no());  // 我们自己生成的订单编号
        bizContent.put("total_amount", orderVo.getTotal_amount()); // 订单的总金额
        bizContent.put("subject", orderVo.getSubject());   // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        alipayRequest.setBizContent(bizContent.toString());

        //3、请求支付宝进行付款，并获取支付结果
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println(result);
        //返回付款信息
        return result;
    }
}
