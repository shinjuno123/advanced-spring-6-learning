package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoanController {

    @GetMapping("/myLoans")
    public String getAccountDetails(){
        return "Here are the loan details from the DB";
    }
}
