package com.example.nettydemo.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

public class OrderEventFactory implements EventFactory<OrderEvent> {
    
    /**
     * 返回空的数据对象
     * @return
     */
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
