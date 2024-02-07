package org.egov.report.user;

import static org.springframework.util.ObjectUtils.isEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.report.model.enums.UserType;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;
    private String uuid;

    @Size(max = 50)
    private String tenantId;
    private String username;
    private String title;
    private String password;
    private String salutation;

    @Size(max = 50)
    private String name;
    private String mobileNumber;
    
    private String emailId;
    private String altContactNumber;
    private String pan;
    private String aadhaarNumber;
    private Address permanentAddress;
    private Address correspondenceAddress;
    private Set<Address> addresses;
    private Boolean active;
    private Set<Role> roles;
    private Date dob;
    private Date passwordExpiryDate;
    private UserType type;
    private String identificationMark;
    private String signature;
    private String photo;
    private Boolean accountLocked;
    private Long accountLockedDate;
    private Date lastModifiedDate;
    private Date createdDate;
    private String otpReference;
    private Long createdBy;
    private Long lastModifiedBy;
    private Long loggedInUserId;
    private boolean otpValidationMandatory;
    private boolean mobileValidationMandatory = true;
    
    private String guardian;
    private String guardianrelation;

    public User addAddressItem(Address addressItem) {
        if (this.addresses == null) {
            this.addresses = new HashSet<>();
        }
        this.addresses.add(addressItem);
        return this;
    }

    public User addRolesItem(Role roleItem) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(roleItem);
        return this;
    }

    @JsonIgnore
    public boolean isTypeAbsent() {
        return isEmpty(type);
    }

    @JsonIgnore
    public boolean isActiveIndicatorAbsent() {
        return isEmpty(active);
    }

    @JsonIgnore
    public boolean isMobileNumberAbsent() {
        return mobileValidationMandatory && isEmpty(mobileNumber);
    }

    @JsonIgnore
    public boolean isNameAbsent() {
        return isEmpty(name);
    }

    @JsonIgnore
    public boolean isUsernameAbsent() {
        return isEmpty(username);
    }

    @JsonIgnore
    public boolean isTenantIdAbsent() {
        return isEmpty(tenantId);
    }

    @JsonIgnore
    public boolean isPasswordAbsent() {
        return isEmpty(password);
    }

    @JsonIgnore
    public boolean isRolesAbsent() {
        return CollectionUtils.isEmpty(roles) || roles.stream().anyMatch(r -> isEmpty(r.getCode()));
    }

    @JsonIgnore
    public boolean isIdAbsent() {
        return id == null;
    }

    public void nullifySensitiveFields() {
        username = null;
        type = null;
        mobileNumber = null;
        password = null;
        passwordExpiryDate = null;
        roles = null;
        accountLocked = null;
        accountLockedDate = null;
    }

    @JsonIgnore
    public boolean isLoggedInUserDifferentFromUpdatedUser() {
        return !id.equals(loggedInUserId);
    }

    public void setRoleToCitizen() {
        type = UserType.CITIZEN;
        roles = Collections.singleton(Role.getCitizenRole());
    }

    public void updatePassword(String newPassword) {
        password = newPassword;
    }


    @JsonIgnore
    public List<Address> getPermanentAndCorrespondenceAddresses() {
        final ArrayList<Address> addresses = new ArrayList<>();
        if (correspondenceAddress != null && correspondenceAddress.isNotEmpty()) {
            addresses.add(correspondenceAddress);
        }
        if (permanentAddress != null && permanentAddress.isNotEmpty()) {
            addresses.add(permanentAddress);
        }
        return addresses;
    }

    public void setDefaultPasswordExpiry(int expiryInDays) {
        if (passwordExpiryDate == null) {
            passwordExpiryDate = DateUtils.addDays(new Date(), expiryInDays);
        }
    }

    public void setActive(boolean isActive) {
        active = isActive;
    }
    
    @JsonIgnore
    public boolean isGuardianNameAbsent() {
        return isEmpty(guardian);
    }
    
    @JsonIgnore
    public boolean isGuardianRelationAbsent() {
        return isEmpty(guardianrelation);
    }
}
