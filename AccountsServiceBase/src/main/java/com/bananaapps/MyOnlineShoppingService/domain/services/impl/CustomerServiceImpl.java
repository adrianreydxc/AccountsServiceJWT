package com.bananaapps.MyOnlineShoppingService.domain.services.impl;

import com.bananaapps.MyOnlineShoppingService.domain.dto.response.CustomerDto;
import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import com.bananaapps.MyOnlineShoppingService.domain.exception.custom.NoSuchCustomerException;
import com.bananaapps.MyOnlineShoppingService.domain.mappers.CustomerMapper;
import com.bananaapps.MyOnlineShoppingService.domain.repositories.CustomerRepository;
import com.bananaapps.MyOnlineShoppingService.domain.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = Customer.builder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerMapper.toDto(savedCustomer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Customer no encontrado con ID: " + id));

        return CustomerMapper.toDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchCustomerException("Customer no encontrado con ID: " + id));

        // Actualizar los datos del cliente
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());

        // Guardar los cambios
        Customer updatedCustomer = customerRepository.save(customer);

        return CustomerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new NoSuchCustomerException("Customer no encontrado con ID: " + id);
        }

        customerRepository.deleteById(id);
    }
}
