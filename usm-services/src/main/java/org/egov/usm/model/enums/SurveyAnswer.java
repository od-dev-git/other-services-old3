package org.egov.usm.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SurveyAnswer {
	YES("YES"), NO("NO");
	
	private String value;

	SurveyAnswer(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
    public String toString() {
        return name();
    }

	@JsonCreator
	public static SurveyAnswer fromValue(String passedValue) {
		for (SurveyAnswer obj : SurveyAnswer.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}
}
