package com.example.nettydemo.disruptor.demo1;

import lombok.Data;

import java.io.Serializable;

@Data
public class TranslatorData implements Serializable {
    
    private static final long serialVersionUID = 5639846514390056458L;
    
    private String id;
    private String name;
    private String message;
    
}
