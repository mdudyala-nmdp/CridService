/**
 * 
 */
package com.nmdp.cridservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;

/**
 * @author mdudyala
 *
 */
@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientRequestInfo {

	//Mandatoy Fields
	String firstName;
	String lastName;
	String birthDate;
	String gender;

	//String allNames = lastName + "," + firstName + "(usual)";

	//Optional
	String ssn;
	String mothersMaidenName;
	Integer nmdpRid;
	String cibmtrIubmid;
	Integer cibmtrTeam;
	String ebmtId;
	String ebmtCic;
	//String birthSex = gender;
	//birthSex - Apparently birthsex will be mapped to different column.
	//allNames - Last name

}
