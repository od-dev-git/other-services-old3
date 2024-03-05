package org.egov.mr.repository.builder;

import java.util.List;

import org.egov.mr.config.MRConfiguration;
import org.egov.mr.util.MRConstants;
import org.egov.mr.web.models.MarriageRegistrationSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MRQueryBuilder {

    private MRConfiguration config;

    @Autowired
    public MRQueryBuilder(MRConfiguration config) {
        this.config = config;
    }

    private static final String INNER_JOIN_STRING = " INNER JOIN ";
    private static final String LEFT_OUTER_JOIN_STRING = " LEFT OUTER JOIN ";

    @Value("${egov.receipt.businessserviceMR}")
    private String businessServiceMR;




    private static final String QUERY = "SELECT mr.*,mrp.*,mrc.*,mrca.*," +
            "mrgd.*,mrapldoc.*,mrdscdetails.*,mrverdoc.*,mrw.*,mr.id as mr_originalId,mr.tenantid as mr_tenantId,mr.applicationnumber as mr_applicationnumber,mr.lastModifiedTime as " +
            "mr_lastModifiedTime,mr.createdBy as mr_createdBy,mr.lastModifiedBy as mr_lastModifiedBy,mr.createdTime as " +
            "mr_createdTime,mrp.id as mrp_id,mrp.locality as mrp_locality,mrp.pincode as mrp_pincode,mrp.additionalDetail as mrp_additionalDetail,mrc.id as mrc_id,mrc.title as mrc_title,mrc.firstname as mrc_firstName," +
            "mrca.id as mrca_id,mrca.addressline1 as mrca_addressLine1,mrca.country as mrca_country,mrca.state as mrca_state,mrca.district as mrca_district,mrca.pincode as mrca_pincode,mrca.contact as mrca_contact,mrca.emailaddress as mrca_emailaddress," +
            "mrgd.id as mrgd_id,mrgd.addressline1 as mrgd_addressLine1,mrgd.country as mrgd_country,mrgd.state as mrgd_state,mrgd.district as mrgd_district,mrgd.pincode as mrgd_pincode,mrgd.contact as mrgd_contact,mrgd.emailaddress as mrgd_emailaddress," +
            "mrw.id as mrw_id,mrw.title as mrw_title,mrw.firstname as mrw_firstName,mrw.country as mrw_country,mrw.state as mrw_state,mrw.district as mrw_district,mrw.pincode as mrw_pincode,mrw.contact as mrw_contact," +
            "mrapldoc.id as mr_ap_doc_id,mrapldoc.documenttype as mr_ap_doc_documenttype,mrapldoc.filestoreid as mr_ap_doc_filestoreid,mrapldoc.active as mr_ap_doc_active," +
            "mrdscdetails.id as mr_dsc_details_id,mrdscdetails.documenttype as mr_dsc_details_documenttype,mrdscdetails.documentid  as mr_dsc_details_documentid,mrdscdetails.additionaldetail as  mr_dsc_details_additionaldetail,mrdscdetails.applicationnumber as mr_dsc_details_applicationnumber," +
            "mraptdtl.id as mr_apt_dtl_id,mraptdtl.startTime as mr_apt_dtl_startTime,mraptdtl.endTime as mr_apt_dtl_endTime,mraptdtl.active as mr_apt_dtl_active,mraptdtl.description as mr_apt_dtl_description,mraptdtl.additionalDetail as mr_apt_dtl_additionalDetail," +
            "mrverdoc.id as mr_ver_doc_id,mrverdoc.documenttype as mr_ver_doc_documenttype,mrverdoc.filestoreid as mr_ver_doc_filestoreid,mrverdoc.active as mr_ver_doc_active,mr.istatkalapplication as istatkalapplication,mr.slaendtime as slaEndTime, mr.additionaldetails as additionalDetails FROM eg_mr_application mr " 
            +INNER_JOIN_STRING
            +"eg_mr_marriageplace mrp ON mrp.mr_id = mr.id"
            +INNER_JOIN_STRING
            +"eg_mr_couple mrc ON mrc.mr_id = mr.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_address mrca ON mrca.mr_couple_id = mrc.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_gaurdiandetails mrgd ON mrgd.mr_couple_id = mrc.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_witness mrw ON mrw.mr_couple_id = mrc.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_verificationdocument mrverdoc ON mrverdoc.mr_id = mr.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_dscdetails mrdscdetails ON mrdscdetails.mr_id = mr.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_applicationdocument mrapldoc ON mrapldoc.mr_id = mr.id"
            +LEFT_OUTER_JOIN_STRING
            +"eg_mr_appointmentdetails mraptdtl ON mraptdtl.mr_id = mr.id";
    
    private static final String DSC_PENDING_QUERY = "SELECT * FROM eg_mr_dscdetails";


      private final String paginationWrapper = "SELECT * FROM " +
              "(SELECT *, DENSE_RANK() OVER (ORDER BY mr_lastModifiedTime DESC , mr_originalId) offset_ FROM " +
              "({})" +
              " result) result_offset " +
              "WHERE offset_ > ? AND offset_ <= ?";

      private final String dscPaginationWrapper = "SELECT * FROM " +
              "(SELECT *, DENSE_RANK() OVER (ORDER BY lastModifiedTime DESC) offset_ FROM " +
              "({})" +
              " result) result_offset " +
              "WHERE offset_ > ? AND offset_ <= ?";
      
      private final String IS_INWORKFLOW_QUERY = " mr.status not in ( '"+MRConstants.STATUS_APPROVED+"','"+MRConstants.STATUS_REJECTED+"','" +
    		                                    MRConstants.STATUS_CANCELLED+"')";
      
      public String getMRDscDetailsQuery(MarriageRegistrationSearchCriteria criteria, List<Object> preparedStmtList) {

      	StringBuilder builder = new StringBuilder(DSC_PENDING_QUERY);


      	if (criteria.getTenantId() != null) {
              addClauseIfRequired(preparedStmtList, builder);
              builder.append(" tenantid = ? ");
              preparedStmtList.add(criteria.getTenantId());
          }

      	if (criteria.getEmployeeUuid() != null) {
              addClauseIfRequired(preparedStmtList, builder);
              builder.append(" approvedby = ? ");
              preparedStmtList.add(criteria.getEmployeeUuid());
          }

           builder.append(" AND  documentid is null ");



      	return addDscPaginationWrapper(builder.toString(),preparedStmtList,criteria);

      }




    public String getMRSearchQuery(MarriageRegistrationSearchCriteria criteria, List<Object> preparedStmtList) {

        StringBuilder builder = new StringBuilder(QUERY);

        addBusinessServiceClause(criteria,preparedStmtList,builder);


        if(criteria.getAccountId()!=null && (criteria.getMrIdList() == null || criteria.getMrIdList().isEmpty())){
        	
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" mr.accountid = ? ");
            preparedStmtList.add(criteria.getAccountId());
   
        } else if(criteria.getAccountId()!=null && (criteria.getMrIdList() != null && !criteria.getMrIdList().isEmpty())){
        	
        	addClauseIfRequired(preparedStmtList,builder);
        	preparedStmtList.add(criteria.getAccountId()); 
            builder.append(" (mr.accountid = ? OR LOWER(mr.id) in (").append(createQuery(criteria.getMrIdList())).append("))");
            preparedStmtList.addAll(criteria.getMrIdList());
            
        } else {
        	
            if (criteria.getTenantId() != null) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" mr.tenantid=? ");
                preparedStmtList.add(criteria.getTenantId());
            }
            List<String> ids = criteria.getIds();
            if (!CollectionUtils.isEmpty(ids)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" mr.id IN (").append(createQuery(ids)).append(")");
                addToPreparedStatement(preparedStmtList, ids);
            }

            if(criteria.getOwnerId()!=null){
                addClauseIfRequired(preparedStmtList,builder);
                builder.append(" mr.accountid = ? ");
                preparedStmtList.add(criteria.getOwnerId());
       
            }


            if (criteria.getApplicationNumber() != null) {
            	
            	if(criteria.getApplicationNumber().length() == 6) {
            		addClauseIfRequired(preparedStmtList, builder);
                    builder.append("  LOWER(mr.applicationnumber) like LOWER(?) ");
                    preparedStmtList.add("%"+criteria.getApplicationNumber());
            	}
            	else {
            		addClauseIfRequired(preparedStmtList, builder);
                    builder.append("  LOWER(mr.applicationnumber) = LOWER(?) ");
                    preparedStmtList.add(criteria.getApplicationNumber());
            	}  	
            }
            
            if (criteria.getStatus() != null) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append("  mr.status = ? ");
                preparedStmtList.add(criteria.getStatus());
            }

            List<String> mrNumbers = criteria.getMrNumbers();
            if (!CollectionUtils.isEmpty(mrNumbers)) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append(" LOWER(mr.mrnumber) IN (").append(createQuery(mrNumbers)).append(")");
                addToPreparedStatement(preparedStmtList, mrNumbers);
            }
            
            if (criteria.getApplicationType() != null) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append("  mr.applicationtype = ? ");
                preparedStmtList.add(criteria.getApplicationType());
            }


            if (criteria.getFromDate() != null) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append("  mr.applicationDate >= ? ");
                preparedStmtList.add(criteria.getFromDate());
            }

            if (criteria.getToDate() != null) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append("  mr.applicationDate <= ? ");
                preparedStmtList.add(criteria.getToDate());
            }
            
            if (criteria.getIsTatkalApplication() != null && criteria.getIsTatkalApplication()) {
                addClauseIfRequired(preparedStmtList, builder);
                builder.append("  mr.isTatkalApplication = ? ");
                preparedStmtList.add(criteria.getIsTatkalApplication());
            }
            
            if(criteria.getIsInworkflow() != null && criteria.getIsInworkflow()) {
            	  addClauseIfRequired(preparedStmtList, builder);
            	  builder.append(IS_INWORKFLOW_QUERY);
            }

        }
        
        addClauseIfRequired(preparedStmtList, builder);
        builder.append(" mr.status != ? ");
        preparedStmtList.add(String.valueOf(MRConstants.STATUS_DELETED));

       // enrichCriteriaForUpdateSearch(builder,preparedStmtList,criteria);

        return addPaginationWrapper(builder.toString(),preparedStmtList,criteria);
    }


    private void addBusinessServiceClause(MarriageRegistrationSearchCriteria criteria,List<Object> preparedStmtList,StringBuilder builder){
        if ((criteria.getBusinessService() == null) || (businessServiceMR.equals(criteria.getBusinessService()))) {
            addClauseIfRequired(preparedStmtList, builder);
            builder.append(" (mr.businessservice=? or mr.businessservice isnull) ");
            preparedStmtList.add(businessServiceMR);
        }
    }

    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for( int i = 0; i< length; i++){
            builder.append(" LOWER(?)");
            if(i != length -1) builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList,List<String> ids)
    {
        ids.forEach(id ->{ preparedStmtList.add(id);});
    }


    private String addPaginationWrapper(String query,List<Object> preparedStmtList,MarriageRegistrationSearchCriteria criteria){
        int limit = config.getDefaultLimit();
        int offset = config.getDefaultOffset();
        String finalQuery = paginationWrapper.replace("{}",query);

        if(criteria.getLimit()!=null && criteria.getLimit()<=config.getMaxSearchLimit())
            limit = criteria.getLimit();

        if(criteria.getLimit()!=null && criteria.getLimit()>config.getMaxSearchLimit())
            limit = config.getMaxSearchLimit();

        if(criteria.getOffset()!=null)
            offset = criteria.getOffset();

        preparedStmtList.add(offset);
        preparedStmtList.add(limit+offset);
        
        log.info("preparestatements: ");
        preparedStmtList.forEach(item -> log.info(String.valueOf(item)));
        log.info("finalQuery  :-  "+finalQuery);
        
       return finalQuery;
    }

    
    private String addDscPaginationWrapper(String query,List<Object> preparedStmtList,MarriageRegistrationSearchCriteria criteria){
    	int limit = config.getDefaultLimit();
    	int offset = config.getDefaultOffset();
    	String finalQuery = dscPaginationWrapper.replace("{}",query);

    	if(criteria.getLimit()!=null && criteria.getLimit()<=config.getMaxSearchLimit())
    		limit = criteria.getLimit();

    	if(criteria.getLimit()!=null && criteria.getLimit()>config.getMaxSearchLimit())
    		limit = config.getMaxSearchLimit();

    	if(criteria.getOffset()!=null)
    		offset = criteria.getOffset();

    	preparedStmtList.add(offset);
    	preparedStmtList.add(limit+offset);

    	return finalQuery;
    }

    private static void addClauseIfRequired(List<Object> values, StringBuilder queryString) {
        if (values.isEmpty())
            queryString.append(" WHERE ");
        else {
            queryString.append(" AND");
        }
    }

    public String getMRPlainSearchQuery(MarriageRegistrationSearchCriteria criteria, List<Object> preparedStmtList) {
        StringBuilder builder = new StringBuilder(QUERY);

        List<String> ids = criteria.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            addClauseIfRequired(preparedStmtList,builder);
            builder.append(" mr.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }

        return addPaginationWrapper(builder.toString(), preparedStmtList, criteria);

    }




}
