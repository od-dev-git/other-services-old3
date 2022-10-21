package org.egov.report.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.egov.report.model.Demand;
import org.egov.report.web.contract.User;
//import org.egov.report.web.model.User;
import org.springframework.stereotype.Component;

//import org.egov.demand.model.Demand;
//import org.egov.demand.web.contract.User;

@Component
public class DemandEnrichmentUtil {
    


    /**
     * Collects the list of payer in to map of UUID and object 
     * 
     * and enriches demand array based on the map
     * @param demands
     * @param payers
     * @return
     */
    public List<Demand> enrichPayer(List<Demand> demands, List<User> payers) {

        Map<String, User> map = payers.stream().collect(Collectors.toMap(User::getUuid, Function.identity()));
        List<Demand> rsDemands = new ArrayList<>();

        for (Demand demand : demands) {
            
            if (null == demand.getPayer())
                continue;

            String payerUuid = demand.getPayer().getUuid();
            if (map.containsKey(payerUuid)) {
                demand.setPayer(map.get(payerUuid));
            }
            rsDemands.add(demand);
        }
        return rsDemands;
    }


}
