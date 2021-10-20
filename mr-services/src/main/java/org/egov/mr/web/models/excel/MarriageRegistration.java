package org.egov.mr.web.models.excel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eg_mr_marriageregistration_migration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarriageRegistration {

	@Id
	private String   id;
	private String   tenantid = "";
	private String   ulbname    = "";
	private String   registrationno    = "";
	private String   applicationno    = "";
	private Long     dateofapplication    ;
	private Long     dateofregistration   ;
	private String   approvedby    = "";
	private String   placeofapproval    = "";
	private String   currentstatus    = "";
	private String   marriageward    = "";
	private String   marriagezone    = "";
	private String   placeofmarriage    = "";
	private String   bridetitle    = "";
	private String   bridefirstname    = "";
	private String   bridemiddlename    = "";
	private String   bridelastname    = "";
	private String   bridefathertitle    = "";
	private String   bridefatherfirstname    = "";
	private String   bridefathermiddlename    = "";
	private String   bridefatherlastname    = "";
	private String   bridemothertitle    = "";
	private String   bridemotherfirstname    = "";
	private String   bridemothermiddlename    = "";
	private String   bridemotherlastname    = "";
	private String   bridecontact    = "";
	private String   brideemailaddress    = "";
	private String   isbridedivyang    = "";
	private String   groomtitle    = "";
	private String   groomfirstname    = "";
	private String   groommiddlename    = "";
	private String   groomlastname    = "";
	private String   groomfathertitle    = "";
	private String   groomfatherfirstname    = "";
	private String   groomfathermiddlename    = "";
	private String   groomfatherlastname    = "";
	private String   groommothertitle    = "";
	private String   groommotherfirstname    = "";
	private String   groommothermiddlename    = "";
	private String   groommotherlastname    = "";
	private String   groomcontact    = "";
	private String   groomemailaddress    = "";
	private String   isgroomdivyang    = "";
	private String   brideaddressline1    = "";
	private String   brideaddressline2    = "";
	private String   brideaddressline3    = "";
	private String   bridecountry    = "";
	private String   bridestate    = "";
	private String   bridedistrict    = "";
	private String   bridepincode    = "";
	private Long     bridedateofbirth   ;
	private String   brideage    = "";
	private String   brideguardianrelationship    = "";
	private String   brideguardianname    = "";
	private String   brideguardianaddressline1    = "";
	private String   brideguardianaddressline2    = "";
	private String   brideguardianaddressline3    = "";
	private String   brideguardiancontact    = "";
	private String   brideguardiancountry    = "";
	private String   brideguardianstate    = "";
	private String   brideguardiandistrict    = "";
	private String   brideguardianemail    = "";
	private String   brideguardianpin    = "";
	private String   groomaddressline1    = "";
	private String   groomaddressline2    = "";
	private String   groomaddressline3    = "";
	private String   groomcountry    = "";
	private String   groomstate    = "";
	private String   groomdistrict    = "";
	private String   groompincode    = "";
	private Long     groomdateofbirth    ;
	private String   groomage    = "";
	private String   groomguardianrelationship    = "";
	private String   groomguardianname    = "";
	private String   groomguardianaddressline1    = "";
	private String   groomguardianaddressline2    = "";
	private String   groomguardianaddressline3    = "";
	private String   groomguardiancountry    = "";
	private String   groomguardianstate    = "";
	private String   groomguardiandistrict    = "";
	private String   groomguardianemail    = "";
	private String   groomguardianpin    = "";
	private String   witness1title    = "";
	private String   witness1address    = "";
	private String   witness1contact    = "";
	private String   witness1country    = "";
	private String   witness1state    = "";
	private String   witness1district    = "";
	private String   witness1pin    = "";
	private String   witness1firstname    = "";
	private String   witness1middlename    = "";
	private String   witness1lastname    = "";
	private String   witness2title    = "";
	private String   witness2address    = "";
	private String   witness2contact    = "";
	private String   witness2country    = "";
	private String   witness2state    = "";
	private String   witness2district    = "";
	private String   witness2pin    = "";
	private String   witness2firstname    = "";
	private String   witness2middlename    = "";
	private String   witness2lastname    = "";
	private Long     createdtime ;

}
