package com.think.oms.domain.port.pl.osh.event;


import lombok.Data;

@Data
public  class OrderOperationEvent {

    private PublishType publishType;

    private String orderNo;



    public static enum PublishType {

        LOCAL(1,"本地事件"),MQ(2,"MQ事件");

        private int code;
        private String desc;

        private PublishType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }
}
