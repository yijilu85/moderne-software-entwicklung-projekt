package com.medieninformatik.patientcare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    public String sayHello() {
        String hello = "hello";
        return hello;
    }

}
