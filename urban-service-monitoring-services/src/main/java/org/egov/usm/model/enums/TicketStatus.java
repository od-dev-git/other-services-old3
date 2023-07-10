package org.egov.usm.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketStatus {
	OPEN("OPEN"), CLOSED("CLOSED");
	
	private String value;

	TicketStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
    public String toString() {
        return name();
    }

	@JsonCreator
	public static TicketStatus fromValue(String passedValue) {
		for (TicketStatus obj : TicketStatus.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}
}
