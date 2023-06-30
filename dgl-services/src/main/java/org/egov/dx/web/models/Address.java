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
@XStreamAlias("Address")

public class Address {
	
	public Address(Address addressIssuedTo) {
		this.type = addressIssuedTo.type;
		this.line1 = addressIssuedTo.line1;
		this.line2 = addressIssuedTo.line2;
		this.landmark = addressIssuedTo.landmark;
		this.locality =  addressIssuedTo.locality;
		this.district = addressIssuedTo.district;
		this.pin = addressIssuedTo.pin;
		this.state = addressIssuedTo.state;
		this.country = addressIssuedTo.country;
	}

	@XStreamAsAttribute
    @XStreamAlias("type")
    private String type="";
    
	@XStreamAsAttribute
    @XStreamAlias("line1")
    private String line1="";
    
	@XStreamAsAttribute
    @XStreamAlias("line2")
    private String line2="";
    
	@XStreamAsAttribute
    @XStreamAlias("house")
    private String house;
    
	@XStreamAsAttribute
    @XStreamAlias("landmark")
    private String landmark="";
	
	@XStreamAsAttribute
    @XStreamAlias("locality")
    private String locality="";
    
	@XmlAttribute
    @XStreamAlias("vtc")
    private String vtc;
    
	@XStreamAsAttribute
    @XStreamAlias("district")
    private String district="";
    
	@XStreamAsAttribute
    @XStreamAlias("pin")
    private String pin="";
    
	@XStreamAsAttribute
    @XStreamAlias("state")
    private String state="";
    
	@XStreamAsAttribute
    @XStreamAlias("country")
    private String country="IN";
	
}
