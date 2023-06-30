package org.egov.dx.web.models.MR;

import org.egov.dx.web.models.Address;

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
@XStreamAlias("husband")
public class HusbandResiding {
	
	@XStreamAsAttribute
    @XStreamAlias("residingAt")
    private String residingAt;

}
