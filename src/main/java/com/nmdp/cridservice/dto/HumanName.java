package com.nmdp.cridservice.dto;

import lombok.Data;

@Data
public class HumanName {

	String use;
	String family;
	String Given;

	//TODO "use" can be an Enum - official|old|anonymous  as per Robinette Map official - PM , Old- FM ,(still discussion Other names - Annoymous)
	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGiven() {
		return Given;
	}

	public void setGiven(String given) {
		Given = given;
	}


}
	