package com.example.nettydemo.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class OrderEventProducer {
    
    private RingBuffer<OrderEvent> ringBuffer;
    
    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
    
    
    /**
     * 生产数据
     * @param byteBuffer
     */
    public void setData(ByteBuffer byteBuffer) {
    
        long next = ringBuffer.next();
        
        try {
    
            OrderEvent orderEvent = ringBuffer.get(next);
            orderEvent.setValue(byteBuffer.getLong(0));
    
        } finally {
            ringBuffer.publish(next);
        }
    
    
    }
    
}
