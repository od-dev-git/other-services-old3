package org.egov.dss.util;

import javax.annotation.PostConstruct;

import org.egov.dss.config.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DashboardUtility {
	
	private static DashboardUtility instance;
	
	@Autowired
	public SystemProperties properties;
	
	@PostConstruct
	public void fillInstance() {
		instance = this;
	}
	
	public static SystemProperties getSystemProperties() {
		return instance.properties;
	}

	
	

}
