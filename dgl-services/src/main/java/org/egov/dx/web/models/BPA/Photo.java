package org.egov.dx.web.models.BPA;

import org.egov.dx.web.models.Address;
import org.egov.dx.web.models.MR.PersonMR;

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
@XStreamAlias("Photo")
public class Photo {
	
	@XStreamAsAttribute
    @XStreamAlias("format")
	private String format = "RAW";

}
