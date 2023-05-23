package org.sujog.dba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sujog.dba.repository.SujogDbaRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SujogDbaService {
	
	@Autowired
	private SujogDbaRepo sujogDbaRepo;
	
	public void closeIdleConnection() {
		// TODO Auto-generated method stub
		sujogDbaRepo.closeIdleConnection();

	}

}
