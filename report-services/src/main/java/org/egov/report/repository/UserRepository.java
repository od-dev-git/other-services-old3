package org.egov.report.repository;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.egov.report.model.enums.UserType;
import org.egov.report.repository.builder.UserTypeQueryBuilder;
import org.egov.report.repository.rowmapper.UserResultSetExtractor;
import org.egov.report.user.Role;
import org.egov.report.user.User;
import org.egov.report.user.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserRepository {
	
	private JdbcTemplate jdbcTemplate;
	private AddressRepository addressRepository;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private UserTypeQueryBuilder userTypeQueryBuilder;
    private RoleRepository roleRepository;
    private UserResultSetExtractor userResultSetExtractor;
    
    @Autowired
    UserRepository(RoleRepository roleRepository, UserTypeQueryBuilder userTypeQueryBuilder,
                   AddressRepository addressRepository, UserResultSetExtractor userResultSetExtractor,
                   JdbcTemplate jdbcTemplate,
                   NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.userTypeQueryBuilder = userTypeQueryBuilder;
        this.userResultSetExtractor = userResultSetExtractor;
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
	
	public List<User> findAll(UserSearchCriteria userSearch) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		boolean RoleSearchHappend = false;
		List<Long> userIds = new ArrayList<>();
		if (!isEmpty(userSearch.getRoleCodes()) && userSearch.getTenantId() != null) {
			userIds = findUsersWithRole(userSearch);
			RoleSearchHappend = true;
		}
		List<User> users = new ArrayList<>();
		if (RoleSearchHappend) {
			if (!CollectionUtils.isEmpty(userIds)) {
				if (CollectionUtils.isEmpty(userSearch.getId()))
					userSearch.setId(userIds);
				else {
					userSearch
							.setId(userSearch.getId().stream().filter(userIds::contains).collect(Collectors.toList()));
					if (CollectionUtils.isEmpty(userSearch.getId()))
						return users;
				}
				userSearch.setTenantId(null);
				userSearch.setRoleCodes(null);
			} else {
				return users;
			}
		}
		String queryStr = userTypeQueryBuilder.getQuery(userSearch, preparedStatementValues);
		log.debug(queryStr);

		users = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), userResultSetExtractor);
		enrichRoles(users);

		return users;
	}
	
	private void enrichRoles(List<User> users) {

        if (users.isEmpty())
            return;

        Map<String, Role> roleCodeMap = fetchRolesFromMdms(users);

        for (User user : users) {
            if (!isNull(user.getRoles())) {
                for (Role role : user.getRoles()) {
                    if (roleCodeMap.containsKey(role.getCode())) {
                        role.setDescription(roleCodeMap.get(role.getCode()).getDescription());
                        role.setName(roleCodeMap.get(role.getCode()).getName());
                    }
                }
            }
        }
    }
	
	private Map<String, Role> fetchRolesFromMdms(List<User> users) {

		Set<String> roleCodes = new HashSet<>();

		for (User user : users) {
			if (!isNull(user.getRoles()) && !user.getRoles().isEmpty()) {
				for (Role role : user.getRoles())
					roleCodes.add(role.getCode());
			}
		}

		if (roleCodes.isEmpty())
			return Collections.emptyMap();

		Set<Role> validatedRoles = fetchRolesByCode(roleCodes, getStateLevelTenant(users.get(0).getTenantId()));

		Map<String, Role> roleCodeMap = new HashMap<>();

		for (Role validatedRole : validatedRoles)
			roleCodeMap.put(validatedRole.getCode(), validatedRole);

		return roleCodeMap;
	}
	
	public List<Long> findUsersWithRole(UserSearchCriteria userSearch) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		List<Long> usersIds = new ArrayList<>();
		String queryStr = userTypeQueryBuilder.getQueryUserRoleSearch(userSearch, preparedStatementValues);
		log.debug(queryStr);

		usersIds = jdbcTemplate.queryForList(queryStr, preparedStatementValues.toArray(), Long.class);

		return usersIds;
	}
	
	private String getStateLevelTenant(String tenantId) {
        return tenantId.split("\\.")[0];
    }
	
	private Set<Role> fetchRolesByCode(Set<String> roleCodes, String tenantId) {


        Set<Role> validatedRoles = roleRepository.findRolesByCode(roleCodes, tenantId);

        return validatedRoles;
    }

	public boolean isUserPresent(String userName, String tenantId, UserType userType) {

        String query = userTypeQueryBuilder.getUserPresentByUserNameAndTenant();

        final Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put("userName", userName);
        parametersMap.put("tenantId", tenantId);
        parametersMap.put("userType", userType.toString());

        int count = namedParameterJdbcTemplate.queryForObject(query, parametersMap, Integer.class);

        return count > 0;
    }
	
	public List<Map<String, Object>> createUserDetailsReport(
			org.egov.report.model.@Valid UserSearchCriteria userSearchCriteria, String tenantId) {
		log.info("Search Criteria : " + userSearchCriteria.toString());

        String query = userTypeQueryBuilder.getCreateUserDetailsReportQuery(userSearchCriteria, tenantId);

	    List<Map<String, Object>> UserDetailsList =  jdbcTemplate.queryForList(query.toString());

	    if(UserDetailsList.isEmpty())
			return Collections.emptyList();
		return UserDetailsList;
	}
}
