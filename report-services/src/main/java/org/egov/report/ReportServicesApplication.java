package org.egov.report;

import java.util.TimeZone;

import javax.sql.DataSource;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
@ComponentScan(basePackages = { "org.egov.report" })
@EnableAutoConfiguration
@Import({ TracerConfiguration.class })
public class ReportServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportServicesApplication.class, args);
	}
	
	@Value("${app.timezone}")
    private String timeZone;
	
	@Value("${spring.datasource2.driver-class-name}")
	private String dataSourceClassForEmas;
	
	@Value("${spring.datasource2.url}")
	private String dataSourceUrlForEmas;
	
	@Value("${spring.datasource2.username}")
	private String dataSourceUserNameForEmas;
	
	@Value("${spring.datasource2.password}")
	private String dataSourcePasswordForEmas;
	
	@Value("${spring.datasource.driver-class-name}")
	private String dataSourceClassForDigit;
	
	@Value("${spring.datasource.url}")
	private String dataSourceUrlForDigit;
	
	@Value("${spring.datasource.username}")
	private String dataSourceUserNameForDigit;
	
	@Value("${spring.datasource.password}")
	private String dataSourcePasswordForDigit;
	
//	@Bean
//	public ObjectMapper objectMapper() {
//		return new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
//				.setDefaultPropertyInclusion(Include.NON_NULL)
//				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).setTimeZone(TimeZone.getTimeZone(timeZone));
//	}
	
	/*
	 * Bean for EMAS DataSource
	 */
	@Bean
	public DataSource emasDataSource() {

	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(dataSourceClassForEmas);
	    dataSource.setUrl(dataSourceUrlForEmas);
	    dataSource.setUsername(dataSourceUserNameForEmas);
	    dataSource.setPassword(dataSourcePasswordForEmas);

	    return dataSource;

	}

	@Bean(name = "springDataSource2")
    @ConfigurationProperties(prefix = "spring.datasource2")
    public DataSource dataSourceEmas() {
        return emasDataSource();
    }

	/*
	 * Bean for JDBC Template for EMAS DB only
	 */
    @Bean(name = "jdbcTemplateEmas")
    public JdbcTemplate jdbcTemplate2() {
        return new JdbcTemplate(dataSourceEmas());
    }
    
    /*
	 * Bean for Digit DataSource
	 */
	@Bean
	public DataSource digitDataSource() {

	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(dataSourceClassForDigit);
	    dataSource.setUrl(dataSourceUrlForDigit);
	    dataSource.setUsername(dataSourceUserNameForDigit);
	    dataSource.setPassword(dataSourcePasswordForDigit);

	    return dataSource;

	}
    
    @Bean(name = "springDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceDigit() {
        return digitDataSource();
    }

	/*
	 * Bean for JDBC Template for Digit DB only
	 */
    @Primary
    @Bean(name = "jdbcTemplateDigit")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSourceDigit());
    }
    
    @Bean(name = "namedParameterjdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSourceDigit());
    }
    
}
