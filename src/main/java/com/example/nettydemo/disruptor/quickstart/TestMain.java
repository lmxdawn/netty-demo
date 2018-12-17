package com.example.nettydemo.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestMain {
    
    public static void main(String[] args) {
        
        OrderEventFactory orderEventFactory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024;
        
        // 线程池
        ThreadFactory threadFactory = new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable);
            }
        };
        
        // 1. 实例化disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(orderEventFactory,
                ringBufferSize,
                threadFactory);
        
        // 2. 添加消费者的监听
        disruptor.handleEventsWith(new OrderEventHandler());
        
        // 3. 启动 disruptor
        disruptor.start();
        
        // 获取存储容器
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        
        OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        
        for (long i = 0; i < 100; i++) {
            byteBuffer.putLong(0, i);
            orderEventProducer.setData(byteBuffer);
        }
        
        // 关闭
        disruptor.shutdown();
        
        
    }
    
}
