package com.think.oms.domain.pl.response;

import com.think.oms.domain.pl.ShippingCallbackResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingCallbackResponse {

   private ShippingCallbackResult callbackResult;
}
