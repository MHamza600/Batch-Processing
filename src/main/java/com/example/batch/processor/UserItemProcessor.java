package com.example.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.batch.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserItemProcessor implements ItemProcessor<User, User>{

	private static final Logger log = LoggerFactory.getLogger(UserItemProcessor.class);

	   @Override
	   public User process(final User user) throws Exception {
	      final String firstName = user.getFirstName().toUpperCase();
	      final String lastName = user.getLastName().toUpperCase();
	      final User transformedPerson = new User(firstName, lastName);

	      log.info("Converting (" + user + ") into (" + transformedPerson + ")");
	      return transformedPerson;
	   }
	
}
