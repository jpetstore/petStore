package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.RefundOrder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface RefundOrderMapper {
    public boolean insertNewRefundOrder(RefundOrder refundOrder);
}
