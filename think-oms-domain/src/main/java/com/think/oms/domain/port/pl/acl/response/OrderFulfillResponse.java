package com.think.oms.domain.port.pl.acl.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderFulfillResponse {

   private boolean isFulfill;

   private String msg;
}
