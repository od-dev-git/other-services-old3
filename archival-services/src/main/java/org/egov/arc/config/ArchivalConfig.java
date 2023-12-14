package org.egov.arc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class ArchivalConfig {

	@Value("${demand.pt.archival}")
	private Boolean demandPtArchival;

	@Value("${demand.ws.archival}")
	private Boolean demandWsArchival;

	@Value("${demand.mr.archival}")
	private Boolean demandMrArchival;

	@Value("${demand.tl.archival}")
	private Boolean demandTlArchival;

	@Value("${demand.bpa.archival}")
	private Boolean demandBpaArchival;
	
	@Value("${demand.archive.month}")
	private int demandArchivalMonth;
	
	@Value("${egov.archival.default.offset}")
	private int defaultOffset;
	
	@Value("${egov.archival.default.limit}")
	private int defaultLimit;
	
	@Value("${egov.archival.max.limit}")
	private int maxSearchLimit;
	
	@Value("${archival.insert.batch.size}")
	private int insertBatchSize;
}
