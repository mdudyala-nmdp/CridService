/**
 *
 */
package com.nmdp.cridservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author mdudyala
 */

@Entity
@Table(name = "t_unique_patient", schema = "dbo")
@Data
public class PatientEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3571367122017103808L;

	@Id
	private Integer unique_patient_key;
	private Long unique_patient_id;
	private String ssn;
	private Timestamp birthday;
	private String city_of_birth;
	private String state_of_birth;
	private String country_of_birth;
	private String first_name;
	private String last_name;
	private String mothers_maiden_name;
	private String gender;
	private Boolean active;
	private Integer nmdp_rid;
	private String ebmt_id;
	private Integer cibmtr_center_number;
	private Timestamp infusion_date;
	private String cibmtr_iubmid;
	private Integer cibmtr_team;
	private String non_citizen_na;
	private String primary_disease;
	private String previous_hsct_yn;
	private Timestamp date_previous_hsct;
	private String prev_hsct_auto_yn;
	private String prev_hsct_allo_u_yn;
	private String prev_hsct_allo_r_yn;
	private String prev_hsct_syn_yn;
	private String prev_hsct_prod_marrow_yn;
	private String prev_hsct_prod_pbsc_yn;
	private String prev_hsct_prod_cord_yn;
	private String auth_first_name;
	private String auth_last_name;
	private String auth_phone;
	private String auth_fax;
	private String auth_email;
	private Timestamp lst_updt_dte;
	private String lst_updt_id;
	private String auto_tx_consent_yn;
	private String infus_typ;
	private Timestamp todays_dte;
	private String cibmtr_store_auto;
	private String cell_type;
	private String type_spec;
	private String ebmt_cic;
	private String auto_yn;
	private String allo_u_yn;
	private String allo_r_yn;
	private String syn_twin_yn;
	private String bm_yn;
	private String pbsc_yn;
	private String cord_yn;
	private String multi_cord_yn;
	private String other_prod_yn;
	private String other_prod_spec;
	private String ct_indication_spec;
	private String frst_ct_app_yn;
	private String nmdp_na_yn;
	private String ebmt_na_yn;
	private String team_na_yn;
	private String is_hsct_first_yn;
	private Timestamp enrollment_date;
	private String ssn_yna;
	private String nmdp_rid_yn;
	private String ebmt_id_yn;
	private String ebmt_cic_yn;
	private String cibmtr_iubmid_yn;
	private String cibmtr_team_yn;
	private String institution_specific_subject_id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PatientEntity that = (PatientEntity) o;
		return Objects.equals(unique_patient_id, that.unique_patient_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(unique_patient_id);
	}
}
	