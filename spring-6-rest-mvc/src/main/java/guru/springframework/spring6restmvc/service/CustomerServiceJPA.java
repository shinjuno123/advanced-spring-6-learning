package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCutomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCutomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return customerMapper.customerToCutomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresentOrElse( foundCustomer->{
            foundCustomer.setName(customer.getName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());
            atomicReference.set(
                    Optional.of(customerMapper.customerToCutomerDto(customerRepository.save(foundCustomer)))
            );
        }, () -> {
            atomicReference.set(Optional.empty());
        });


        return atomicReference.get();
    }

    @Override
    public boolean deleteCustomerById(UUID id) {
        if(customerRepository.existsById(id)){
            customerRepository.deleteById(id);

            return true;
        }

        return false;
    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO customer) {

    }
}
