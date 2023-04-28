package com.tiklup.springsecurity.demo.config;

import com.tiklup.springsecurity.demo.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = "com.tiklup.springsecurity.demo.repository.Employees",
		entityManagerFactoryRef = "employeesEntitityManagerFactory",
		transactionManagerRef = "employeesTransactionManager"
)
public class EmployeeDataSourceConfig {
	

	@Bean
	@ConfigurationProperties(prefix="app.employees")
	public DataSource employeesDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean employeesEntitityManagerFactory(@Qualifier("employeesDataSource")DataSource employeesDataSource, EntityManagerFactoryBuilder builder){
		return builder.dataSource(employeesDataSource).packages(Employee.class).build();
	}

	@Bean
	public PlatformTransactionManager employeesTransactionManager(@Qualifier("employeesEntitityManagerFactory") LocalContainerEntityManagerFactoryBean entityManger){
		return new JpaTransactionManager(Objects.requireNonNull(entityManger.getObject()));
	}

}
