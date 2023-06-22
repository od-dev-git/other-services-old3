package org.egov.dx.web.models.TL;

import java.util.ArrayList;
import java.util.List;

import org.egov.dx.web.models.IssuedTo;
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
@XStreamAlias("IssuedTo")
public class DocDetailsIssuedTo {
	
	@XStreamAlias("Persons")
    private List<DocDetailsPerson> persons=new ArrayList<DocDetailsPerson>();

}
