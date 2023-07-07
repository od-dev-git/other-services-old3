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
@XStreamAlias("ConstructionPermission")
public class ConstructionPermission {
	
	@XStreamAsAttribute
    @XStreamAlias("name")
    private String name="";
    
	@XStreamAsAttribute
    @XStreamAlias("singleDoubleMulti")
    private String single="";
    
	@XStreamAsAttribute
    @XStreamAlias("others")
    private String others="";

}
