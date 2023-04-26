package org.csu.mypetstore.service;


import org.csu.mypetstore.domain.RefundOrder;
import org.csu.mypetstore.persistence.RefundOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundOrderService {

    @Autowired
    private RefundOrderMapper refundOrderMapper;

    public boolean insertRefundOrder(RefundOrder refundOrder){
        return refundOrderMapper.insertNewRefundOrder(refundOrder);
    }
}
