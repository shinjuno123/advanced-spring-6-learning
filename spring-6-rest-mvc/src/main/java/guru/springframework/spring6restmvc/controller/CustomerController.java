package guru.springframework.spring6restmvc.controller;


import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    public static final String CUSTOMER_PATH = "/api/v1/customers";

    public static final String CUSTOMER_PATH_ID = "/api/v1/customers/{id}";



    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers(){

        return customerService.listCustomers();
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customer){

        CustomerDTO savedCustomer = customerService.saveCustomer(customer);

        HttpHeaders headers =  new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + savedCustomer.getId() );

        return new ResponseEntity<>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("id") UUID id){

        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomer(@PathVariable UUID id, @RequestBody CustomerDTO customer){
        customerService.updateCustomer(id, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable UUID id){
        customerService.deleteCustomerById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomer(@PathVariable UUID id, @RequestBody CustomerDTO customer){
        customerService.patchCustomer(id, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
