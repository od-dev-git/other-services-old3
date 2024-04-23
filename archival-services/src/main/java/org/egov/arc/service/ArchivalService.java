package org.egov.arc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.event.TableColumnModelListener;

import org.egov.arc.config.ArchivalConfig;
import org.egov.arc.model.Demand;
import org.egov.arc.model.DemandCriteria;
import org.egov.arc.model.DemandDetail;
import org.egov.arc.repository.ArchivalRepository;
import org.egov.arc.util.ArchivalUtility;
import org.egov.arc.util.Util;
import org.egov.arc.util.Constants.Constants;
import org.egov.arc.validator.ArchivalValidator;
import org.egov.common.contract.request.RequestInfo;
import org.javers.common.collections.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;
import com.jayway.jsonpath.Criteria;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArchivalService {

	@Autowired
	ArchivalValidator archivalValidator;

	@Autowired
	ArchivalRepository archivalRepository;

	@Autowired
	ArchivalConfig archivalConfig;

	@Autowired
	Util util;

	/**
	 * method to insert archival demand into DB
	 * 
	 * @param demandCriteria
	 * @param requestInfo
	 * @return
	 */
	public Set<Demand> insertArchiveDemands(DemandCriteria demandCriteria, RequestInfo requestInfo) {
		// Set<Demand> archivalDemands = getArchiveDemands(demandCriteria, requestInfo);
		Set<Demand> archivalDemands = getArchiveDemandsV2(demandCriteria, requestInfo);
		Set<Demand> distinctDemand = archivalDemands.stream().distinct().collect(Collectors.toSet());
		if (!CollectionUtils.isEmpty(distinctDemand)) {
			log.info("Demand Archival process start for tenant: {} Size: {}", demandCriteria.getTenantId(),
					distinctDemand.size());
			distinctDemand.forEach(d -> log.info("Demand Id for Archival: {}", d));
			archivalRepository.insertArchiveDemands(distinctDemand, demandCriteria);
			log.info("Demand Archival process completed successfully for Tenant: {}", demandCriteria.getTenantId());
		} else {
			log.info("No Demand found under the archival criteria for tenant: {}", demandCriteria.getTenantId());
		}
		return distinctDemand;
	}

	/**
	 * Search method to fetch demands from DB
	 * 
	 * @param demandCriteria
	 * @param requestInfo
	 * @return
	 */
	@SuppressWarnings("null")
	public Set<Demand> getArchiveDemands(DemandCriteria demandCriteria, RequestInfo requestInfo) {
		// Archive the data till last 6 months
		enrichDemandCriteria(demandCriteria);
		Long archivaTillDate = util.getArchivalMonthStartDate(demandCriteria.getArchiveTillMonth());
		log.info("Archival Till Date : " + archivaTillDate.toString());
		List<Demand> demands = new ArrayList<>();
		Set<Demand> archiveDemands = new HashSet<>();		
		Long count = archivalRepository.getDemandCount(demandCriteria);
		log.info("Demand record count :" + count);
		Integer limit = demandCriteria.getLimit();		
		Integer offset = 0;
		if (count > 0) {
			while (count > 0) {
				demandCriteria.setLimit(limit);
				demandCriteria.setOffset(offset);
				List<Demand> demandList = archivalRepository.getDemands(demandCriteria);
				demands.addAll(demandList);
				count = count - limit;
				offset += limit;
			}
		}
		HashMap<String, Demand> demandMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(demands)) {
			for (Demand d : demands) {
				DemandDetail demandDetail = new DemandDetail();
				demandMap.put(d.getId(), d);
				for (DemandDetail dd : d.getDemandDetails()) {
					if (dd.getDemandId().equals(d.getId()) && demandMap.keySet().contains(dd.getDemandId()))
						d.setDemandDetails(Sets.newHashSet(dd));
				}
			}
		}
		List<Demand> distinctDemands = new ArrayList<>();
		for (Entry<String, Demand> dm : demandMap.entrySet()) {
			distinctDemands.add(dm.getValue());
		}

		log.info("Distinct Demands :" + distinctDemands.toString());
		Map<String, List<Demand>> demandGroupByBS = ((Collection<Demand>) distinctDemands).stream()
				.collect(Collectors.groupingBy(Demand::getBusinessService));

		for (Entry<String, List<Demand>> demandByBS : demandGroupByBS.entrySet()) {
			String businessService = demandByBS.getKey();
			switch (businessService) {
			case Constants.WS_ONE_TIME_FEE:
				if (archivalConfig.getDemandWsArchival()) {
					wsApplicationFeeDemandArchival(archivaTillDate, archiveDemands, demandByBS);
				}
				break;

			case Constants.WS:
				if (archivalConfig.getDemandWsArchival()) {
					wsUsageFeeDemandArchival(archivaTillDate, archiveDemands, demandByBS);
				}
				break;

			default:
				break;
			}

		}
		log.info("List of Archival Demands : " + archiveDemands);
		return archiveDemands;
	}

	private List<Demand> getLatestDemandTillArchival(Set<String> businessServices, Long archivaTillDate) {
		DemandCriteria criteria = DemandCriteria.builder().businessServices(businessServices).archiveTillDate(null)
				.periodFrom(archivaTillDate).periodTo(System.currentTimeMillis()).build();
		return archivalRepository.getDemands(criteria);
	}

	/**
	 * @param demandCriteria
	 */
	private void enrichDemandCriteria(DemandCriteria demandCriteria) {
		// demandCriteria.setArchiveTillDate(archivalTillDate);
		if (archivalConfig.getDemandWsArchival()) {
			demandCriteria.setBusinessServices(Sets.newHashSet(Constants.WS_ONE_TIME_FEE, Constants.WS));
		} else if (archivalConfig.getDemandPtArchival()) {
			demandCriteria.setBusinessServices(Sets.newHashSet(Constants.PT, Constants.PT_MUTATION));
		} else if (archivalConfig.getDemandMrArchival()) {
			demandCriteria.setBusinessServices(Sets.newHashSet(Constants.MR));
		} else if (archivalConfig.getDemandTlArchival()) {
			demandCriteria.setBusinessServices(Sets.newHashSet(Constants.TL));
		} else if (archivalConfig.getDemandBpaArchival()) {
			demandCriteria.setBusinessServices(Sets.newHashSet(Constants.BPA_NC_APP_FEE, Constants.BPA_NC_SAN_FEE));
		}
		if (StringUtils.isEmpty(demandCriteria.getArchiveTillMonth()) || demandCriteria.getArchiveTillMonth() == 0) {
			demandCriteria.setArchiveTillMonth(archivalConfig.getDemandArchivalMonth());
		}

	}

	/**
	 * @param archivaTillDate
	 * @param archiveDemands
	 * @param demandByBS
	 */
	private void wsUsageFeeDemandArchival(Long archivalTillDate, Set<Demand> archiveDemands,
			Entry<String, List<Demand>> demandByBS) {
		List<Demand> wsUsageDemand = demandByBS.getValue();

		wsUsageDemand.stream()
				.filter(demand -> demand.getIsPaymentCompleted() && demand.getTaxPeriodFrom() <= archivalTillDate)
				.collect(Collectors.groupingBy(Demand::getConsumerCode)) // Group demands by consumer code
				.forEach((consumerCode, demandsForConsumer) -> {
					List<Demand> sortedDemands = demandsForConsumer.stream()
							.sorted(Comparator.comparing(Demand::getTaxPeriodFrom).reversed())
							.collect(Collectors.toList());

					// Skip the first demand (latest) and filter demands before archivalTillDate
					List<Demand> archivalDemands = sortedDemands.stream().skip(1)
							.filter(demand -> demand.getTaxPeriodFrom() < archivalTillDate)
							.collect(Collectors.toList());

					archiveDemands.addAll(archivalDemands);
				});
	}

	/**
	 * @param archivaTillDate
	 * @param archiveDemands
	 * @param demandByBS
	 */
	private void wsApplicationFeeDemandArchival(Long archivaTillDate, Set<Demand> archiveDemands,
			Entry<String, List<Demand>> demandByBS) {
		List<Demand> wsApplicationDemand = demandByBS.getValue();
		for (Demand demand : wsApplicationDemand) {
			if (demand.getIsPaymentCompleted() && demand.getTaxPeriodFrom() <= archivaTillDate) {
				archiveDemands.add(demand);
			}
		}
	}
	

	/**
	 * Search method to fetch demands from DB
	 * 
	 * @param demandCriteria
	 * @param requestInfo
	 * @return
	 */
	@SuppressWarnings("null")
	public Set<Demand> getArchiveDemandsV2(DemandCriteria demandCriteria, RequestInfo requestInfo) {
		String tenantObj = ArchivalUtility.getSystemProperties().getTenants();
		List<Object> tenants = null;
		if(!CollectionUtils.isEmpty(demandCriteria.getTenantIds())) {
			tenants = demandCriteria.getTenantIds();
		}else {
			 tenants = Arrays.asList(tenantObj.split("\\s+"));
		}
		enrichDemandCriteria(demandCriteria);
		Set<Demand> archiveDemands = new HashSet<>();
		Long archivalTillDate = util.getArchivalMonthStartDate(demandCriteria.getArchiveTillMonth());
		log.info("Archive till date: {}", archivalTillDate);
		demandCriteria.setPeriodTo(archivalTillDate);
		for (Object tenant : tenants) {
			demandCriteria.setTenantId(String.valueOf(tenant));
			Long countForTenant = archivalRepository.getDemandCount(demandCriteria);
			log.info("Total Demand record count till archival date: {} for tenant: {} Business Service: {}",
					countForTenant, tenant, demandCriteria.getBusinessServices());
			log.info("Fetching Demand Data for tenant: {}", tenant);
			List<Demand> demands = archivalRepository.getDemands(demandCriteria);
			if (!CollectionUtils.isEmpty(demands)) {
				Map<String, List<Demand>> demandGroupByBS = demands.stream().flatMap(demand -> demand.getDemandDetails()
						.stream().collect(Collectors.toMap(DemandDetail::getDemandId, dd -> demand, (d1, d2) -> {
							d1.getDemandDetails().add(d2.getDemandDetails().iterator().next());
							return d1;
						})).values().stream()).collect(Collectors.groupingBy(Demand::getBusinessService));

				for (Entry<String, List<Demand>> demandByBS : demandGroupByBS.entrySet()) {
					String businessService = demandByBS.getKey();
					switch (businessService) {
					case Constants.WS_ONE_TIME_FEE:
						if (archivalConfig.getDemandWsArchival()) {
							wsApplicationFeeDemandArchival(archivalTillDate, archiveDemands, demandByBS);
						}
						break;

					case Constants.WS:
						if (archivalConfig.getDemandWsArchival()) {
							wsUsageFeeDemandArchival(archivalTillDate, archiveDemands, demandByBS);
						}
						break;

					default:
						break;
					}

				}
			} else {
				log.info("No Demand found for archival criteria for tenant : {} ", tenant);
			}
		}
		return archiveDemands;
	}

}
