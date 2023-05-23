package org.sujog.dba.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class SujogDbaRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static String QUERY="select public.close_idle_con()";
	
	public void closeIdleConnection() {
		try {
			jdbcTemplate.execute(QUERY);
			log.info("Connetion has been close sucessfully");
		} catch (Exception e) {
			log.info("Problem occure in query execution. Error: "+e.getLocalizedMessage());
		}
	}

}
