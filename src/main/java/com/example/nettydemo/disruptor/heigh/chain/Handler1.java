package com.example.nettydemo.disruptor.heigh.chain;

import com.lmax.disruptor.EventHandler;

public class Handler1 implements EventHandler<Trade> {
    
    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        System.out.println(trade.getPrice());
    }
}
