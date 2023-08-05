package com.example.demo.configs;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.listner.*;
import com.example.demo.model.User;
import com.example.demo.processor.UserItemProcessor;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

@Configuration
@EnableBatchProcessing
public class UserBatchConfigs {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;
	
	   @Bean
	   public FlatFileItemReader<User> reader() {
	      FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
	      reader.setResource(new ClassPathResource("file.csv"));
	      reader.setLineMapper(new DefaultLineMapper<User>() {
	         {
	            setLineTokenizer(new DelimitedLineTokenizer() {
	               {
	                  setNames(new String[] { "firstName", "lastName" });
	               }
	            });
	            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {
	               {
	                  setTargetType(User.class);
	               }
	            });
	         }
	      });
	      return reader;
	   }
	   
	   @Bean
	   public UserItemProcessor processor() {
	      return new UserItemProcessor();
	   }

	   @Bean
	   public JdbcBatchItemWriter<User> writer() {
	      JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
	      writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
	      writer.setSql("INSERT INTO USERS (first_name, last_name) VALUES (:firstName, :lastName)");
	      writer.setDataSource(dataSource);
	      return writer;
	   }
	   
	   @Bean
	   @Qualifier("UserJob")
	   public Job importUserJob(JobCompletionNotificationListener listener) {
	      return jobBuilderFactory.get("importUserJob").incrementer(
	         new RunIdIncrementer()).listener(listener).flow(step1()).end().build();
	   }
	   @Bean
	   public Step step1() {
	      return stepBuilderFactory.get("step1").<User, User>chunk(10).reader(reader()).processor(processor()).writer(writer()).build();
	   }
}
