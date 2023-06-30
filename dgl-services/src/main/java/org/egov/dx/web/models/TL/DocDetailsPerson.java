package org.egov.dx.web.models.TL;

import org.egov.dx.web.models.Organization;
import org.egov.dx.web.models.Person;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

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
	@XStreamAsAttribute
	private String name;
	
	@XStreamAlias("phone")
	@XStreamAsAttribute
	private String phone;
	
	@XStreamAlias("gender")
	@XStreamAsAttribute
	private String gender;
	
	@XStreamAlias("dob")
	@XStreamAsAttribute
	private String dob;

}
