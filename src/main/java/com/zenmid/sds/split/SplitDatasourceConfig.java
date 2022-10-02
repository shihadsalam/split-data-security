package com.zenmid.sds.split;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SplitDatasourceConfig {

	@Bean
	@ConfigurationProperties("spring.datasource.split")
	public DataSourceProperties splitDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource splitDataSource() {
		return splitDataSourceProperties().initializeDataSourceBuilder().build();
	}

	@Bean
	public JdbcTemplate splitJdbcTemplate(@Qualifier("splitDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
