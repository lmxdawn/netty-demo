package com.example.nettydemo.disruptor.heigh.chain;

import com.lmax.disruptor.EventFactory;

public class TradeFactory implements EventFactory<Trade> {
    @Override
    public Trade newInstance() {
        return new Trade();
    }
}
