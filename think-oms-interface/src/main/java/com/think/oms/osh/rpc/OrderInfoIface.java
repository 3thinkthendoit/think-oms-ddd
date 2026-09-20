package com.think.oms.osh.rpc;


import com.think.oms.osh.dto.req.OrderInfoQueryReq;
import com.think.oms.osh.dto.resp.OrderInfoQueryResp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 订单微服务
 */
@RequestMapping("orderInfoIface")
public interface OrderInfoIface {

    @RequestMapping(value = "query",method = RequestMethod.POST)
    public OrderInfoQueryResp query(OrderInfoQueryReq req);
}
