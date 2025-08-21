package com.think.oms.domain.pl;

import lombok.Data;

/**
 * 发货回传结果
 */
@Data
public class ShippingCallbackResult {

    private Integer callStatus;

    private String result;
}
