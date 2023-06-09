package com.eazybytes.controller;

import com.eazybytes.model.Customer;
import com.eazybytes.model.Loans;
import com.eazybytes.repository.CustomerRepository;
import com.eazybytes.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class LoanController {

    private final LoanRepository loanRepository;

    private final CustomerRepository customerRepository;

    @GetMapping("/myLoans")
    // @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);

        if(customers != null && !customers.isEmpty()) {
            List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customers.get(0).getId());

            if(loans != null && !loans.isEmpty()){
                return loans;
            }
        }
        return null;
    }
}
