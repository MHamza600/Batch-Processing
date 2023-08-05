package com.example.demo.listner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.repository.CustomerRepository;


@Component
public class CustomerJobCompletionListener extends JobExecutionListenerSupport {
	    // Creates an instance of the logger
	    private final CustomerRepository customerRepository;
	    
		private final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);


	    @Autowired
	    public CustomerJobCompletionListener(CustomerRepository customerRepository) {
	        this.customerRepository = customerRepository;
	    }

	    @Override
	    public void afterJob(JobExecution jobExecution) {
	        // When the batch process is completed the the users in the database are retrieved and logged on the application logs
	        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
	        log.info("!!! JOB COMPLETED! verify the results");
	        customerRepository.findAll()
	        .forEach(person -> log.info("Found (" + person + ">) in the database.") );
	        }
	    }
	
}
