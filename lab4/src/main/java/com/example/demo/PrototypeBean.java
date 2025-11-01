package com.example.demo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalTime;


@Scope("prototype")
public class PrototypeBean {
    private final String creationTime = LocalTime.now().toString();

    public PrototypeBean() {
        System.out.println("СТВОРЕННЯ PROTOTYPE БІНА");
    }

    public String getTimes() {
        return creationTime;
    }
}