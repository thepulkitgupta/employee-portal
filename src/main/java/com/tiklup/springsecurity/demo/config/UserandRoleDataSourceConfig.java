package com.tiklup.springsecurity.demo.config;

import com.tiklup.springsecurity.demo.entity.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
		basePackages = "com.tiklup.springsecurity.demo.repository.Security",
		entityManagerFactoryRef = "securityEntitityManagerFactory",
		transactionManagerRef = "securityTransactionManager"
)
public class UserandRoleDataSourceConfig {
	

	@Primary
	@Bean
	@ConfigurationProperties(prefix="app.security")
	public DataSource securityDataSourcce() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean securityEntitityManagerFactory(@Qualifier("securityDataSourcce")DataSource securityDataSourcce, EntityManagerFactoryBuilder builder){
		return builder.dataSource(securityDataSourcce).packages(Employee.class).build();
	}

	@Bean
	@Primary
	public PlatformTransactionManager securityTransactionManager(@Qualifier("securityEntitityManagerFactory") LocalContainerEntityManagerFactoryBean entityManger){
		return new JpaTransactionManager(Objects.requireNonNull(entityManger.getObject()));
	}



}
