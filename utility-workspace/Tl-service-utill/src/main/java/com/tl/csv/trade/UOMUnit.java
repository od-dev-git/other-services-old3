package com.tl.csv.trade;

public enum UOMUnit {
	NA(null), SQFT("SQFT"), NUMBEROFDAYS("Number of Days"),Year("Year"),NOOFMACHINES("Number of Machines"),NUMBEROFROOMS("Number of Rooms"),
	NUMBEROFBEDS("Number of Beds"),NUMBEROFBABMBOO("Number of Bamboo"),NUMBEROFWHEELS("Number of wheels"),NUMBEROFSTUDENTS("Number of Students"),
	NUMBEROFCONSUMERS("Number of Consumers"),TURNOVER("Turnover"),NUMBEROFSCREENS("Number of Screens"),KVA("KVA");

	private final String value;

	private UOMUnit(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
