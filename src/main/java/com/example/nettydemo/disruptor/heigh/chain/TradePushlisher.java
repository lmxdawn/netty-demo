package com.example.nettydemo.disruptor.heigh.chain;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TradePushlisher implements Runnable {
    
    private Disruptor<Trade> disruptor;
    
    private CountDownLatch latch;
    
    public TradePushlisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.disruptor = disruptor;
        this.latch = latch;
    }
    
    @Override
    public void run() {
        
        TradeEventTranslator tradeEventTranslator = new TradeEventTranslator();
        System.out.println(Thread.currentThread().getId());
        for (int i = 0; i < 10; i++){
            disruptor.publishEvent(tradeEventTranslator);
        }
        latch.countDown();
    }
}


class TradeEventTranslator implements EventTranslator<Trade> {
    
    private Random random = new Random();
    
    @Override
    public void translateTo(Trade trade, long l) {
        generateTrade(trade);
    }
    
    private void generateTrade(Trade trade) {
        trade.setPrice(random.nextDouble() * 9999);
    }
}