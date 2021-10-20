package org.egov.mr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.egov.mr.config.MRConfiguration;
import org.egov.mr.repository.MarriageRegistrationExcelRepository;
import org.egov.mr.repository.rowmapper.LegacyExcelRowMapper;
import org.egov.mr.web.models.excel.LegacyRow;
import org.egov.mr.web.models.excel.RowExcel;
import org.egov.mr.web.models.excel.MarriageRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MigrationService {

	@Autowired
	private ExcelService excelService;

	@Autowired
	private LegacyExcelRowMapper legacyExcelRowMapper;

	@Autowired
	private MarriageRegistrationExcelRepository marriageRegistrationExcelRepository ;
	
	@Autowired
	private MRConfiguration config;
	
	final ClassLoader loader = MigrationService.class.getClassLoader();



	public void importMarriageRegistrations( Long skip, Long limit) throws Exception {


		  final InputStream excelFile = loader.getResourceAsStream(config.getMigrationFileName());


		AtomicInteger numOfSuccess = new AtomicInteger();
		AtomicInteger numOfErrors = new AtomicInteger();
		
		List<MarriageRegistration>  migartionList = new ArrayList<MarriageRegistration>();

		excelService.read(excelFile, skip, limit, (RowExcel row) -> {
			LegacyRow legacyRow = null;

			try {
				legacyRow = legacyExcelRowMapper.map(row);
				
				

				MarriageRegistration migratedMarriageRegistration= new 	MarriageRegistration();		

				migratedMarriageRegistration.setId(UUID.randomUUID().toString());
				
				if(legacyRow.getTenantId()!= null )
					migratedMarriageRegistration.setTenantid(legacyRow.getTenantId().trim());


				if(legacyRow.getDateOfApplication()!=null && !legacyRow.getDateOfApplication().trim().isEmpty())
				{
					BigDecimal exponentialValue = new BigDecimal(legacyRow.getDateOfApplication());
					migratedMarriageRegistration.setDateofapplication(Long.parseLong(exponentialValue.toPlainString()));
				}

				if(legacyRow.getDateOfRegistration()!=null && !legacyRow.getDateOfRegistration().trim().isEmpty())
				{
					BigDecimal exponentialValue = new BigDecimal(legacyRow.getDateOfRegistration());
					migratedMarriageRegistration.setDateofregistration(Long.parseLong(exponentialValue.toPlainString()));
					
				}

				if(legacyRow.getBrideDateOfBirth()!=null && !legacyRow.getBrideDateOfBirth().trim().isEmpty())
				{
					BigDecimal exponentialValue = new BigDecimal(legacyRow.getBrideDateOfBirth());
					migratedMarriageRegistration.setBridedateofbirth(Long.parseLong(exponentialValue.toPlainString()));
				}

				if(legacyRow.getGroomDateOfBirth()!=null && !legacyRow.getGroomDateOfBirth().trim().isEmpty())
				{
					BigDecimal exponentialValue = new BigDecimal(legacyRow.getGroomDateOfBirth());
					migratedMarriageRegistration.setGroomdateofbirth(Long.parseLong(exponentialValue.toPlainString()));
				}

				if(legacyRow.getUlbName()!= null )
				{
					migratedMarriageRegistration.setUlbname(legacyRow.getUlbName().trim());
				}


				if(legacyRow.getRegistrationNo()!= null )
				{
					migratedMarriageRegistration.setRegistrationno(legacyRow.getRegistrationNo().trim());
				}

				if(legacyRow.getApplicationNo()!= null )
				{
					migratedMarriageRegistration.setApplicationno(legacyRow.getApplicationNo().trim());
				}

				if(legacyRow.getApprovedBy()!= null )
				{
					migratedMarriageRegistration.setApprovedby(legacyRow.getApprovedBy().trim());
				}
				
				if(legacyRow.getPlaceOfApproval()!= null )
				{
					migratedMarriageRegistration.setPlaceofapproval(legacyRow.getPlaceOfApproval().trim());
				}

				if(legacyRow.getCurrentStatus()!= null )
				migratedMarriageRegistration.setCurrentstatus(legacyRow.getCurrentStatus().trim());

				if(legacyRow.getMarriageWard()!= null )
				migratedMarriageRegistration.setMarriageward(legacyRow.getMarriageWard().trim());

				

				if(legacyRow.getMarriageZone()!= null )
				migratedMarriageRegistration.setMarriagezone(legacyRow.getMarriageZone().trim());

				if(legacyRow.getPlaceOfMarriage()!= null )
				migratedMarriageRegistration.setPlaceofmarriage(legacyRow.getPlaceOfMarriage().trim());

				if(legacyRow.getBrideTitle()!= null )
				migratedMarriageRegistration.setBridetitle(legacyRow.getBrideTitle().trim());

				if(legacyRow.getBrideFirstName()!= null )
				migratedMarriageRegistration.setBridefirstname(legacyRow.getBrideFirstName().trim());

				if(legacyRow.getBrideMiddleName()!= null )
				migratedMarriageRegistration.setBridemiddlename(legacyRow.getBrideMiddleName().trim());

				if(legacyRow.getBrideLastName()!= null )
				migratedMarriageRegistration.setBridelastname(legacyRow.getBrideLastName().trim());

				if(legacyRow.getBrideFatherTitle()!= null )
				migratedMarriageRegistration.setBridefathertitle(legacyRow.getBrideFatherTitle().trim());

				if(legacyRow.getBrideFatherFirstName()!= null )
				migratedMarriageRegistration.setBridefatherfirstname(legacyRow.getBrideFatherFirstName().trim());

				if(legacyRow.getBrideFatherMiddleName()!= null )
				migratedMarriageRegistration.setBridefathermiddlename(legacyRow.getBrideFatherMiddleName().trim());

				if(legacyRow.getBrideFatherLastName()!= null )
				migratedMarriageRegistration.setBridefatherlastname(legacyRow.getBrideFatherLastName().trim());

				if(legacyRow.getBrideMotherTitle()!= null )
				migratedMarriageRegistration.setBridemothertitle(legacyRow.getBrideMotherTitle().trim());

				if(legacyRow.getBrideMotherFirstName()!= null )
				migratedMarriageRegistration.setBridemotherfirstname(legacyRow.getBrideMotherFirstName().trim());

				if(legacyRow.getBrideMotherMiddleName()!= null )
				migratedMarriageRegistration.setBridemothermiddlename(legacyRow.getBrideMotherMiddleName().trim());

				if(legacyRow.getBrideMotherLastName()!= null )
				migratedMarriageRegistration.setBridemotherlastname(legacyRow.getBrideMotherLastName().trim());

				if(legacyRow.getBrideContact()!= null )
				migratedMarriageRegistration.setBridecontact(legacyRow.getBrideContact().trim());
				
			//	===========================================================================================================================================
				
				if(legacyRow.getBrideEmailAddress()!= null )
				migratedMarriageRegistration.setBrideemailaddress(legacyRow.getBrideEmailAddress().trim());

				if(legacyRow.getIsBrideDivyang()!= null )
				migratedMarriageRegistration.setIsbridedivyang(legacyRow.getIsBrideDivyang().trim());

				if(legacyRow.getGroomTitle()!= null )
				migratedMarriageRegistration.setGroomtitle(legacyRow.getGroomTitle().trim());

				if(legacyRow.getGroomFirstName()!= null )
				migratedMarriageRegistration.setGroomfirstname(legacyRow.getGroomFirstName().trim());

				if(legacyRow.getGroomMiddleName()!= null )
				migratedMarriageRegistration.setGroommiddlename(legacyRow.getGroomMiddleName().trim());

				if(legacyRow.getGroomLastName()!= null )
				migratedMarriageRegistration.setGroomlastname(legacyRow.getGroomLastName().trim());

				if(legacyRow.getGroomFatherTitle()!= null )
				migratedMarriageRegistration.setGroomfathertitle(legacyRow.getGroomFatherTitle().trim());

				if(legacyRow.getGroomFatherFirstName()!= null )
				migratedMarriageRegistration.setGroomfatherfirstname(legacyRow.getGroomFatherFirstName().trim());

				if(legacyRow.getGroomFatherMiddleName()!= null )
				migratedMarriageRegistration.setGroomfathermiddlename(legacyRow.getGroomFatherMiddleName().trim());

				if(legacyRow.getGroomFatherLastName()!= null )
				migratedMarriageRegistration.setGroomfatherlastname(legacyRow.getGroomFatherLastName().trim());

				if(legacyRow.getGroomMotherTitle()!= null )
				migratedMarriageRegistration.setGroommothertitle(legacyRow.getGroomMotherTitle().trim());

				if(legacyRow.getGroomMotherFirstName()!= null )
				migratedMarriageRegistration.setGroommotherfirstname(legacyRow.getGroomMotherFirstName().trim());
				
				if(legacyRow.getGroomMotherMiddleName()!= null )
				migratedMarriageRegistration.setGroommothermiddlename(legacyRow.getGroomMotherMiddleName().trim());

				if(legacyRow.getGroomMotherLastName()!= null )
				migratedMarriageRegistration.setGroommotherlastname(legacyRow.getGroomMotherLastName().trim());

				if(legacyRow.getGroomContact()!= null )
				migratedMarriageRegistration.setGroomcontact(legacyRow.getGroomContact().trim());

				if(legacyRow.getGroomEmailAddress()!= null )
				migratedMarriageRegistration.setGroomemailaddress(legacyRow.getGroomEmailAddress().trim());

				if(legacyRow.getIsGroomDivyang()!= null )
				migratedMarriageRegistration.setIsgroomdivyang(legacyRow.getIsGroomDivyang().trim());

				if(legacyRow.getBrideAddressLine1()!= null )
				migratedMarriageRegistration.setBrideaddressline1(legacyRow.getBrideAddressLine1().trim());

				if(legacyRow.getBrideAddressLine2()!= null )
				migratedMarriageRegistration.setBrideaddressline2(legacyRow.getBrideAddressLine2().trim());

				if(legacyRow.getBrideAddressLine3()!= null )
				migratedMarriageRegistration.setBrideaddressline3(legacyRow.getBrideAddressLine3().trim());

				if(legacyRow.getBrideCountry()!= null )
				migratedMarriageRegistration.setBridecountry(legacyRow.getBrideCountry().trim());

				if(legacyRow.getBrideState()!= null )
				migratedMarriageRegistration.setBridestate(legacyRow.getBrideState().trim());

				if(legacyRow.getBrideDistrict()!= null )
				migratedMarriageRegistration.setBridedistrict(legacyRow.getBrideDistrict().trim());

				if(legacyRow.getBridePincode()!= null )
				migratedMarriageRegistration.setBridepincode(legacyRow.getBridePincode().trim());
				
				if(legacyRow.getBrideAge()!= null )
				migratedMarriageRegistration.setBrideage(legacyRow.getBrideAge().trim());

				if(legacyRow.getBrideGuardianRelationship()!= null )
				migratedMarriageRegistration.setBrideguardianrelationship(legacyRow.getBrideGuardianRelationship().trim());

				if(legacyRow.getBrideGuardianName()!= null )
				migratedMarriageRegistration.setBrideguardianname(legacyRow.getBrideGuardianName().trim());

				if(legacyRow.getBrideGuardianAddressLine1()!= null )
				migratedMarriageRegistration.setBrideguardianaddressline1(legacyRow.getBrideGuardianAddressLine1().trim());
				
				if(legacyRow.getBrideGuardianAddressLine2()!= null )
				migratedMarriageRegistration.setBrideguardianaddressline2(legacyRow.getBrideGuardianAddressLine2().trim());
				
				if(legacyRow.getBrideGuardianAddressLine3()!= null )
				migratedMarriageRegistration.setBrideguardianaddressline3(legacyRow.getBrideGuardianAddressLine3().trim());

				if(legacyRow.getBrideGuardianContact()!= null )
				migratedMarriageRegistration.setBrideguardiancontact(legacyRow.getBrideGuardianContact().trim());

				if(legacyRow.getBrideGuardianCountry()!= null )
				migratedMarriageRegistration.setBrideguardiancountry(legacyRow.getBrideGuardianCountry().trim());

				if(legacyRow.getBrideGuardianState()!= null )
				migratedMarriageRegistration.setBrideguardianstate(legacyRow.getBrideGuardianState().trim());

				if(legacyRow.getBrideGuardianDistrict()!= null )
				migratedMarriageRegistration.setBrideguardiandistrict(legacyRow.getBrideGuardianDistrict().trim());

				if(legacyRow.getBrideGuardianEmail()!= null )
				migratedMarriageRegistration.setBrideguardianemail(legacyRow.getBrideGuardianEmail().trim());

				if(legacyRow.getBrideGuardianPin()!= null )
				migratedMarriageRegistration.setBrideguardianpin(legacyRow.getBrideGuardianPin().trim());

				if(legacyRow.getGroomAddressLine1()!= null )
				migratedMarriageRegistration.setGroomaddressline1(legacyRow.getGroomAddressLine1().trim());
				
				if(legacyRow.getGroomAddressLine2()!= null )
				migratedMarriageRegistration.setGroomaddressline2(legacyRow.getGroomAddressLine2().trim());
				
				if(legacyRow.getGroomAddressLine3()!= null )
				migratedMarriageRegistration.setGroomaddressline3(legacyRow.getGroomAddressLine3().trim());

				if(legacyRow.getGroomCountry()!= null )
				migratedMarriageRegistration.setGroomcountry(legacyRow.getGroomCountry().trim());
				
				
				if(legacyRow.getGroomState()!= null )
				migratedMarriageRegistration.setGroomstate(legacyRow.getGroomState().trim());

				if(legacyRow.getGroomDistrict()!= null )
				migratedMarriageRegistration.setGroomdistrict(legacyRow.getGroomDistrict().trim());

				if(legacyRow.getGroomPinCode()!= null )
				migratedMarriageRegistration.setGroompincode(legacyRow.getGroomPinCode().trim());

				if(legacyRow.getGroomAge()!= null )
				migratedMarriageRegistration.setGroomage(legacyRow.getGroomAge().trim());

				if(legacyRow.getGroomGuardianRelationship()!= null )
				migratedMarriageRegistration.setGroomguardianrelationship(legacyRow.getGroomGuardianRelationship().trim());

				if(legacyRow.getGroomGuardianName()!= null )
				migratedMarriageRegistration.setGroomguardianname(legacyRow.getGroomGuardianName().trim());

				if(legacyRow.getGroomGuardianAddressLine1()!= null )
				migratedMarriageRegistration.setGroomguardianaddressline1(legacyRow.getGroomGuardianAddressLine1().trim());
				
				if(legacyRow.getGroomGuardianAddressLine2()!= null )
				migratedMarriageRegistration.setGroomguardianaddressline2(legacyRow.getGroomGuardianAddressLine2().trim());
				
				if(legacyRow.getGroomGuardianAddressLine3()!= null )
				migratedMarriageRegistration.setGroomguardianaddressline3(legacyRow.getGroomGuardianAddressLine3().trim());

				if(legacyRow.getGroomGuardianCountry()!= null )
				migratedMarriageRegistration.setGroomguardiancountry(legacyRow.getGroomGuardianCountry().trim());

				if(legacyRow.getGroomGuardianState()!= null )
				migratedMarriageRegistration.setGroomguardianstate(legacyRow.getGroomGuardianState().trim());

				if(legacyRow.getGroomGuardianDistrict()!= null )
				migratedMarriageRegistration.setGroomguardiandistrict(legacyRow.getGroomGuardianDistrict().trim());

				if(legacyRow.getGroomGuardianEmail()!= null )
				migratedMarriageRegistration.setGroomguardianemail(legacyRow.getGroomGuardianEmail().trim());

				if(legacyRow.getGroomGuardianPin()!= null )
				migratedMarriageRegistration.setGroomguardianpin(legacyRow.getGroomGuardianPin().trim());
				
				if(legacyRow.getWitness1Title()!= null )
				migratedMarriageRegistration.setWitness1title(legacyRow.getWitness1Title().trim());

				if(legacyRow.getWitness1Address()!= null )
				migratedMarriageRegistration.setWitness1address(legacyRow.getWitness1Address().trim());

				if(legacyRow.getWitness1Contact()!= null )
				migratedMarriageRegistration.setWitness1contact(legacyRow.getWitness1Contact().trim());

				if(legacyRow.getWitness1Country()!= null )
				migratedMarriageRegistration.setWitness1country(legacyRow.getWitness1Country().trim());

				if(legacyRow.getWitness1State()!= null )
				migratedMarriageRegistration.setWitness1state(legacyRow.getWitness1State().trim());

				if(legacyRow.getWitness1District()!= null )
				migratedMarriageRegistration.setWitness1district(legacyRow.getWitness1District().trim());

				if(legacyRow.getWitness1Pin()!= null )
				migratedMarriageRegistration.setWitness1pin(legacyRow.getWitness1Pin().trim());

				if(legacyRow.getWitness1FirstName()!= null )
				migratedMarriageRegistration.setWitness1firstname(legacyRow.getWitness1FirstName().trim());

				if(legacyRow.getWitness1MiddleName()!= null )
				migratedMarriageRegistration.setWitness1middlename(legacyRow.getWitness1MiddleName().trim());

				if(legacyRow.getWitness1LastName()!= null )
				migratedMarriageRegistration.setWitness1lastname(legacyRow.getWitness1LastName().trim());

				if(legacyRow.getWitness2Title()!= null )
				migratedMarriageRegistration.setWitness2title(legacyRow.getWitness2Title().trim());

				if(legacyRow.getWitness2Address()!= null )
				migratedMarriageRegistration.setWitness2address(legacyRow.getWitness2Address().trim());
				
				if(legacyRow.getWitness2Contact()!= null )
				migratedMarriageRegistration.setWitness2contact(legacyRow.getWitness2Contact().trim());

				if(legacyRow.getWitness2Country()!= null )
				migratedMarriageRegistration.setWitness2country(legacyRow.getWitness2Country().trim());

				if(legacyRow.getWitness2State()!= null )
				migratedMarriageRegistration.setWitness2state(legacyRow.getWitness2State().trim());

				if(legacyRow.getWitness2District()!= null )
				migratedMarriageRegistration.setWitness2district(legacyRow.getWitness2District().trim());
				
				if(legacyRow.getWitness2Pin()!= null )
				migratedMarriageRegistration.setWitness2pin(legacyRow.getWitness2Pin().trim());
				
				if(legacyRow.getWitness2FirstName()!= null )
				migratedMarriageRegistration.setWitness2firstname(legacyRow.getWitness2FirstName().trim());

				if(legacyRow.getWitness2MiddleName()!= null )
				migratedMarriageRegistration.setWitness2middlename(legacyRow.getWitness2MiddleName().trim());

				if(legacyRow.getWitness2LastName()!= null )
				migratedMarriageRegistration.setWitness2lastname(legacyRow.getWitness2LastName().trim());


				migratedMarriageRegistration.setCreatedtime(new Date().getTime());
				Thread.sleep(1);
				migartionList.add(migratedMarriageRegistration);

				if(numOfSuccess.longValue() %1000 ==0)
				{
					marriageRegistrationExcelRepository.saveAll(migartionList);
					migartionList.clear();
				}


			//	System.out.println(" saved to repoistory "+migratedMarriageRegistration.toString());

				numOfSuccess.getAndIncrement();

				log.info("  numOfSuccess  "+numOfSuccess);

			} catch (Exception e) {
				numOfErrors.getAndIncrement();
				
				log.info(" Failed to save to repoistory with line number "+row.getRowIndex());

				log.info("  numOfErrors  "+numOfErrors);

				e.printStackTrace();
			}

			return true;
		});
		
		if(!migartionList.isEmpty())
		{
			marriageRegistrationExcelRepository.saveAll(migartionList);
			migartionList.clear();
		}

	}

}
