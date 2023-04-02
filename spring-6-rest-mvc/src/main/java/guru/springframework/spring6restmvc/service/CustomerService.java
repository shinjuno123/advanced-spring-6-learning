package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    public List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO saveCustomer(CustomerDTO customer);

    void updateCustomer(UUID id, CustomerDTO customer);

    void deleteCustomerById(UUID id);

    void patchCustomer(UUID id, CustomerDTO customer);
}
