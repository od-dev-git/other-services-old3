package org.egov.dx.web.models.BPA;

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
@XStreamAlias("Address")
public class AddressBPA {
	
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
    @XStreamAlias("holdingNo")
    private String holdingNo="";
    
	@XStreamAsAttribute
    @XStreamAlias("house")
    private String house="";
    
	@XStreamAsAttribute
    @XStreamAlias("wardNo")
    private String wardNo="";
	
	@XStreamAsAttribute
    @XStreamAlias("streetRoad")
    private String streetRoad="";
	
	@XStreamAsAttribute
    @XStreamAlias("toujiNo")
    private String toujiNo="";
	
	@XStreamAsAttribute
    @XStreamAlias("district")
    private String district="";
	
	@XStreamAsAttribute
    @XStreamAlias("state")
    private String state="Odisha";
	
	@XStreamAsAttribute
    @XStreamAlias("country")
    private String country="IN";

}
