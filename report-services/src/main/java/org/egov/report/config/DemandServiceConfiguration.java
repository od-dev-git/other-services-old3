package org.egov.report.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class DemandServiceConfiguration {

    
//    @Value("${kafka.topics.save.bill}")
//    private String createBillTopic;
//
//    @Value("${kafka.topics.update.bill}")
//    private String updateBillTopic;
//
//    @Value("${kafka.topics.save.bill.key}")
//    private String createBillTopicKey;
//
//    @Value("${kafka.topics.update.bill.key}")
//    private String updatekBillTopicKey;
//
//    @Value("${bs.bill.seq.name}")
//    private String billSeqName;
//
//    @Value("${bs.billdetail.seq.name}")
//    private String billDetailSeqName;
//    
//    @Value("${bs.bill.billnumber.format}")
//    private String billNumberFormat;
//    
//    @Value("${bs.bill.billnumber.format.module}")
//    private String moduleReplaceStirng;
//    
//    @Value("${bs.bill.billnumber.format.tenantid}")
//    private String tenantIdReplaceString;
//    
//    @Value("${istenantlevelbillnumberingenabled}")
//    private Boolean isTenantLevelBillNumberingEnabled;
//    
//    @Value("${egov.idgen.hostname}")
//    private String idGenHost;
//    
//    @Value("${egov.idgen.uri}")
//    private String idGenUrl;
//
//    @Value("${bs.billaccountdetail.seq.name}")
//    private String billAccDetailSeqName;
//
//    @Value("${kafka.topics.save.demand}")
//    private String createDemandTopic;
//
//    @Value("${kafka.topics.update.demand}")
//    private String updateDemandTopic;
//    
//    @Value("${kafka.topics.receipt.update.demand}")
//    private String updateDemandFromReceipt;
//    
//    @Value("${kafka.topics.demandBill.update.name}")
//    private String updateDemandBillTopicName;
//
//    @Value("${bs.demand.seq.name}")
//    private String demandSeqName;
//
//    @Value("${bs.demanddetail.seq.name}")
//    private String demandDetailSeqName;
//
//    @Value("${bs.billdetail.billnumber.seq.name}")
//    private String billNumSeqName;
//
//    @Value("${kafka.topics.receipt.cancel.name}")
//    private String receiptCancellationTopic;
//    
//    @Value("${kafka.topics.demand.index.name}")
//    private String demandIndexTopic;
    
    
    /*
     * billing service v1.1
     */
    
//    @Value("${egov.mdms.host}")
//    private String mdmsHost;
//
//    @Value("${egov.mdms.search.endpoint}")
//    private String mdmsEndpoint;
    
//    @Value("${user.service.hostname}")
//    private String userServiceHostName;
//
//    @Value("${user.service.searchpath}")
//    private String userServiceSearchPath;
    
//    @Value("${demand.is.user.create.enabled}")
//    private Boolean isUserCreateEnabled;
//    
//    @Value("${egov.user.create.user}")
//    private String userCreateEnpoint;
//
//    @Value("#{${bs.businesscode.demand.updateurl}}")
//    private Map<String, String> businessCodeAndDemandUpdateUrlMap;
    
//    // V2
//    
//    @Value("${kafka.topics.receipt.update.demand.v2}")
//    private String updateDemandFromReceiptV2;
//    
//    @Value("${kafka.topics.receipt.cancel.name.v2}")
//    private String receiptCancellationTopicV2;
//    
//    @Value("${search.pagesize.default}")
//    private String commonSearchDefaultLimit;
    

//    // Apportioning
//    @Value("${egov.apportion.host}")
//    private String apportionHost;
//
//    @Value("${egov.apportion.endpoint}")
//    private String apportionEndpoint;
//
//    // Amendment
//    @Value("${is.amendment.workflow.enabled}")
//    private Boolean isAmendmentworkflowEnabed;
//    
//    @Value("${workflow.host}")
//    private String wfHost;
//
//    @Value("${workflow.transition.path}")
//    private String wfTransitionPath;
//
//    @Value("${workflow.businessservice.search.path}")
//    private String wfBusinessServiceSearchPath;
//
//    @Value("${workflow.processinstance.search.path}")
//    private String wfProcessInstanceSearchPath;
//    
//    @Value("${workflow.open.action}")
//    private String amendmentWfOpenAction;
//    
//    @Value("${workflow.modulename}")
//    private String amendmentWfModuleName;
//    
//    @Value("${amendment.workflow.name}")
//    private String amendmentWfName;
//    
//    @Value("${amendment.id.format.name}")
//    private String amendmentIdFormatName;
    
}
