package org.egov.dx.web.models.TL;

import org.egov.dx.web.models.Organization;
import org.egov.dx.web.models.Person;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@XStreamAlias("Person")
public class DocDetailsPerson {
	
	@XStreamAlias("name")
	private String name;
	
	@XStreamAlias("phone")
	private String phone;
	
	@XStreamAlias("gender")
	private String gender;
	
	@XStreamAlias("dob")
	private String dob;

}
