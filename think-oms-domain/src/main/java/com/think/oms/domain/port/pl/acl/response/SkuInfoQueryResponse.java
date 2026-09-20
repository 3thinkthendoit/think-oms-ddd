package com.think.oms.domain.port.pl.acl.response;

import com.think.oms.domain.port.pl.acl.SkuFullInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkuInfoQueryResponse {

    private List<SkuFullInfo> skuInfos;
}
