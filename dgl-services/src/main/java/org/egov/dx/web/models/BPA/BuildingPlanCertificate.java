package org.egov.dx.web.models.BPA;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.MR.AddressMR;
import org.egov.dx.web.models.MR.MarriageCertificate;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("BuildingPlan")
public class BuildingPlanCertificate {
	
	@XStreamAsAttribute
	@XStreamAlias("siNo")
	private String siNo;
	
	@XStreamAsAttribute
	@XStreamAlias("applicationNo")
	private String applicationNo;
	
	@XStreamAsAttribute
	@XStreamAlias("unitName")
	private String unitName;
	
	@XStreamAsAttribute
	@XStreamAlias("validUpto")
	private String validUpto;
	
	@XStreamAsAttribute
	@XStreamAlias("feesPaid")
	private String feesPaid;

	@XmlAttribute
	@XStreamAlias("Address")
	private AddressBPA address;

	@XmlAttribute
	@XStreamAlias("Area")
	private Area area;

	@XmlAttribute
	@XStreamAlias("ConstructionPermission")
	private ConstructionPermission constructionPermission;

}
