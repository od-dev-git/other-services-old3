package org.egov.dx.web.models.BPA;

import javax.xml.bind.annotation.XmlAttribute;

import org.egov.dx.web.models.MR.AddressMR;
import org.egov.dx.web.models.MR.MarriageCertificate;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XStreamAlias("BuildingPlanCertificate")
public class BuildingPlanCertificate {

	@XmlAttribute
	@XStreamAlias("approvalNumber")
	private String approvalNumber;

	@XmlAttribute
	@XStreamAlias("applicationNumber")
	private String applicationNumber;

	@XmlAttribute
	@XStreamAlias("approvalDate")
	private Long approvalDate;

	@XmlAttribute
	@XStreamAlias("ownerName")
	private String ownerName;

	@XmlAttribute
	@XStreamAlias("ownerAddress")
	private Address ownerAddress;

}
