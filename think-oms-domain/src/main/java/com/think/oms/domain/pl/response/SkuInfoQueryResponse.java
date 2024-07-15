package com.think.oms.domain.pl.response;

import com.think.oms.domain.pl.SkuInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkuInfoQueryResponse {

    private List<SkuInfo> skuInfos;
}