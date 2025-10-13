package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class BeanTesterService {

    private final SingletonBean singletonBean1;
    private final SingletonBean singletonBean2;

    private final PrototypeBean prototypeBean1;
    private final PrototypeBean prototypeBean2;

    public BeanTesterService(SingletonBean singletonBean1,
                             SingletonBean singletonBean2,
                             PrototypeBean prototypeBean1,
                             PrototypeBean prototypeBean2) {
        this.singletonBean1 = singletonBean1;
        this.singletonBean2 = singletonBean2;
        this.prototypeBean1 = prototypeBean1;
        this.prototypeBean2 = prototypeBean2;
    }

    @PostConstruct
    public void test() {
        System.out.println("singleton 1 time = " + singletonBean1.getTimes());
        System.out.println("singleton 2 time = " + singletonBean2.getTimes());

        System.out.println("prototype 1 time = " + prototypeBean1.getTimes());
        System.out.println("prototype 2 time = " + prototypeBean2.getTimes());
    }
}
