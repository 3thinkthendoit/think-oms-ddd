package com.think.oms.domain.port.pl.osh.command;

import com.think.oms.domain.model.constant.*;
import com.think.oms.domain.model.valueobject.UserInfo;
import com.think.oms.domain.port.pl.acl.OrderSkuInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 订单创建command
 */
@Data
@Builder
public class OrderCreateCommand {

    /**
     * 订单号
     */
    private String externalOrderNo;

    /**
     * 订单状态
     */
    private OrderStatus orderStatus;

    /**
     * 订单类型
     */
    private OrderType orderType;

    /**
     * 店铺编码
     */
    private String storeCode;

    /**
     * 订单来源
     */
    private OrderSource orderSource;

    /**
     * 订单标题
     */
    private String orderTitle;

    /**
     * 订单金额 单位分
     */
    private Long orderPrice;

    /**
     * 支付方式
     */
    private PayType payType;

    /**
     * 优惠金额
     */
    private Long discountPrice;

    /**
     * 币钟
     */
    private Currency currency;

    /**
     * Sku下单信息
     */
    private List<OrderSkuInfo> orderSkuInfos;

    /**
     * 下单用户信息
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户类型
     */
    private UserInfo.UserType userType;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 收件人
     */
    private String recipient;

    /**
     * 发票信息
     */
    private String invoiceName;

    private String invoiceDetails;

    /**
     * 附件费用信息
     */
    private Map<FeeType,Long > feeAmountMap;

    /**
     * 附加信息
     */
    private Map<String,Object> attachInfos;



    /**
     * 校验数据
     */
    public void validate(){
        Assert.notNull(orderSource,"orderSource is null !!!");
        Assert.notNull(orderPrice,"orderPrice is null !!!");
        Assert.notNull(externalOrderNo,"externalOrderNo is null !!!");
        //其他验证自行补充
    }


}
