package com.bananaapps.MyOnlineShoppingService.domain.repositories;

import com.bananaapps.MyOnlineShoppingService.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
