package com.think.oms.infrastructure.acl.gateway;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.think.oms.domain.port.pl.acl.SkuFullInfo;
import com.think.oms.domain.port.pl.acl.request.SkuInfoQueryRequest;
import com.think.oms.domain.port.pl.acl.response.SkuInfoQueryResponse;
import com.think.oms.domain.port.gateway.SkuInfoQueryGateway;
import com.think.oms.infrastructure.core.http.HttpClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;

/**
 * 商品信息南向网关
 */
@Component
public class SkuInfoQueryGatewayImpl implements SkuInfoQueryGateway {

    @Resource
    HttpClient httpClient;

    public SkuInfoQueryResponse query(SkuInfoQueryRequest request){
        //查询商品信息
        Map<String,Object> params = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(request.getExternalSkuIds())){
            params.put("externalSkuIds",request.getExternalSkuIds());
        }
        String result = httpClient.post("http://api/sku-service/queryBySkuIds",params);
        List<SkuFullInfo> skuInfos = Lists.newArrayList();
        //解析result pl to SkuInfo对象
        return SkuInfoQueryResponse.builder()
                .skuInfos(skuInfos)
                .build();
    }

}
