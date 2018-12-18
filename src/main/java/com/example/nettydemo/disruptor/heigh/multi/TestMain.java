package com.example.nettydemo.disruptor.heigh.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

public class TestMain {
    
    public static void main(String[] args) {
    
        // 创建
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                new EventFactory<Order>() {
                    @Override
                    public Order newInstance() {
                        return new Order();
                    }
                },
                1024,
                new YieldingWaitStrategy());
        // 创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        
        // 构建多消费
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < 10; i++){
            Consumer consumer = new Consumer("C" + i);
            consumers[i] = consumer;
        }
        
        // 构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<Order>(ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);
    
    }
    
    
    static class EventExceptionHandler implements ExceptionHandler {
    
        @Override
        public void handleEventException(Throwable throwable, long l, Object o) {
        
        }
    
        @Override
        public void handleOnStartException(Throwable throwable) {
        
        }
    
        @Override
        public void handleOnShutdownException(Throwable throwable) {
        
        }
    }
    
}
