/**
 *
 */
package com.nmdp.cridservice.util;

import com.nmdp.cridservice.constants.PatientErrorMessages;
import com.nmdp.cridservice.dto.Extension;
import com.nmdp.cridservice.dto.PatientRequestInfo;
import com.nmdp.cridservice.dto.PatientResponseInfo;
import com.nmdp.cridservice.entity.PatientEntity;
import com.nmdp.cridservice.service.PatientSearchService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mdudyala
 */
public class CridserviceUtils {

    private static String FIRSTNAME_REGEX_PATTERN = "[A-Za-z]*";
    private static String LASTNAME_REGEX_PATTERN = "[A-Za-z]*";
    private static String BIRTHDATE_REGEX_PATTERN = "\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d";
    private static String GENDER_REGEX_PATTERN = "[mMfF]";

    /**
     * @param jsonString
     */
    public static boolean isValidJson(String jsonString) {
        try {
            new JSONObject(jsonString);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    /**
     * @param patientRequestInfo
     * @return
     */
    public static String checkForMissingValues(PatientRequestInfo patientRequestInfo) {

        if (StringUtils.isEmpty(patientRequestInfo.getFirstName())) {
            return PatientErrorMessages.MISSING_FIRST_NAME;
        }

        if (StringUtils.isEmpty(patientRequestInfo.getLastName())) {
            return PatientErrorMessages.MISSING_LAST_NAME;
        }

        if (StringUtils.isEmpty(patientRequestInfo.getBirthDate())) {
            return PatientErrorMessages.MISSING_BIRTH_DATE;
        }

        if (StringUtils.isEmpty(patientRequestInfo.getGender())) {
            return PatientErrorMessages.MISSING_GENDER;
        }

        return null;
    }

    /**
     * @param patientRequestInfo
     * @return
     */
    public static String checkValidPattern(PatientRequestInfo patientRequestInfo) {

        Pattern pattern = null;
        Matcher matcher = null;
        boolean matches;

        // check for firstname
        pattern = Pattern.compile(FIRSTNAME_REGEX_PATTERN);
        matcher = pattern.matcher(patientRequestInfo.getFirstName());
        matches = matcher.matches();
        if (!matches) {
            return String.format("firstName %s is not valid", patientRequestInfo.getFirstName());
        }

        // check for lastname
        pattern = Pattern.compile(LASTNAME_REGEX_PATTERN);
        matcher = pattern.matcher(patientRequestInfo.getLastName());
        matches = matcher.matches();
        if (!matches) {
            return String.format("lastName %s is not valid", patientRequestInfo.getLastName());
        }

        // check for birthdate
        pattern = Pattern.compile(BIRTHDATE_REGEX_PATTERN);
        matcher = pattern.matcher(patientRequestInfo.getBirthDate());
        matches = matcher.matches();
        if (!matches) {
            return String.format("birthDate %s is not valid", patientRequestInfo.getBirthDate());
        }

        // check for gender
        pattern = Pattern.compile(GENDER_REGEX_PATTERN);
        matcher = pattern.matcher(patientRequestInfo.getGender());
        matches = matcher.matches();
        if (!matches) {
            return String.format("gender %s is not valid", patientRequestInfo.getGender());
        }

        return null;
    }

    /**
     * @param dateInString
     * @return
     */
    public static Timestamp convertStringToTimeStamp(String dateInString) {

        if ( dateInString == null ) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = formatter.parse(dateInString);
            return new Timestamp(date.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param dateInTimeStamp
     * @return
     */
    private static String convertTimeStampToString(Timestamp dateInTimeStamp ) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format( new Date( dateInTimeStamp.getTime() ));
    }


    /**
     *
     * @param key
     * @param wrapper
     * @param responseInfoMatches
     */
    public static void convertToMatchResponse(String key, PatientSearchService.Wrapper wrapper, List<PatientResponseInfo.Match> responseInfoMatches) {

        List<PatientEntity> dbPatientEntities = wrapper.getPatientEntities();

        if ( !CollectionUtils.isEmpty( dbPatientEntities ) ) {

            for (PatientEntity entity : dbPatientEntities) {
                PatientResponseInfo.Match match = new PatientResponseInfo.Match();
                match.setMatchedCriteria( getMatchedCriteria( wrapper.getInputCriteria(), entity ) );
                match.setMatchType(key);
                match.setCrid(entity.getUnique_patient_id());
                match.setExtensions(buildExtensions(entity));
                responseInfoMatches.add(match);
            }
        }
    }

    /**
     *
     * @param inputCriteria
     * @param patientEntity
     * @return
     */
    private static String[] getMatchedCriteria( PatientRequestInfo inputCriteria, PatientEntity patientEntity ) {

        List<String> matchedCriteria = new ArrayList<String>();

        if (patientEntity.getFirst_name() != null && patientEntity.getFirst_name().equalsIgnoreCase(inputCriteria.getFirstName())) {
            matchedCriteria.add( "firstName" );
        }

        if (patientEntity.getLast_name() != null && patientEntity.getLast_name().equalsIgnoreCase(inputCriteria.getLastName())) {
            matchedCriteria.add( "lastName" );
        }

        if (patientEntity.getBirthday() != null && convertTimeStampToString(patientEntity.getBirthday()).equalsIgnoreCase(inputCriteria.getBirthDate())) {
            matchedCriteria.add( "birthDay" );
        }

        if (patientEntity.getGender() != null && patientEntity.getGender().equalsIgnoreCase(inputCriteria.getGender())) {
            matchedCriteria.add( "gender" );
        }

        if (patientEntity.getMothers_maiden_name() != null && patientEntity.getMothers_maiden_name().equalsIgnoreCase(inputCriteria.getMothersMaidenName())) {
            matchedCriteria.add( "mothersMaidenName" );
        }

        if (patientEntity.getSsn() != null && patientEntity.getSsn().equalsIgnoreCase(inputCriteria.getSsn())) {
            matchedCriteria.add( "ssn" );
        }

        if ( (patientEntity.getNmdp_rid() != null && inputCriteria.getNmdpRid() != null && patientEntity.getNmdp_rid().toString().equalsIgnoreCase(inputCriteria.getNmdpRid().toString()))
                || (patientEntity.getCibmtr_iubmid() != null && patientEntity.getCibmtr_iubmid().equalsIgnoreCase(inputCriteria.getCibmtrIubmid()))
                || (patientEntity.getCibmtr_team() != null && inputCriteria.getCibmtrTeam() != null && patientEntity.getCibmtr_team().toString().equalsIgnoreCase(inputCriteria.getCibmtrTeam().toString()))
                || (patientEntity.getEbmt_id() != null && patientEntity.getEbmt_id().equalsIgnoreCase(inputCriteria.getEbmtId()))
                || (patientEntity.getEbmt_cic() != null && patientEntity.getEbmt_cic().equalsIgnoreCase(inputCriteria.getEbmtCic()))
        )
        {
            matchedCriteria.add( "registry" );
        }

        return matchedCriteria.toArray(new String[matchedCriteria.size()]);
    }


    /**
     * @param patientEntity
     * @return
     */
    static List<Extension> buildExtensions(PatientEntity patientEntity) {

            List<Extension> extensionList = new ArrayList<>();

            if ( patientEntity.getEbmt_id() != null && patientEntity.getEbmt_cic() != null ) {

                Extension extension = new Extension();

                extension.setRegistry("Ebmt");
                extension.setRegistryID(patientEntity.getEbmt_id());
                extension.setRegistryValue(patientEntity.getEbmt_cic());
                extensionList.add(extension);
            }


            return extensionList;
        }
}