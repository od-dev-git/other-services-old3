package org.egov.report.repository.builder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.egov.report.model.DemandDetailAuditRequest;
import org.egov.report.repository.builder.DemandQueryBuilder;
import org.egov.report.web.model.DemandCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DemandQueryBuilder {
    
    

    public static final String PAYMENT_BACKUPDATE_AUDIT_INSERT_QUERY = "INSERT INTO egbs_payment_backupdate_audit (paymentid, isbackupdatesuccess, isreceiptcancellation, errorMessage)"
            + " VALUES (?,?,?,?);";
    
    public static final String PAYMENT_BACKUPDATE_AUDIT_SEARCH_QUERY = "SELECT paymentid FROM egbs_payment_backupdate_audit where paymentid=? AND isbackupdatesuccess=? AND isreceiptcancellation=?;";

    public static final String BASE_DEMAND_QUERY = "SELECT dmd.id AS did,dmd.consumercode AS dconsumercode,"
            + "dmd.consumertype AS dconsumertype,dmd.businessservice AS dbusinessservice,dmd.payer,"
            + "dmd.billexpirytime AS dbillexpirytime, dmd.fixedBillExpiryDate as dfixedBillExpiryDate, "
            + "dmd.taxperiodfrom AS dtaxperiodfrom,dmd.taxperiodto AS dtaxperiodto,"
            + "dmd.minimumamountpayable AS dminimumamountpayable,dmd.createdby AS dcreatedby,"
            + "dmd.lastmodifiedby AS dlastmodifiedby,dmd.createdtime AS dcreatedtime,"
            + "dmd.lastmodifiedtime AS dlastmodifiedtime,dmd.tenantid AS dtenantid,dmd.status,"
            + "dmd.additionaldetails as demandadditionaldetails,dmd.ispaymentcompleted as ispaymentcompleted,"

            + "dmdl.id AS dlid,dmdl.demandid AS dldemandid,dmdl.taxheadcode AS dltaxheadcode,"
            + "dmdl.taxamount AS dltaxamount,dmdl.collectionamount AS dlcollectionamount,"
            + "dmdl.createdby AS dlcreatedby,dmdl.lastModifiedby AS dllastModifiedby,"
            + "dmdl.createdtime AS dlcreatedtime,dmdl.lastModifiedtime AS dllastModifiedtime,"
            + "dmdl.tenantid AS dltenantid,dmdl.additionaldetails as detailadditionaldetails " + "FROM egbs_demand_v1 dmd "
            + "INNER JOIN egbs_demanddetail_v1 dmdl ON dmd.id=dmdl.demandid " + "AND dmd.tenantid=dmdl.tenantid WHERE ";

    public static final String BASE_DEMAND_DETAIL_QUERY = "SELECT "
            + "demanddetail.id AS dlid,demanddetail.demandid AS dldemandid,demanddetail.taxheadcode AS dltaxheadcode,"
            + "demanddetail.taxamount AS dltaxamount,demanddetail.collectionamount AS dlcollectionamount,"
            + "demanddetail.createdby AS dlcreatedby,demanddetail.lastModifiedby AS dllastModifiedby,"
            + "demanddetail.createdtime AS dlcreatedtime,demanddetail.lastModifiedtime AS dllastModifiedtime,"
            + "demanddetail.tenantid AS dltenantid " + " FROM egbs_demanddetail_v1 demanddetail "
                    + "INNER JOIN egbs_demand demand ON demanddetail.demandid=demand.id AND "
                    + "demanddetail.tenantid=demand.tenantid WHERE ";

    public static final String DEMAND_QUERY_ORDER_BY_CLAUSE = "dmd.taxperiodfrom";

    public static final String BASE_DEMAND_DETAIL_QUERY_ORDER_BY_CLAUSE = "dmdl.id";

    public static final String DEMAND_INSERT_QUERY = "INSERT INTO egbs_demand_v1 "
            + "(id,consumerCode,consumerType,businessService,payer,taxPeriodFrom,taxPeriodTo,"
            + "minimumAmountPayable,createdby,lastModifiedby,createdtime,lastModifiedtime,tenantid, status, additionaldetails, billexpirytime, fixedBillExpiryDate) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    public static final String DEMAND_DETAIL_INSERT_QUERY = "INSERT INTO egbs_demanddetail_v1 "
            + "(id,demandid,taxHeadCode,taxamount,collectionamount,"
            + "createdby,lastModifiedby,createdtime,lastModifiedtime,tenantid,additionaldetails)" 
            + " VALUES (?,?,?,?,?,?,?,?,?,?,?);";

    public static final String DEMAND_UPDATE_QUERY = "UPDATE egbs_demand_v1 SET " + "payer=?,taxPeriodFrom=?,"
            + "taxPeriodTo=?,minimumAmountPayable=?,lastModifiedby=?," + "lastModifiedtime=?,tenantid=?,"
            + " status=?,additionaldetails=?,billexpirytime=?,ispaymentcompleted=?, fixedBillExpiryDate=? WHERE id=? AND tenantid=?;";
    
    public static final String DEMAND_DETAIL_UPDATE_QUERY = "UPDATE egbs_demanddetail_v1 SET taxamount=?,collectionamount=?,"
            + "lastModifiedby=?,lastModifiedtime=?, additionaldetails=? WHERE id=? AND demandid=? AND tenantid=?;";

    public static final String DEMAND_AUDIT_INSERT_QUERY = "INSERT INTO egbs_demand_v1_audit "
            + "(demandid,consumerCode,consumerType,businessService,payer,taxPeriodFrom,taxPeriodTo,"
            + "minimumAmountPayable,createdby,createdtime,tenantid, status, additionaldetails,id,billexpirytime, ispaymentcompleted) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    public static final String DEMAND_DETAIL_AUDIT_INSERT_QUERY = "INSERT INTO egbs_demanddetail_v1_audit "
            + "(demanddetailid,demandid,taxHeadCode,taxamount,collectionamount,"
            + "createdby,createdtime,tenantid,additionaldetails,id)" 
            + " VALUES (?,?,?,?,?,?,?,?,?,?);";
    
    public static final String DEMAND_UPDATE_CONSUMERCODE_QUERY="UPDATE egbs_demand_v1 SET consumercode=?, lastmodifiedby=?, lastmodifiedtime=? "
            + " WHERE tenantid=? AND id IN (";
    
    private static final String BASE_DEMAND_AUDIT_QUERY = "SELECT "
            + "edv.id, "
            + "edv.consumercode, "
            + "edv.businessservice, "
            + "edv.taxperiodfrom, "
            + "edv.taxperiodto, "
            + "dense_rank() over (partition by edv.consumercode order by edv2.createdtime) as entryRank, "
            + "initcap(replace(edv2.taxheadcode,'_',' ')) as taxheadcode , "
            + "edv2.taxamount as demandTaxamount, "
            + "sum(edv2.taxamount) over (partition by edv.consumercode order by edv2.createdtime rows between unbounded preceding and current row) as cumulativeTaxamount, "
            + "edv2.collectionamount as demandCollectionamount, "
            + "sum(edv2.collectionamount) over (partition by edv.consumercode order by edv2.createdtime rows between unbounded preceding and current row) as cumulativeCollectionamount, "
            + "edv2.createdby as demandDetailCreatedby, "
            + "edv2.createdtime as demandDetailCreatedtime, "
            + "edv2.lastmodifiedby as demandDetailLastmodifiedby, "
            + "edv2.lastmodifiedtime as demandDetailLastmodifiedtime, "
            + "edv2.additionalDetails ->> 'demandAdjusted' as demandadjusted "
            + "FROM egbs_demand_v1 edv "
            + "INNER JOIN egbs_demanddetail_v1 edv2 ON edv.id = edv2.demandid and edv.status='ACTIVE' "
            + "WHERE edv.tenantid = ?";

        private static final String PAGINATION_AND_SORT = " ORDER BY edv2.createdtime ASC "
            + "LIMIT ? OFFSET ?";

    
    public String getDemandQueryForConsumerCodes(Map<String,Set<String>> businessConsumercodeMap,List<Object> preparedStmtList, String tenantId){
        
        StringBuilder query = new StringBuilder(BASE_DEMAND_QUERY);
        
        query.append("dmd.tenantid=? ");
        preparedStmtList.add(tenantId);
        
        query.append("AND dmd.status='ACTIVE' ");
        
        boolean orFlag = false;
        for (Entry<String, Set<String>> consumerCode : businessConsumercodeMap.entrySet()) {
            
            String businessService = consumerCode.getKey();
            Set<String> consumerCodes = consumerCode.getValue();
            
            if(consumerCodes!=null && !consumerCodes.isEmpty()){
                
                if(orFlag)
                    query.append("OR");
                else
                    query.append("AND");
                
                query.append(" dmd.businessservice='"+businessService+"' AND dmd.consumercode IN ("
                        +getIdQueryForStrings(consumerCodes));
                orFlag=true;
            }
        }
        
        return query.toString();
                }
    
    public String getDemandQuery(DemandCriteria demandCriteria, List<Object> preparedStatementValues) {

        StringBuilder demandQuery = new StringBuilder(BASE_DEMAND_QUERY);

        String tenantId = demandCriteria.getTenantId();
        String[] tenantIdChunks = tenantId.split("\\.");
        
        if(tenantIdChunks.length == 1){
            demandQuery.append(" dmd.tenantid LIKE ? ");
            preparedStatementValues.add(demandCriteria.getTenantId() + '%');
            
            addAndClause(demandQuery);
            demandQuery.append(" dmdl.tenantid LIKE ? ");
            preparedStatementValues.add(demandCriteria.getTenantId() + '%');
        }else{
            demandQuery.append(" dmd.tenantid = ? ");
            preparedStatementValues.add(demandCriteria.getTenantId());
            
            addAndClause(demandQuery);
            demandQuery.append(" dmdl.tenantid = ? ");
            preparedStatementValues.add(demandCriteria.getTenantId());
        }
        

        if (demandCriteria.getStatus() != null) {

            addAndClause(demandQuery);
            demandQuery.append("dmd.status=?");
            preparedStatementValues.add(demandCriteria.getStatus());
        }
        
        if (demandCriteria.getDemandId() != null && !demandCriteria.getDemandId().isEmpty()) {
            addAndClause(demandQuery);
            demandQuery.append("dmd.id IN (" + getIdQueryForStrings(demandCriteria.getDemandId()));
        }
        if (!CollectionUtils.isEmpty(demandCriteria.getPayer())) {
            addAndClause(demandQuery);
            demandQuery.append("dmd.payer IN (" + getIdQueryForStrings(demandCriteria.getPayer()));
        }
        if (demandCriteria.getBusinessService() != null) {
            addAndClause(demandQuery);
            demandQuery.append("dmd.businessservice=?");
            preparedStatementValues.add(demandCriteria.getBusinessService());
        }
        
        if(demandCriteria.getIsPaymentCompleted() != null){
            addAndClause(demandQuery);
            demandQuery.append("dmd.ispaymentcompleted = ?");
            preparedStatementValues.add(demandCriteria.getIsPaymentCompleted());
        }
        
        if (demandCriteria.getPeriodFrom() != null) {
            addAndClause(demandQuery);
            demandQuery.append("dmd.taxPeriodFrom >= ?");
            preparedStatementValues.add(demandCriteria.getPeriodFrom());
        }
        
        if(demandCriteria.getPeriodTo() != null){
            addAndClause(demandQuery);
            demandQuery.append("dmd.taxPeriodTo <= ?");
            preparedStatementValues.add(demandCriteria.getPeriodTo());
        }
        
        if (demandCriteria.getConsumerCode() != null && !demandCriteria.getConsumerCode().isEmpty()) {
            addAndClause(demandQuery);
            demandQuery.append("dmd.consumercode IN ("
            + getIdQueryForStrings(demandCriteria.getConsumerCode()));
        }

        addOrderByClause(demandQuery, DEMAND_QUERY_ORDER_BY_CLAUSE);
//      addPagingClause(demandQuery, preparedStatementValues);

        log.info("the query String for demand : " + demandQuery.toString());
        return demandQuery.toString();
    }

    private static void addOrderByClause(StringBuilder demandQueryBuilder,String columnName) {
        demandQueryBuilder.append(" ORDER BY " + columnName);
    }

    private static void addPagingClause(StringBuilder demandQueryBuilder, List<Object> preparedStatementValues) {
        demandQueryBuilder.append(" LIMIT ?");
        preparedStatementValues.add(500);
        demandQueryBuilder.append(" OFFSET ?");
        preparedStatementValues.add(0);
    }

    private static boolean addAndClause(StringBuilder queryString) {
        queryString.append(" AND ");
        return true;
    }
    
    private static String getIdQueryForStrings(Set<String> idList) {

        StringBuilder query = new StringBuilder();
        if (!idList.isEmpty()) {

            String[] list = idList.toArray(new String[idList.size()]);
            query.append("'"+list[0]+"'");
            for (int i = 1; i < idList.size(); i++) {
                query.append("," + "'"+list[i]+"'");
            }
        }
        return query.append(")").toString();
    }

    public String getDemandDetailsQuery(DemandDetailAuditRequest request, List<Object> preparedStmtList) {
		StringBuilder queryBuilder = new StringBuilder(BASE_DEMAND_AUDIT_QUERY);

		preparedStmtList.add(request.getTenantId());
		
		if (!StringUtils.isEmpty(request.getConsumercode())) {
			queryBuilder.append(" AND edv.consumercode = ?");
			preparedStmtList.add(request.getConsumercode());
		}

		if (request.getTaxperiodfrom() != null) {
			queryBuilder.append(" AND edv.taxperiodfrom >= ?");
			preparedStmtList.add(request.getTaxperiodfrom());
		}

		if (request.getTaxperiodto() != null) {
			queryBuilder.append(" AND edv.taxperiodto <= ?");
			preparedStmtList.add(request.getTaxperiodto());
		}

		if (!StringUtils.isEmpty(request.getDemandid())) {
			queryBuilder.append(" AND edv.id = ?");
			preparedStmtList.add(request.getDemandid());
		}


		if (request.getDemandAdjusted() != null) {
			if(request.getDemandAdjusted()) {
			    queryBuilder.append(" AND edv2.additionalDetails ->> 'demandAdjusted' = ?");
			}else if(!request.getDemandAdjusted()) {
			    queryBuilder.append(" AND (edv2.additionalDetails ->> 'demandAdjusted' != ?  "
			    		+ " or edv2.additionalDetails is null  "
			    		+ "	or edv2.additionalDetails::text ='null' ) ");
			}

		    preparedStmtList.add("Y");
		}
		
		queryBuilder.append(PAGINATION_AND_SORT);

		preparedStmtList.add(request.getPageSize());
		preparedStmtList.add((request.getPageNumber() - 1) * request.getPageSize());
        log.info("Generated SQL Query: {}", queryBuilder.toString());
		return queryBuilder.toString();
	}
	


}
