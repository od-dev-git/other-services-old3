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
    @XStreamAlias("khataNo")
    private String khataNo="";
    
	@XStreamAsAttribute
    @XStreamAlias("plotNo")
    private String plotNo="";
	
	@XStreamAsAttribute
    @XStreamAlias("mauza")
    private String mauza="";
    
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
