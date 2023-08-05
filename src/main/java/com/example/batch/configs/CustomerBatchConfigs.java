package com.example.batch.configs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;

import com.example.batch.listner.JobCompletionNotificationListener;
import com.example.batch.model.Customer;
import com.example.batch.model.User;
import com.example.batch.processor.CustomerItemProcessor;
import com.example.batch.repository.CustomerRepository;

@Configuration // Informs Spring that this class contains configurations
@EnableBatchProcessing // Enables batch processing for the application
public class CustomerBatchConfigs {

	    @Autowired
	    public JobBuilderFactory jobBuilderFactory;

	    @Autowired
	    public StepBuilderFactory stepBuilderFactory;

	    @Autowired
	    @Lazy
	    public CustomerRepository customerRepository;

	    // Reads the sample-data.csv file and creates instances of the Person entity for each person from the .csv file.
	    @Bean
	    public FlatFileItemReader<Customer> customerReader() {
	    	
	        return new FlatFileItemReaderBuilder<Customer>()
	                .name("customerReader")
	                .resource(new ClassPathResource("data.csv"))
	                .delimited()
	                .names(new String[]{"firstName", "lastName"})
	                .fieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
	                setTargetType(Customer.class);
	                }})
	                .build();
	    }

	    // Creates the Writer, configuring the repository and the method that will be used to save the data into the database
	    @Bean
	    public RepositoryItemWriter<Customer> customerWriter() {
	        RepositoryItemWriter<Customer> iwriter = new RepositoryItemWriter<>();
	        iwriter.setRepository(customerRepository);
	        iwriter.setMethodName("save");
	        return iwriter;
	    }

	    // Creates an instance of PersonProcessor that converts one data form to another. In our case the data form is maintained.
	    @Bean
	    public CustomerItemProcessor customerProcessor() {
	        return new CustomerItemProcessor();
	    }

	    // Batch jobs are built from steps. A step contains the reader, processor and the writer.
	    @Bean
	    public Step customerstep1()
	    throws Exception {

	        return this.stepBuilderFactory.get("step1")
	        .<Customer, Customer>chunk(5)
	        .reader(customerReader())
	        .processor(customerProcessor())
	        .writer(customerWriter())
	        .build();
	    }

	    // Executes the job, saving the data from .csv file into the database.
	    @Bean
	    @Qualifier("CustomerJob")
	    public Job customerUpdateJob(JobCompletionNotificationListener listener)
	    throws Exception {
	        return this.jobBuilderFactory.get("customerUpdateJob").incrementer(new RunIdIncrementer())
	        .listener(listener).start(customerstep1()).build();
	    }
}
