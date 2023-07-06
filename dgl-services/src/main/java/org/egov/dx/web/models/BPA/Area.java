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
@XStreamAlias("Area")
public class Area {
	
	@XStreamAsAttribute
    @XStreamAlias("type")
    private String type="";
    
	@XStreamAsAttribute
    @XStreamAlias("approvedCoveredArea")
    private String approvedCoveredArea="";
    
	@XStreamAsAttribute
    @XStreamAlias("floorArea")
    private String floorArea="";
	
	@XStreamAsAttribute
    @XStreamAlias("approvedDate")
    private String approvedDate="";

}
