package com.zenmid.sds.split;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = { SplitCustomer.class,
		SplitCreditCard.class }, entityManagerFactoryRef = "splitEntityManagerFactory", transactionManagerRef = "splitTransactionManager")
public class SplitJpaConfiguration {

	@Bean
	public LocalContainerEntityManagerFactoryBean splitEntityManagerFactory(
			@Qualifier("splitDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource).packages(SplitCustomer.class, SplitCreditCard.class).build();
	}

	@Bean
	public PlatformTransactionManager splitTransactionManager(
			@Qualifier("splitEntityManagerFactory") LocalContainerEntityManagerFactoryBean splitEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(splitEntityManagerFactory.getObject()));
	}

}