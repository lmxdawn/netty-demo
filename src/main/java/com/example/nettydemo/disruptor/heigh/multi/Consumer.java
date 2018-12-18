package com.example.nettydemo.disruptor.heigh.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消费者
 */
public class Consumer implements WorkHandler<Order> {
    
    private String consumerId;
    
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    
    private Random random = new Random();
    
    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }
    
    @Override
    public void onEvent(Order order) throws Exception {
        Thread.sleep(random.nextInt(5));
        System.out.printf("当前消费者: %s, 消费信息: %s", consumerId, order.getId());
        atomicInteger.incrementAndGet();
    }
    
    public Integer getCount() {
        return atomicInteger.get();
    }
    
}
