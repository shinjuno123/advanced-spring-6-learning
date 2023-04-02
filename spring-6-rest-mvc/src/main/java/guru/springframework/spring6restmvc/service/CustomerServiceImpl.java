package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, CustomerDTO> customers;

    public CustomerServiceImpl() {
        this.customers = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Juno")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Kevin")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Christy")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();


        CustomerDTO customer4 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Mackenzie")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
        customers.put(customer4.getId(), customer4);

    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customers.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customers.get(id));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {

        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customers.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customers.get(id);

        existing.setName(customer.getName());
        existing.setLastModifiedDate(LocalDateTime.now());

    }

    @Override
    public void deleteCustomerById(UUID id) {
        customers.remove(id);
    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customers.get(id);

        if(StringUtils.hasText(existing.getName())){
            existing.setName(customer.getName());
        }

        if(existing.getVersion() != null){
            existing.setVersion(customer.getVersion());
        }

        existing.setLastModifiedDate(LocalDateTime.now());
    }
}
