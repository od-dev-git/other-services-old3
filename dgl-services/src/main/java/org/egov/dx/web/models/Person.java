package org.egov.dx.web.models;

import javax.xml.bind.annotation.XmlAttribute;

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
public class Person {

	@XStreamAsAttribute
	@XStreamAlias("uid")
	private String uid;

	@XStreamAsAttribute
	@XStreamAlias("title")
	private String title;

	@XStreamAsAttribute
	@XStreamAlias("name")
	private String name;

	@XStreamAsAttribute
	@XStreamAlias("phone")
	private String phone;

	@XStreamAsAttribute
	@XStreamAlias("email")
	private String email;

	@XStreamAlias("Address")
	private Address address;

}
