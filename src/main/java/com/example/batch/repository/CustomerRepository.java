package com.example.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.batch.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
