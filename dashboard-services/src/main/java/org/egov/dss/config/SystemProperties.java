package org.egov.dss.config;

import java.util.HashMap;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "masterdata")
public class SystemProperties {
	
	private HashMap<String, String> tenantstable;
	
	private List<String> sparitulbs;
	
	private List<String> urculbs;

}
