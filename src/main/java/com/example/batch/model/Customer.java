package com.example.batch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "person")
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer")
public class Customer {

	@Id // Sets the id field as the primary key in the database table
	@Column(name = "id") // sets the column name for the id property
	@GeneratedValue(strategy = GenerationType.AUTO) // States that the id field should be autogenerated
	private Long id;

	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "first_name")
	private String firstName;

	// A method that returns firstName and Lastname when an object of the class is
	// logged
	@Override
	public String toString() {
		return "firstName: " + firstName + ", lastName: " + lastName;
	}

}
