package org.egov.report.service;

import java.util.HashMap;
import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.egov.report.config.ReportServiceConfiguration;
import org.egov.report.model.BillResponse;
import org.egov.report.repository.IssueFixRepository;
import org.egov.report.repository.ServiceRepository;
import org.egov.report.web.model.PropertyResponse;
import org.egov.report.web.model.RequestInfoWrapper;
import org.egov.report.web.model.Transaction;
import org.egov.report.web.model.issuefix.IssueFix;
import org.egov.report.web.model.issuefix.IssueFixRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("updateTransactionIssue")
public class UpdateTransactionIssue implements IIssueFixService{

	@Autowired
	private IssueFixRepository repository;
	
	@Autowired
	private ReportServiceConfiguration configuration;
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public IssueFix issueFix(IssueFixRequest issueFixRequest) {
		
		Transaction transaction = checkTransacationStatusinDB(issueFixRequest);
		
		checkIfBillIsActive(transaction, issueFixRequest);
		
		createNewBill(transaction, issueFixRequest);
		
		repository.updateBillIdInTransaction(transaction, issueFixRequest);
		
		updateTransaction(transaction, issueFixRequest);
		
		return issueFixRequest.getIssueFix();
	}

	

	private void updateTransaction(Transaction transaction, IssueFixRequest issueFixRequest) {
		
		String txnId = transaction.getTxnId();
		
		StringBuilder uri = new StringBuilder(configuration.getPgServiceHost())
				.append(configuration.getUpdateTransactionEndpoint()).append("?").append("eg_pg_txnid=").append(txnId);
		
		Object request = RequestInfoWrapper.builder().requestInfo(issueFixRequest.getRequestInfo()).build();

		try {
			Object response = serviceRepository.fetchResult(uri, request);

		} catch (Exception ex) {
			log.info("External Service Call Erorr", ex);
			throw new CustomException("UPDATE_TRANSACTION_ERROR", "Kindly check the status after 4 hours, issue should be fixed...");
		}
		
	}

	private void createNewBill(Transaction transaction, IssueFixRequest issueFixRequest) {
		
		String consumerCode = transaction.getConsumerCode();
		String businessService = transaction.getModule();
		String tenantId = transaction.getTenantId();
		
		StringBuilder uri = new StringBuilder(configuration.getBillingHost())
				.append(configuration.getFetchBillEndpoint()).append("?").append("tenantId=").append(tenantId)
				.append("&").append("consumerCode=").append(consumerCode).append("&").append("businessService=")
				.append(businessService);
		
		Object request = RequestInfoWrapper.builder().requestInfo(issueFixRequest.getRequestInfo()).build();
		
		try {
			Object response = serviceRepository.fetchResult(uri, request);
			BillResponse billResponse = mapper.convertValue(response, BillResponse.class);
			if(!CollectionUtils.isEmpty(billResponse.getBills())) {
				log.info("Fetch bill done for consumer.. moving to next step.."+consumerCode);
			}
		} catch (Exception ex) {
			log.error("External Service Call Erorr", ex);
			throw new CustomException("FETCH_BILL_ISSUE", "Fetch Bill Call is not successfull");
		}
	}

	private void checkIfBillIsActive(Transaction transaction, IssueFixRequest issueFixRequest) {
		
		String billId = transaction.getBillId();
		
		String tenantId = transaction.getTenantId();
		
		String status = repository.getBillStatus(billId, tenantId);
		
		if (!status.equalsIgnoreCase("EXPIRED")) {
			throw new CustomException("NO_ISSUES_FOUND",
					"Transaction status is Success in Sujog Portal.. If issue still exist, Please make an entry in Payment Issues !");
		}
	}

	private Transaction checkTransacationStatusinDB(IssueFixRequest issueFixRequest) {
	
		List<Transaction> transactions = repository.getTransactions(issueFixRequest.getIssueFix());
		
		if(CollectionUtils.isEmpty(transactions) || transactions.size() > 1) {
			throw new CustomException("TRANSACTION_ERROR",
					"Either no or more than one Transaction exist with the provided criteria.. Kindly check once !");
		}
		
		if (transactions.get(0).getTxnStatus().equalsIgnoreCase("SUCCESS")) {
			throw new CustomException("NO_ISSUES_FOUND",
					"Transaction status is Success in Sujog Portal.. If issue still exist, Please make an entry in Payment Issues !");
		}
		
		return transactions.get(0);
	}

}
