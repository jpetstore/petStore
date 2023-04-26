package org.csu.mypetstore.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundOrder {

    private int orderid;

    private BigDecimal refundAmount;

    private String refundReason;

    private boolean isProcessed;

    private boolean isRefused;

    private String refuseReason;

}
