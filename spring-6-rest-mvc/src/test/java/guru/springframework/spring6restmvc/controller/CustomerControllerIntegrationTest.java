package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.service.CustomerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerIntegrationTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }




    @Test
    void deleteNotExistingCustomer(){
        assertThrows(NotFoundException.class, ()->{
            customerController.deleteCustomerById(UUID.randomUUID());
        });

    }


    @Transactional
    @Test
    @Rollback
    void deleteExistingCustomer(){
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));


        assertThat(customerRepository.findById(customer.getId())).isEmpty();

    }


    @Test
    void updateNotExistingCustomer(){
        assertThrows(NotFoundException.class, ()->{
           customerController.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Test
    void updateExistingCustomer(){
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCutomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String updatedName = "New Name";
        customerDTO.setName(updatedName);

        ResponseEntity responseEntity = customerController.updateCustomer(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer existingCustomer = customerRepository.findById(customer.getId()).get();

        assertThat(existingCustomer.getName()).isEqualTo(updatedName);
    }


    @Transactional
    @Rollback
    @Test
    void saveNewCustomer(){
        CustomerDTO customerDto = CustomerDTO.builder()
                .name("Junho Shin").build();

        ResponseEntity<CustomerDTO> customerDTOResponseEntity = customerController.saveCustomer(customerDto);

        assertThat(customerDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(customerDTOResponseEntity.getHeaders().getLocation()).isNotNull();

        String responseCustomerId = customerDTOResponseEntity.getHeaders().getLocation().toString().split("/")[4];
        UUID savedUUID = UUID.fromString(responseCustomerId);

        Customer customer = customerRepository.findById(savedUUID).get();

        assertThat(customer).isNotNull();

    }

    @Transactional
    @Rollback
    @Test
    void testEmptyListCustomers(){
        customerRepository.deleteAll();
        List<CustomerDTO> customers = customerController.listCustomers();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void testListCustomers(){
        List<CustomerDTO> customers = customerController.listCustomers();

        assertThat(customers.size()).isEqualTo(4);
    }


    @Test
    void testGetCustomerById(){
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO).isNotNull();
    }

    @Test
    void testGetCustomerByIdAndNotFound(){
        assertThrows(NotFoundException.class, ()->
            customerController.getCustomerById(UUID.randomUUID())
        );

    }




}