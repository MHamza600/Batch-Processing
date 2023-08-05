package com.example.demo.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomerItemProcessor implements ItemProcessor<Customer, Customer>{

    private static final Logger logger = LoggerFactory.getLogger(CustomerItemProcessor.class);
    // This method transforms data form one form to another.
    @Override
    public Customer process(final Customer customer) throws Exception {
        final String firstName = customer.getFirstName().toUpperCase();
        final String lastName = customer.getLastName().toUpperCase();
        // Creates a new instance of Person
        final Customer transformedCustomer = new Customer();
        transformedCustomer.setFirstName(firstName);
        transformedCustomer.setLastName(lastName);
        // logs the person entity to the application logs
        logger.info("Converting (" + customer + ") into (" + transformedCustomer + ")");
        return transformedCustomer;
    }
	
}
