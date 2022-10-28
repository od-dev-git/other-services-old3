package org.egov.report.repository.builder;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Iterator;
import java.util.List;

import org.egov.report.repository.RoleRepository;
import org.egov.report.user.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserTypeQueryBuilder {
	
	@Autowired
    private RoleRepository roleRepository;

    private static final String SELECT_USER_QUERY = "SELECT u.title, u.salutation, u.dob, u.locale, u.username, u" +
            ".password, u.pwdexpirydate,  u.mobilenumber, u.altcontactnumber, u.emailid, u.createddate, u" +
            ".lastmodifieddate,  u.createdby, u.lastmodifiedby, u.active, u.name, u.gender, u.pan, u.aadhaarnumber, u" +
            ".type,  u.version, u.guardian, u.guardianrelation, u.signature, u.accountlocked, u.accountlockeddate, u" +
            ".bloodgroup, u.photo, u.identificationmark,  u.tenantid, u.id, u.uuid, addr.id as addr_id, addr.type as " +
            "addr_type, addr .address as addr_address,  addr.city as addr_city, addr.pincode as addr_pincode, addr" +
            ".tenantid as " +
            "addr_tenantid, addr.userid as addr_userid, ur.role_code as role_code, ur.role_tenantid as role_tenantid \n" +
            "\tFROM eg_user u LEFT OUTER JOIN eg_user_address addr ON u.id = addr.userid AND u.tenantid = addr" +
            ".tenantid LEFT OUTER JOIN eg_userrole_v1 ur ON u.id = ur.user_id AND u.tenantid = ur.user_tenantid  ";

    private static final String PAGINATION_WRAPPER = "SELECT * FROM " +
            "(SELECT *, DENSE_RANK() OVER (ORDER BY id) offset_ FROM " +
            "({baseQuery})" +
            " result) result_offset " +
            "WHERE offset_ > ? AND offset_ <= ?";

    public static final String SELECT_NEXT_SEQUENCE_USER = "select nextval('seq_eg_user')";

    public static final String SELECT_FAILED_ATTEMPTS_BY_USER_SQL = "select user_uuid, ip, attempt_date, active from " +
            "eg_user_login_failed_attempts WHERE user_uuid = :user_uuid AND attempt_date >= :attempt_date AND active " +
            "= 'true' ";

    public static final String INSERT_FAILED_ATTEMPTS_SQL = " INSERT INTO eg_user_login_failed_attempts (user_uuid, " +
            "ip, attempt_date, active) VALUES ( :user_uuid, :ip , :attempt_date, :active ) ";

    public static final String UPDATE_FAILED_ATTEMPTS_SQL = " UPDATE eg_user_login_failed_attempts SET active = " +
            "'false' WHERE user_uuid = :user_uuid";

    private static final String SELECT_USER_ROLE_QUERY = "SELECT distinct(user_id) from eg_userrole_v1 ur";

    @SuppressWarnings("rawtypes")
    public String getQuery(final UserSearchCriteria userSearchCriteria, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SELECT_USER_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, userSearchCriteria);


        addOrderByClause(selectQuery, userSearchCriteria);
        return addPagingClause(selectQuery, preparedStatementValues, userSearchCriteria);
    }

    @SuppressWarnings("rawtypes")
    public String getQueryUserRoleSearch(final UserSearchCriteria userSearchCriteria, final List preparedStatementValues) {
        final StringBuilder selectQuery = new StringBuilder(SELECT_USER_ROLE_QUERY);

        addWhereClauseUserRoles(selectQuery, preparedStatementValues, userSearchCriteria);
        return selectQuery.toString();

    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                final UserSearchCriteria userSearchCriteria) {

        if (CollectionUtils.isEmpty(userSearchCriteria.getId()) && userSearchCriteria.getUserName() == null
                && userSearchCriteria.getName() == null && userSearchCriteria.getEmailId() == null
                && userSearchCriteria.getActive() == null && userSearchCriteria.getTenantId() == null
                && userSearchCriteria.getType() == null && userSearchCriteria.getUuid() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (userSearchCriteria.getId() != null && !userSearchCriteria.getId().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(false, selectQuery);
            selectQuery.append(" u.id IN ( ").append(getQueryForCollection(userSearchCriteria.getId(),
                    preparedStatementValues)).append(" )");
        }

        if (userSearchCriteria.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.tenantid = ?");
            preparedStatementValues.add(userSearchCriteria.getTenantId().trim());
        }

        if (userSearchCriteria.getUserName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.username = ?");
            preparedStatementValues.add(userSearchCriteria.getUserName().trim());
        }

        if (!userSearchCriteria.isFuzzyLogic() && userSearchCriteria.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.name = ?");
            preparedStatementValues.add(userSearchCriteria.getName().trim());
        }

        if (userSearchCriteria.getActive() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.active = ?");
            preparedStatementValues.add(userSearchCriteria.getActive());
        }

        if (userSearchCriteria.getEmailId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.emailid = ?");
            preparedStatementValues.add(userSearchCriteria.getEmailId().trim());
        }
//
//        if (userSearchCriteria.getAadhaarNumber() != null) {
//            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
//            selectQuery.append(" u.aadhaarnumber = ?");
//            preparedStatementValues.add(userSearchCriteria.getAadhaarNumber().trim());
//        }

        if (userSearchCriteria.getMobileNumber() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.mobilenumber = ?");
            preparedStatementValues.add(userSearchCriteria.getMobileNumber().trim());
        }

//        if (userSearchCriteria.getPan() != null) {
//            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
//            selectQuery.append(" u.pan = ?");
//            preparedStatementValues.add(userSearchCriteria.getPan().trim());
//        }

        if (userSearchCriteria.getType() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.type = ?");
            preparedStatementValues.add(userSearchCriteria.getType().toString());
        }

        if (userSearchCriteria.isFuzzyLogic() && userSearchCriteria.getName() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.name like " + "'%").append(userSearchCriteria.getName().trim()).append("%'");
        }

        if (!isEmpty(userSearchCriteria.getUuid())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" u.uuid IN (").append(getQueryForCollection(userSearchCriteria.getUuid(),
                    preparedStatementValues)).append(" )");
        }

//        if(!isEmpty(userSearchCriteria.getRoleCodes())){
//            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
//            selectQuery.append(" ur.role_code IN (").append(getQueryForCollection(userSearchCriteria.getRoleCodes(),
//                    preparedStatementValues)).append(" )");
//        }
    }

    private void addOrderByClause(final StringBuilder selectQuery, final UserSearchCriteria userSearchCriteria) {
        final String sortBy = userSearchCriteria.getSort() != null && !userSearchCriteria.getSort().isEmpty()
                ? " u." + userSearchCriteria.getSort().get(0) : "u.name";
        selectQuery.append(" ORDER BY ").append(sortBy);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
                                   final UserSearchCriteria criteria) {

        if (isNull(criteria.getOffset()))
            criteria.setOffset(0);

        if (criteria.getLimit() != null && criteria.getLimit() != 0) {
            String finalQuery = PAGINATION_WRAPPER.replace("{baseQuery}", selectQuery);
            preparedStatementValues.add(criteria.getOffset());
            preparedStatementValues.add(criteria.getOffset() + criteria.getLimit());

            return finalQuery;
        } else
            return selectQuery.toString();

    }

    private void addWhereClauseUserRoles(final StringBuilder selectQuery, final List preparedStatementValues,
                                         final UserSearchCriteria userSearchCriteria) {


        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (userSearchCriteria.getTenantId() != null) {
            isAppendAndClause = addAndClauseIfRequired(false, selectQuery);
            selectQuery.append(" ur.role_tenantid = ?");
            preparedStatementValues.add(userSearchCriteria.getTenantId().trim());
        }


        if (!isEmpty(userSearchCriteria.getRoleCodes())) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" ur.role_code IN (").append(getQueryForCollection(userSearchCriteria.getRoleCodes(),
                    preparedStatementValues)).append(" )");
        }

    }

    private String getQueryForCollection(List<?> ids, List<Object> preparedStmtList) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = ids.iterator();
        while (iterator.hasNext()) {
            builder.append(" ?");
            preparedStmtList.add(iterator.next());

            if (iterator.hasNext())
                builder.append(",");
        }
        return builder.toString();
    }

    /**
     * This method is always called at the beginning of the method so that and
     * is prepended before the field's predicate is handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    public String getInsertUserQuery() {
        return "insert into eg_user (id,uuid,tenantid,salutation,dob,locale,username,password,pwdexpirydate,mobilenumber,altcontactnumber,emailid,active,name,gender,pan,aadhaarnumber,"
                + "type,guardian,guardianrelation,signature,accountlocked,bloodgroup,photo,identificationmark,createddate,lastmodifieddate,createdby,lastmodifiedby) values (:id,:uuid,:tenantid,:salutation,"
                + ":dob,:locale,:username,:password,:pwdexpirydate,:mobilenumber,:altcontactnumber,:emailid,:active,:name,:gender,:pan,:aadhaarnumber,:type,:guardian,:guardianrelation,:signature,"
                + ":accountlocked,:bloodgroup,:photo,:identificationmark,:createddate,:lastmodifieddate,:createdby,:lastmodifiedby) ";
    }

    public String getUpdateUserQuery() {
        return "update eg_user set salutation=:Salutation,dob=:Dob,locale=:Locale,password=:Password,pwdexpirydate=:PasswordExpiryDate,mobilenumber=:MobileNumber,altcontactnumber=:AltContactNumber,emailid=:EmailId,active=:Active,name=:Name,gender=:Gender,pan=:Pan,aadhaarnumber=:AadhaarNumber,"
                + "type=:Type,guardian=:Guardian,guardianrelation=:GuardianRelation,signature=:Signature," +
                "accountlocked=:AccountLocked, accountlockeddate=:AccountLockedDate, bloodgroup=:BloodGroup," +
                "photo=:Photo, identificationmark=:IdentificationMark,lastmodifieddate=:LastModifiedDate," +
                "lastmodifiedby=:LastModifiedBy where username=:username and tenantid=:tenantid and type=:type";
    }


    public String getUserPresentByUserNameAndTenant() {
        return "select count(*) from eg_user where username =:userName and tenantId =:tenantId and type = :userType ";
    }

}
