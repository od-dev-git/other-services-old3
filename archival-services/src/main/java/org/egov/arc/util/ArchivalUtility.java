package org.egov.arc.util;

import javax.annotation.PostConstruct;

import org.egov.arc.config.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArchivalUtility {

	private static ArchivalUtility instance;

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
