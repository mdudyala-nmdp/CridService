/**
 *
 */
package com.nmdp.cridservice.repository;

import com.nmdp.cridservice.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author mdudyala
 *
 */
@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {

	// ----------------------------------Perfect Match Patterns -----------------------------------------------------------------------------

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.ssn = ?2 and tup.birthday = ?3 and tup.gender = ?4 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnSSNDobGender(
			@Param("ccn") Integer ccn,
			@Param("ssn") String ssn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.birthday = ?2 and tup.first_name = ?3 and tup.last_name = ?4 and tup.gender = ?5 and tup.mothers_maiden_name = ?6 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnDobFirstNameLastNameGenderMaidenName(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("gender") String gender,
			@Param("mothersMaidenName") String mothersMaidenName);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.nmdp_rid = ?4 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnSSNDobGenderNmdprid(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("nmdprid") Integer nmdprid);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.cibmtr_iubmid = ?4 and tup.cibmtr_team = ?5 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnDobGenderIubmidTeam(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("cibmtrIubmid") String cibmtrIubmid,
			@Param("cibmtrTeam") Integer cibmtrTeam);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.ebmt_id = ?4 and tup.ebmt_cic = ?5 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnDobGenderEbmt(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("ebmtId") String ebmtId,
			@Param("ebmtCic") String ebmtCic);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.first_name = ?4 and tup.last_name = ?5 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnDobGenderFirstNameLastName(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName);


	// -------------------------------Fuzzy match Patterns --------------------------------------------------------

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and tup.ssn = ?2 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnSSN(
			@Param("ccn") Integer ccn,
			@Param("ssn") String ssn);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number = ?1 and (select \n" +
			" case when tup.first_name = ?2 then 1 else 0 end + \n" +
			" case when tup.last_name = ?3 then 1 else 0 end + \n" +
			" case when tup.birthday = ?4 then 1 else 0 end + \n" +
			" case when tup.gender = ?5 then 1 else 0 end + \n" +
			" case when tup.mothers_maiden_name = ?6 then 1 else 0 end \n" +
			") >= 4 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnFirstNameLastNameDobGenderMaidenName(
			@Param("ccn") Integer ccn,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("mothersMaidenName") String mothersMaidenName);

	@Query(value = "select tup.* from t_unique_patient tup \n" +
			"where tup.cibmtr_center_number = ?1 and tup.birthday = ?2 and tup.gender = ?3 \n" +
			"and (select \n" +
			"case when tup.first_name = ?4 then 1 else 0 end + \n" +
			"case when tup.last_name = ?5 then 1 else 0 end + \n" +
			"case when tup.mothers_maiden_name = ?6 then 1 else 0 end \n" +
			") >= 1",
			nativeQuery = true)
	List<PatientEntity> findByCcnDobGenderOneOfFirstNameLastMaidenNames(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("mothersMaidenName") String mothersMaidenName);

	@Query(value = "select tup.* from t_unique_patient tup \n" +
			"where tup.cibmtr_center_number = ?1 and tup.nmdp_rid = ?2 \n" +
			"and (select \n" +
			"case when tup.birthday = ?3 then 1 else 0 end + \n" +
			"case when tup.gender = ?4 then 1 else 0 end \n" +
			") >= 1 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnRidOneOfDobGender(
			@Param("ccn") Integer ccn,
			@Param("nmdpRid") Integer nmdpRid,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender);

	@Query(value = "select tup.* from t_unique_patient tup \n" +
			"where tup.cibmtr_center_number = ?1 and tup.cibmtr_iubmid = ?2 and tup.cibmtr_team = ?3 \n" +
			"and (select \n" +
			"case when tup.birthday = ?4 then 1 else 0 end + \n" +
			"case when tup.gender = ?5 then 1 else 0 end \n" +
			") >= 1 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnCibmtrOneOfDobGender(
			@Param("ccn") Integer ccn,
			@Param("cibmtrIubmid") String cibmtrIubmid,
			@Param("cibmtrTeam") Integer cibmtrTeam,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender);

	@Query(value = "select tup.* from t_unique_patient tup \n" +
			"where tup.cibmtr_center_number = ?1 and tup.ebmt_id = ?2 and tup.ebmt_cic = ?3 \n" +
			"and (select \n" +
			"case when tup.birthday = ?4 then 1 else 0 end + \n" +
			"case when tup.gender = ?5 then 1 else 0 end \n" +
			") >= 1 ",
			nativeQuery = true)
	List<PatientEntity> findByCcnEbmtOneOfDobGender(
			@Param("ccn") Integer ccn,
			@Param("ebmtId") String ebmtId,
			@Param("ebmtCic") String ebmtCic,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender);


	//-----------------------------------Hidden Match Patterns----------------------------------------------------------------------------------
	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number != ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.first_name = ?4 and tup.last_name = ?5 ",
			nativeQuery = true)
	List<PatientEntity> findByNotCcnDobGenderFirstNameLastName(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number != ?1 and tup.ssn = ?2 and tup.birthday = ?3 and tup.gender = ?4 ",
			nativeQuery = true)
	List<PatientEntity> findByNotCcnSSNDobGender(
			@Param("ccn") Integer ccn,
			@Param("ssn") String ssn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number != ?1 and tup.birthday = ?2 and tup.first_name = ?3 and tup.last_name = ?4 and tup.gender = ?5 and tup.mothers_maiden_name = ?6 ",
			nativeQuery = true)
	List<PatientEntity> findByNotCcnDobFirstNameLastNameGenderMaidenName(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("firstName") String firstName,
			@Param("lastName") String lastName,
			@Param("gender") String gender,
			@Param("mothersMaidenName") String mothersMaidenName);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number != ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.nmdp_rid = ?4 ",
			nativeQuery = true)
	List<PatientEntity> findByNotCcnSSNDobGenderNmdprid(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("nmdprid") Integer nmdprid);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number! = ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.cibmtr_iubmid = ?4 and tup.cibmtr_team = ?5 ",
			nativeQuery = true)
	List<PatientEntity> findByNotCcnDobGenderIubmidTeam(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("cibmtrIubmid") String cibmtrIubmid,
			@Param("cibmtrTeam") Integer cibmtrTeam);

	@Query(value = "select tup.* from t_unique_patient tup where tup.cibmtr_center_number! = ?1 and tup.birthday = ?2 and tup.gender = ?3 and tup.ebmt_id = ?4 and tup.ebmt_cic = ?5 ",
			nativeQuery = true)
	List<PatientEntity> findByNotCcnDobGenderEbmt(
			@Param("ccn") Integer ccn,
			@Param("birthDate") Timestamp birthDate,
			@Param("gender") String gender,
			@Param("ebmtId") String ebmtId,
			@Param("ebmtCic") String ebmtCic);



	@Query(value="select max(unique_patient_id) from t_unique_patient", nativeQuery = true)
	Long findMaxUniquePatientId();

	@Modifying
	@Query( value = "insert into t_unique_patient ( unique_patient_id, cibmtr_center_number, active, \n" +
			"birthday, gender, first_name, last_name, ssn, mothers_maiden_name, nmdp_rid, cibmtr_iubmid, cibmtr_team, ebmt_id, ebmt_cic, lst_updt_dte, \n" +
			"lst_updt_id ) values ( :uniquePatientId , :ccn, :active, :birthday, :gender, :firstName, :lastName, :ssn, :mothersMaidenName, :nmdpRid, :cibmtrIubmid, \n" +
			":cibmtrTeam, :ebmtId, :ebmtCic, :lastUpdatedTime, :lastUpdatedId )" , nativeQuery = true)


	@Transactional
	void insertNewCridPatientInfo(@Param("uniquePatientId") Long uniquePatientId,
								  @Param("ccn") Integer ccn,
								  @Param("active") Boolean active,
								  @Param("birthday") Timestamp birthday,
								  @Param("gender") String gender,
								  @Param("firstName") String firstName,
								  @Param("lastName") String lastName,
								  @Param("ssn") String ssn,
								  @Param("mothersMaidenName") String mothersMaidenName,
								  @Param("nmdpRid") Integer nmdpRid,
								  @Param("cibmtrIubmid") String cibmtrIubmid,
								  @Param("cibmtrTeam") Integer cibmtrTeam,
								  @Param("ebmtId") String ebmtId,
								  @Param("ebmtCic") String ebmtCic, @Param("lastUpdatedTime") Timestamp lastUpdatedTime,
								  @Param("lastUpdatedId") String lastUpdatedId
	);

	@Modifying
	@Query(value = "update t_unique_patient set first_name = :firstName, \n" +
			"last_name = :lastName, birthday = :birthday, gender= :gender, ssn = :ssn,  \n" +
			"mothers_maiden_name = :mothersMaidenName, nmdp_rid = :nmdpRid, cibmtr_iubmid = :cibmtrIubmid, \n" +
			"cibmtr_team = :cibmtrTeam, ebmt_id = :ebmtId, ebmt_cic = :ebmtCic where unique_patient_id = :uniqueIdentifier", nativeQuery = true)
	@Transactional
	int updateCridPatientInfo(@Param("uniqueIdentifier") Long uniqueIdentifier,
							  @Param("firstName") String firstName,
							  @Param("lastName") String lastName,
							  @Param("birthday") Timestamp birthday,
							  @Param("gender") String gender,
							  @Param("ssn") String ssn,
							  @Param("mothersMaidenName") String mothersMaidenName,
							  @Param("nmdpRid") Integer nmdpRid,
							  @Param("cibmtrIubmid") String cibmtrIubmid,
							  @Param("cibmtrTeam") Integer cibmtrTeam,
							  @Param("ebmtId") String ebmtId,
							  @Param("ebmtCic") String ebmtCic

	);

}