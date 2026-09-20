package com.think.oms.domain.port.pl.acl.response;

import com.think.oms.domain.port.pl.acl.ShippingCallbackResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingCallbackResponse {

   private ShippingCallbackResult callbackResult;
}
