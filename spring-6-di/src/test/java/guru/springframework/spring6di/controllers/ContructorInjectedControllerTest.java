package guru.springframework.spring6di.controllers;

import guru.springframework.spring6di.services.GreetingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ContructorInjectedControllerTest {

    @Autowired
    ContructorInjectedController contructorInjectedController;


    @Test
    void sayHello() {
        System.out.println(contructorInjectedController.sayHello());
    }
}