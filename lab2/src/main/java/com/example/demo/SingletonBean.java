package com.example.demo;


import java.time.LocalTime;


public class SingletonBean {
    private final String creationTime = LocalTime.now().toString();

    public SingletonBean() {
        System.out.println("СТВОРЕННЯ SINGLETON БІН");
    }

    public String getTimes() {
        return creationTime;
    }
}