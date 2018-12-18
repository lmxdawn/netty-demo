package com.example.nettydemo.disruptor.heigh.chain;

import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestMain {
    
    public static void main(String[] args) throws InterruptedException {
    
    
        TradeFactory tradeFactory = new TradeFactory();
        int ringBufferSize = 1024 * 1024;
    
        // 线程池
        ThreadFactory threadFactory = new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable);
            }
        };
    
        // 1. 实例化disruptor对象
        Disruptor<Trade> disruptor = new Disruptor<Trade>(tradeFactory,
                ringBufferSize,
                threadFactory);
    
        disruptor.handleEventsWith(new Handler1());
        
        disruptor.start();
    
        CountDownLatch latch = new CountDownLatch(1);
        // 创建生产者的线程池
        ExecutorService executor = Executors.newCachedThreadPool();
    
        executor.submit(new TradePushlisher(latch, disruptor));
        
        latch.await();
        
        disruptor.shutdown();
        executor.shutdown();
        
    }
    
}
