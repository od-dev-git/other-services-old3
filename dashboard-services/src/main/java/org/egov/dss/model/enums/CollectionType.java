package org.egov.dss.model.enums;




import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CollectionType {
    COUNTER("COUNTER"),
    FIELD("FIELD"),
    ONLINE("ONLINE");

    
    private String value;
    
    CollectionType(String value) {
        this.value = value;
    }
    
    @Override
    @JsonValue
    public String toString() {
    	return String.valueOf(value);
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
	public static CollectionType fromValue(String text) {
        if(text!=null){
            for (CollectionType b : CollectionType.values()) {
                if (0 ==b.value.compareTo(text)) {
                    return b;
                }
            }
        }
		return null;
	}
}
