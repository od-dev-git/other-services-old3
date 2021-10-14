package org.egov.mr.repository;

import org.egov.mr.web.models.excel.MarriageRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarriageRegistrationExcelRepository extends JpaRepository<MarriageRegistration, String>{

}
