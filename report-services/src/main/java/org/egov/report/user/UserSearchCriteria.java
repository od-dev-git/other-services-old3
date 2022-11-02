package org.egov.report.user;

import java.util.List;

import org.egov.report.model.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class UserSearchCriteria {

	private List<Long> id;
    private List<String> uuid;
    private String userName;
    private String name;
    private String mobileNumber;
    private String emailId;
    private boolean fuzzyLogic;
    private Boolean active;
    private Integer offset;
    private Integer limit;
    private List<String> sort;
    private UserType type;
    private String tenantId;
    private List<String> roleCodes;
}
