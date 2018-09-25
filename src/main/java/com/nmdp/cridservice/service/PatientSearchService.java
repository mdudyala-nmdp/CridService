package com.nmdp.cridservice.service;


import com.nmdp.cridservice.dto.PatientRequest;
import com.nmdp.cridservice.dto.PatientRequestInfo;
import com.nmdp.cridservice.dto.PatientResponseInfo;
import com.nmdp.cridservice.entity.PatientEntity;
import com.nmdp.cridservice.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.nmdp.cridservice.util.CridserviceUtils.convertStringToTimeStamp;
import static com.nmdp.cridservice.util.CridserviceUtils.convertToMatchResponse;

@Component
public class PatientSearchService {

    Logger logger = Logger.getLogger(PatientSearchService.class);

    @Autowired
    private PatientRepository patientRepository;

    /**
     * @param patientRequest
     * @return
     */
    public PatientResponseInfo search(PatientRequest patientRequest) {

        Map<String, Object> searchResults = new HashMap<>();

        Map<String, Wrapper> pMatchResults = new HashMap<>();
        Map<String, Wrapper> fMatchResults = new HashMap<>();
        Map<String, Wrapper> hMatchResults = new HashMap<>();

        Integer ccn = patientRequest.getCcn();
        PatientRequestInfo patientRequestInfo = patientRequest.getPatient();

        //--------------Perfect Matches ---------------------

        // pMatch(0) ssn, dob and gender
        if (isExists(patientRequestInfo.getSsn()) && isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender())) {

            List<PatientEntity> pm1Results = patientRepository.findByCcnSSNDobGender(ccn, patientRequestInfo.getSsn(), convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender());

            if (!CollectionUtils.isEmpty(pm1Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setSsn(patientRequestInfo.getSsn());
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());

                pMatchResults.put("PM1", new Wrapper(inputCriteria, pm1Results));
            }
        }

        //  pMatch(1) dob, firstName, lastName, gender, maiden name
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getFirstName()) && isExists(patientRequestInfo.getLastName())
                && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getMothersMaidenName())) {

            List<PatientEntity> pm2Results = patientRepository.findByCcnDobFirstNameLastNameGenderMaidenName(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getFirstName(),
                    patientRequestInfo.getLastName(),patientRequestInfo.getGender(), patientRequestInfo.getMothersMaidenName());

            if (!CollectionUtils.isEmpty(pm2Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setFirstName(patientRequestInfo.getFirstName());
                inputCriteria.setLastName(patientRequestInfo.getLastName());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setMothersMaidenName(patientRequestInfo.getMothersMaidenName());

                pMatchResults.put("PM2", new Wrapper(inputCriteria, pm2Results));
            }
        }


        // pMatch(2) dob, gender and rid
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && patientRequestInfo.getNmdpRid() != null) {

            List<PatientEntity> pm3Results = patientRepository.findByCcnSSNDobGenderNmdprid(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getNmdpRid());

            if (!CollectionUtils.isEmpty(pm3Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setNmdpRid(patientRequestInfo.getNmdpRid());

                pMatchResults.put("PM3", new Wrapper(inputCriteria, pm3Results));
            }
        }

        //  dob, gender, cibmtr iubmid + Team
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getCibmtrIubmid()) && patientRequestInfo.getCibmtrTeam() != null) {

            List<PatientEntity> pm4Results = patientRepository.findByCcnDobGenderIubmidTeam(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getCibmtrIubmid(), patientRequestInfo.getCibmtrTeam());

            if (!CollectionUtils.isEmpty(pm4Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setCibmtrIubmid(patientRequestInfo.getCibmtrIubmid());
                inputCriteria.setCibmtrTeam(patientRequestInfo.getCibmtrTeam());

                pMatchResults.put("PM4", new Wrapper(inputCriteria, pm4Results));
            }
        }

        //  dob, gender, ebmt id + cic
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getEbmtId()) && isExists(patientRequestInfo.getEbmtCic())) {

            List<PatientEntity> pm5Results = patientRepository.findByCcnDobGenderEbmt(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getEbmtId(), patientRequestInfo.getEbmtCic());

            if (!CollectionUtils.isEmpty(pm5Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setEbmtId(patientRequestInfo.getEbmtId());
                inputCriteria.setEbmtCic(patientRequestInfo.getEbmtCic());

                pMatchResults.put("PM5", new Wrapper(inputCriteria, pm5Results));
            }
        }

        //  dob, gender, firstName and lastName
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getFirstName()) && isExists(patientRequestInfo.getLastName())) {

            List<PatientEntity> pm10Results = patientRepository.findByCcnDobGenderFirstNameLastName(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getFirstName(), patientRequestInfo.getLastName());

            if (!CollectionUtils.isEmpty(pm10Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setFirstName(patientRequestInfo.getFirstName());
                inputCriteria.setLastName(patientRequestInfo.getLastName());

                pMatchResults.put("PM10", new Wrapper(inputCriteria, pm10Results));
            }
        }

        //----------------Fuzzy Match ------------------

        //  ssn
        if (isExists(patientRequestInfo.getSsn())) {

            List<PatientEntity> fm1Results = patientRepository.findByCcnSSN(ccn, patientRequestInfo.getSsn());

            if (!CollectionUtils.isEmpty(fm1Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setSsn(patientRequestInfo.getSsn());

                fMatchResults.put("FM1", new Wrapper(inputCriteria, fm1Results));
            }
        }

        //  firstName, lastName, Dob, gender, maiden name
        if (countByValue(patientRequestInfo.getFirstName(), patientRequestInfo.getLastName(), patientRequestInfo.getBirthDate(), patientRequestInfo.getGender(), patientRequestInfo.getMothersMaidenName()) >= 4) {

            List<PatientEntity> fm2Results = patientRepository.findByCcnFirstNameLastNameDobGenderMaidenName(ccn, patientRequestInfo.getFirstName(), patientRequestInfo.getLastName(), convertStringToTimeStamp(patientRequestInfo.getBirthDate()),
                    patientRequestInfo.getGender(), patientRequestInfo.getMothersMaidenName());

            if (!CollectionUtils.isEmpty(fm2Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setFirstName(patientRequestInfo.getFirstName());
                inputCriteria.setLastName(patientRequestInfo.getLastName());
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setMothersMaidenName(patientRequestInfo.getMothersMaidenName());

                fMatchResults.put("FM2", new Wrapper(inputCriteria, fm2Results));
            }
        }

        // dob, gender and either (FirstName, LastName, MothersMaidenName)
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender())
                && countByValue(patientRequestInfo.getFirstName(), patientRequestInfo.getLastName(), patientRequestInfo.getMothersMaidenName()) >= 1) {

            List<PatientEntity> fm3Results = patientRepository.findByCcnDobGenderOneOfFirstNameLastMaidenNames(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getFirstName(), patientRequestInfo.getLastName(), patientRequestInfo.getMothersMaidenName());

            if (!CollectionUtils.isEmpty(fm3Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setFirstName(patientRequestInfo.getFirstName());
                inputCriteria.setLastName(patientRequestInfo.getLastName());
                inputCriteria.setMothersMaidenName(patientRequestInfo.getMothersMaidenName());

                fMatchResults.put("FM3", new Wrapper(inputCriteria, fm3Results));
            }
        }

        // rid and either( dob, gender )
        if (patientRequestInfo.getNmdpRid() != null && countByValue(patientRequestInfo.getBirthDate(), patientRequestInfo.getGender()) >= 1) {

            List<PatientEntity> fm4Results = patientRepository.findByCcnRidOneOfDobGender(ccn, patientRequestInfo.getNmdpRid(), convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender());

            if (!CollectionUtils.isEmpty(fm4Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setNmdpRid(patientRequestInfo.getNmdpRid());
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());

                fMatchResults.put("FM4", new Wrapper(inputCriteria, fm4Results));
            }
        }

        // cibmtr team and either( dob, gender )
        if (isExists(patientRequestInfo.getCibmtrIubmid()) && patientRequestInfo.getCibmtrTeam() != null && countByValue(patientRequestInfo.getBirthDate(), patientRequestInfo.getGender()) >= 1) {

            List<PatientEntity> fm5Results = patientRepository.findByCcnCibmtrOneOfDobGender(ccn, patientRequestInfo.getCibmtrIubmid(), patientRequestInfo.getCibmtrTeam(), convertStringToTimeStamp(patientRequestInfo.getBirthDate()),
                    patientRequestInfo.getGender());

            if (!CollectionUtils.isEmpty(fm5Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setCibmtrIubmid(patientRequestInfo.getCibmtrIubmid());
                inputCriteria.setCibmtrTeam(patientRequestInfo.getCibmtrTeam());
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());

                fMatchResults.put("FM5", new Wrapper(inputCriteria, fm5Results));
            }
        }

        // ebmt cic and either ( dob, gender )
        if (isExists(patientRequestInfo.getEbmtId()) && patientRequestInfo.getEbmtCic() != null && countByValue(patientRequestInfo.getBirthDate(), patientRequestInfo.getGender()) >= 1) {

            List<PatientEntity> fm6Results = patientRepository.findByCcnEbmtOneOfDobGender(ccn, patientRequestInfo.getEbmtId(), patientRequestInfo.getEbmtCic(), convertStringToTimeStamp(patientRequestInfo.getBirthDate()),
                    patientRequestInfo.getGender());

            if (!CollectionUtils.isEmpty(fm6Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setEbmtId(patientRequestInfo.getEbmtId());
                inputCriteria.setEbmtCic(patientRequestInfo.getEbmtCic());
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());

                fMatchResults.put("FM6", new Wrapper(inputCriteria, fm6Results));
            }
        }

        //-----------------Hidden Matches--------------------------------------

        // hMatch(1) ,!ccn, ssn, dob and gender
        if (isExists(patientRequestInfo.getSsn()) && isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender())) {

            List<PatientEntity> hm1Results = patientRepository.findByNotCcnSSNDobGender(ccn, patientRequestInfo.getSsn(), convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender());

            if (!CollectionUtils.isEmpty(hm1Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setSsn(patientRequestInfo.getSsn());
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());

                hMatchResults.put("HM1", new Wrapper(inputCriteria, hm1Results));
            }
        }

        //  hMatch(2) , !ccn, dob, firstName, lastName, gender, maiden name
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getFirstName()) && isExists(patientRequestInfo.getLastName())
                && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getMothersMaidenName())) {

            List<PatientEntity> hm2Results = patientRepository.findByNotCcnDobFirstNameLastNameGenderMaidenName(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getFirstName(),
                    patientRequestInfo.getLastName(),patientRequestInfo.getGender(), patientRequestInfo.getMothersMaidenName());

            if (!CollectionUtils.isEmpty(hm2Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setFirstName(patientRequestInfo.getFirstName());
                inputCriteria.setLastName(patientRequestInfo.getLastName());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setMothersMaidenName(patientRequestInfo.getMothersMaidenName());

                hMatchResults.put("HM2", new Wrapper(inputCriteria, hm2Results));
            }
        }


        // hMatch(3) ,!ccn , dob, gender and rid
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && patientRequestInfo.getNmdpRid() != null) {

            List<PatientEntity> hm3Results = patientRepository.findByNotCcnSSNDobGenderNmdprid(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getNmdpRid());

            if (!CollectionUtils.isEmpty(hm3Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setNmdpRid(patientRequestInfo.getNmdpRid());

                hMatchResults.put("HM3", new Wrapper(inputCriteria, hm3Results));
            }
        }

        // hMatch(4)  !ccn, dob, gender, cibmtr iubmid + Team
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getCibmtrIubmid()) && patientRequestInfo.getCibmtrTeam() != null) {

            List<PatientEntity> hm4Results = patientRepository.findByNotCcnDobGenderIubmidTeam(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getCibmtrIubmid(), patientRequestInfo.getCibmtrTeam());

            if (!CollectionUtils.isEmpty(hm4Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setCibmtrIubmid(patientRequestInfo.getCibmtrIubmid());
                inputCriteria.setCibmtrTeam(patientRequestInfo.getCibmtrTeam());

                hMatchResults.put("HM4", new Wrapper(inputCriteria, hm4Results));
            }
        }

        // hMatch(5)  !ccn , dob, gender, ebmt id + cic
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getEbmtId()) && isExists(patientRequestInfo.getEbmtCic())) {

            List<PatientEntity> hm5Results = patientRepository.findByNotCcnDobGenderEbmt(ccn, convertStringToTimeStamp(patientRequestInfo.getBirthDate()), patientRequestInfo.getGender(),
                    patientRequestInfo.getEbmtId(), patientRequestInfo.getEbmtCic());

            if (!CollectionUtils.isEmpty(hm5Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setEbmtId(patientRequestInfo.getEbmtId());
                inputCriteria.setEbmtCic(patientRequestInfo.getEbmtCic());

                hMatchResults.put("HM5", new Wrapper(inputCriteria, hm5Results));
            }
        }


        // hMatch(10)  !ccn , dob, gender, firstName and lastName
        if (isExists(patientRequestInfo.getBirthDate()) && isExists(patientRequestInfo.getGender()) && isExists(patientRequestInfo.getFirstName()) && isExists(patientRequestInfo.getLastName())) {

            List<PatientEntity> hm10Results = patientRepository.findByNotCcnDobGenderFirstNameLastName(ccn,
                    convertStringToTimeStamp(patientRequestInfo.getBirthDate()),
                    patientRequestInfo.getGender(),
                    patientRequestInfo.getFirstName(),
                    patientRequestInfo.getLastName());

            if (!CollectionUtils.isEmpty(hm10Results)) {

                PatientRequestInfo inputCriteria = new PatientRequestInfo();
                inputCriteria.setBirthDate(patientRequestInfo.getBirthDate());
                inputCriteria.setGender(patientRequestInfo.getGender());
                inputCriteria.setFirstName(patientRequestInfo.getFirstName());
                inputCriteria.setLastName(patientRequestInfo.getLastName());

                hMatchResults.put("HM10", new Wrapper(inputCriteria, hm10Results));
            }

        }

        searchResults.put("PM", pMatchResults);
        searchResults.put("FM", fMatchResults);
        searchResults.put("HM", hMatchResults);

        PatientResponseInfo responseInfo = buildResponse(searchResults);

        return responseInfo;
    }

    /**
     *
     * @param responseInfo
     * @return
     */
    public PatientResponseInfo create(PatientRequest patientRequest, PatientResponseInfo responseInfo) {

        // if there is perfect match then return the response as is
        if ( !CollectionUtils.isEmpty( responseInfo.getPerfectMatch() )) {
            return assignCridWithPerfectMatch( patientRequest, responseInfo );
            //return responseInfo;
        }

        // otherwise create a new crid and assign it
        return assignCridWithPerfectMatch( patientRequest, responseInfo );
    }

    /**
     *
     * @param patientRequest
     * @param responseInfo
     * @return
     */
    public PatientResponseInfo update( PatientRequest patientRequest, PatientResponseInfo responseInfo ) {

        String requestFirstName = null;
        String requestLastName = null;
        Timestamp requestBirthday = null;
        String requestGender = null;
        String requestSsn = null;
        String requestMothersMaidenName = null;
        Integer requestNmdpRid = null;
        String requestCibmtrIubmid = null;
        Integer requestCibmtrTeam = null;
        String requestEbmtId = null;
        String requestEbmtCic = null;

        Long uniqueIdentifier = null;
        String dbFirstName = null;
        String dbLastName = null;
        Timestamp dbBirthday = null;
        String dbGender = null;
        String dbSsn = null;
        String dbMothersMaidenName = null;
        Integer dbNmdpRid = null;
        String dbCibmtrIubmid = null;
        Integer dbCibmtrTeam = null;
        String dbEbmtId = null;
        String dbEbmtCic = null;

        if ( patientRequest.getPatient() != null ) {
            requestFirstName = patientRequest.getPatient().getFirstName();
            requestLastName =  patientRequest.getPatient().getLastName();
            requestBirthday = convertStringToTimeStamp(patientRequest.getPatient().getBirthDate());
            requestGender = patientRequest.getPatient().getGender();
            requestSsn = patientRequest.getPatient().getSsn();
            requestMothersMaidenName = patientRequest.getPatient().getMothersMaidenName();
            requestNmdpRid = patientRequest.getPatient().getNmdpRid();
            requestCibmtrIubmid = patientRequest.getPatient().getCibmtrIubmid();
            requestCibmtrTeam = patientRequest.getPatient().getCibmtrTeam();
            requestEbmtId = patientRequest.getPatient().getEbmtId();
            requestEbmtCic = patientRequest.getPatient().getEbmtCic();
        }

        if ( responseInfo.getPerfectMatch() != null && responseInfo.getPerfectMatch().size() > 0 ) {
            dbFirstName = responseInfo.getPerfectMatch().get(0).getFirstName();
            dbLastName = responseInfo.getPerfectMatch().get(0).getLastName();
            dbBirthday = convertStringToTimeStamp(responseInfo.getPerfectMatch().get(0).getBirthDate());
            dbGender = responseInfo.getPerfectMatch().get(0).getGender();
            dbSsn = responseInfo.getPerfectMatch().get(0).getSsn();
            dbMothersMaidenName = responseInfo.getPerfectMatch().get(0).getMothersMaidenName();
            dbNmdpRid = responseInfo.getPerfectMatch().get(0).getNmdpRid();
            dbCibmtrIubmid = responseInfo.getPerfectMatch().get(0).getCibmtrIubmid();
            dbCibmtrTeam = responseInfo.getPerfectMatch().get(0).getCibmtrTeam();
            dbEbmtId = responseInfo.getPerfectMatch().get(0).getEbmtId();
            dbEbmtCic = responseInfo.getPerfectMatch().get(0).getEbmtCic();
            uniqueIdentifier = responseInfo.getPerfectMatch().get(0).getCrid();
                   }


        patientRepository.updateCridPatientInfo(
                uniqueIdentifier,
                (requestFirstName != null && (requestFirstName!=dbFirstName)) ? requestFirstName : dbFirstName,
                (requestLastName != null && (requestLastName!=dbLastName)) ? requestLastName : dbLastName,
                (requestBirthday!= null && (requestBirthday!=dbBirthday)) ? requestBirthday : dbBirthday,
                (requestGender != null && (requestGender!=dbGender)) ? requestGender : dbGender,
                (requestSsn != null && (requestSsn!=dbSsn)) ? requestSsn : dbSsn,
                (requestMothersMaidenName != null && (requestMothersMaidenName!=dbMothersMaidenName)) ? requestMothersMaidenName : dbMothersMaidenName,
                (requestNmdpRid != null && (requestNmdpRid!=dbNmdpRid)) ? requestNmdpRid : dbNmdpRid,
                (requestCibmtrIubmid != null && (requestCibmtrIubmid!=dbCibmtrIubmid)) ?  requestCibmtrIubmid : dbCibmtrIubmid ,
                (requestCibmtrTeam != null && (requestCibmtrTeam!=dbCibmtrTeam)) ? requestCibmtrTeam : dbCibmtrTeam,
                (requestEbmtId != null && (requestEbmtId!=dbEbmtId)) ? requestEbmtId : dbEbmtId,
                (requestEbmtCic != null && (requestEbmtCic!=dbEbmtCic)) ? requestEbmtCic : dbEbmtCic

        );

        return responseInfo;

    }

    /**
     *
     * @param responseInfo
     * @return
     */
    private PatientResponseInfo assignCridWithPerfectMatch ( PatientRequest patientRequest,  PatientResponseInfo responseInfo ) {

        Long newCrid = patientRepository.findMaxUniquePatientId() + 1;

        Date now = new java.util.Date();
        Timestamp current = new java.sql.Timestamp(now.getTime());

        PatientResponseInfo.Match responseEntry = new PatientResponseInfo.Match();
        //TODO gender check for birthSex

        PatientRequestInfo requestInfo = patientRequest.getPatient();
        Integer ccn = patientRequest.getCcn();

        if ( ccn != null ) {

            patientRepository.insertNewCridPatientInfo( newCrid,
                    ccn,
                    true,
                    convertStringToTimeStamp(requestInfo.getBirthDate()),
                    requestInfo.getGender(),
                    requestInfo.getFirstName(),
                    requestInfo.getLastName(),
                    requestInfo.getSsn(),
                    requestInfo.getMothersMaidenName(),
                    requestInfo.getNmdpRid(),
                    requestInfo.getCibmtrIubmid(),
                    requestInfo.getCibmtrTeam(),
                    requestInfo.getEbmtId(),
                    requestInfo.getEbmtCic(),
                    current,
                    "FHIR"
            );

            // build the perfect match here
            // List<PatientEntity> patientEntities = new ArrayList<>();
            //List<PatientResponseInfo.Match> perfectMatches = new ArrayList<>();

            // build patient entities
            // patientEntities.add(buildPatientEntityFromRequestInfo(newCrid, ccn, requestInfo));

            // create the perfect match
            // Wrapper wrapper = new Wrapper(requestInfo, patientEntities);
            //convertToMatchResponse(buildKey(requestInfo), wrapper, perfectMatches);

            responseInfo.setCrid( newCrid );

            //responseInfo.setPerfectMatch(perfectMatches);
        }

        return responseInfo;
    }

    /**
     *
     * @param requestInfo
     * @return
     */
    private String buildKey(PatientRequestInfo requestInfo) {

        if (isExists(requestInfo.getSsn()) && isExists(requestInfo.getBirthDate()) && isExists(requestInfo.getGender())) {
            return "PM1";
        }

        if (isExists(requestInfo.getBirthDate()) && isExists(requestInfo.getFirstName()) && isExists(requestInfo.getLastName())
                && isExists(requestInfo.getMothersMaidenName())) {
            return "PM2";
        }

        if (isExists(requestInfo.getBirthDate()) && isExists(requestInfo.getGender()) && requestInfo.getNmdpRid() != null) {
            return "PM3";
        }

        if (isExists(requestInfo.getBirthDate()) && isExists(requestInfo.getGender()) && isExists(requestInfo.getCibmtrIubmid()) && requestInfo.getCibmtrTeam() != null) {
            return "PM4";
        }

        if (isExists(requestInfo.getBirthDate()) && isExists(requestInfo.getGender()) && isExists(requestInfo.getEbmtId()) && isExists(requestInfo.getEbmtCic())) {
            return "PM5";
        }

        if (isExists(requestInfo.getBirthDate()) && isExists(requestInfo.getGender()) && isExists(requestInfo.getFirstName()) && isExists(requestInfo.getLastName())) {
            return "PM10";
        }

        return "PM"; // default value
    }




    /**
     *
     * @param ccn
     * @param requestInfo
     * @return
     */
    private PatientEntity buildPatientEntityFromRequestInfo( Long newCrid, Integer ccn, PatientRequestInfo requestInfo ) {

        PatientEntity patientEntity = new PatientEntity();

        patientEntity.setUnique_patient_id(newCrid);
        patientEntity.setCibmtr_center_number(ccn);
        patientEntity.setFirst_name(requestInfo.getFirstName());
        patientEntity.setLast_name(requestInfo.getLastName());
        patientEntity.setBirthday(convertStringToTimeStamp(requestInfo.getBirthDate()));
        patientEntity.setGender(requestInfo.getGender());
        patientEntity.setSsn(requestInfo.getSsn());
        patientEntity.setMothers_maiden_name(requestInfo.getMothersMaidenName());
        patientEntity.setNmdp_rid(requestInfo.getNmdpRid());
        patientEntity.setCibmtr_iubmid(requestInfo.getCibmtrIubmid());
        patientEntity.setCibmtr_team(requestInfo.getCibmtrTeam());
        patientEntity.setEbmt_id(requestInfo.getEbmtId());
        patientEntity.setEbmt_cic(requestInfo.getEbmtCic());

        return patientEntity;
    }

    /**
     * @param searchResults
     * @return patientResponseInfo
     */
    @SuppressWarnings("all")
    private PatientResponseInfo buildResponse(Map<String, Object> searchResults) {

        PatientResponseInfo patientResponseInfo = new PatientResponseInfo();

        Map<String, Wrapper> pMap = (Map<String, Wrapper>) searchResults.get("PM");
        Map<String, Wrapper> fMap = (Map<String, Wrapper>) searchResults.get("FM");
        Map<String, Wrapper> hMap = (Map<String, Wrapper>) searchResults.get("HM");

        // build perfect matches
        if (!CollectionUtils.isEmpty(pMap)) {
            List<PatientResponseInfo.Match> perfectMatches = new ArrayList<>();
            for (Map.Entry<String, Wrapper> entry : pMap.entrySet()) {
                convertToMatchResponse(entry.getKey(), entry.getValue(), perfectMatches);
            }
            patientResponseInfo.setPerfectMatch(perfectMatches);
        }

        // build fuzzy matches
        if (!CollectionUtils.isEmpty(fMap)) {
            List<PatientResponseInfo.Match> fuzzyMatches = new ArrayList<>();
            for (Map.Entry<String, Wrapper> entry : fMap.entrySet()) {
                convertToMatchResponse(entry.getKey(), entry.getValue(), fuzzyMatches);
            }
            patientResponseInfo.setFuzzyMatch(fuzzyMatches);
        }

        // build hidden matches
        if (!CollectionUtils.isEmpty(hMap)) {
            List<PatientResponseInfo.Match> hiddenMatches = new ArrayList<>();
            for (Map.Entry<String, Wrapper> entry : hMap.entrySet()) {
                convertToMatchResponse(entry.getKey(), entry.getValue(), hiddenMatches);
            }
            patientResponseInfo.setHiddenMatch(hiddenMatches);
        }

        // filter
        removeDuplicateCrids(patientResponseInfo);

        return patientResponseInfo;
    }

    /**
     * @param patientResponseInfo
     */
    private static void removeDuplicateCrids(PatientResponseInfo patientResponseInfo) {

        // remove duplicate crids from perfect matches
        List<PatientResponseInfo.Match> perfectMatches = patientResponseInfo.getPerfectMatch()
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // remove duplicate crids from fuzzy matches
        List<PatientResponseInfo.Match> fuzzyMatches = patientResponseInfo.getFuzzyMatch().stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // if there is a perfect match we do not want the fuzzy match for the same person.
        fuzzyMatches.removeAll(perfectMatches);

        // for now we are not filtering any hidden matches

        // finally set the values back in response
        patientResponseInfo.setPerfectMatch(perfectMatches);
        patientResponseInfo.setFuzzyMatch(fuzzyMatches);
    }

    /*

     */

    /**
     * @param firstName
     * @param lastName
     * @param dob
     * @param gender
     * @param mothersMaidenName
     * @return
     */
    private int countByValue(String firstName, String lastName, String dob, String gender, String mothersMaidenName) {

        int count = 0;

        if (isExists(firstName)) count++;
        if (isExists(lastName)) count++;
        if (isExists(dob)) count++;
        if (isExists(gender)) count++;
        if (isExists(mothersMaidenName)) count++;

        return count;
    }

    /**
     * @param firstName
     * @param lastName
     * @param mothersMaidenName
     * @return
     */
    private int countByValue(String firstName, String lastName, String mothersMaidenName) {

        int count = 0;

        if (isExists(firstName)) count++;
        if (isExists(lastName)) count++;
        if (isExists(mothersMaidenName)) count++;

        return count;
    }

    /**
     * @param dob
     * @param gender
     * @return
     */
    private int countByValue(String dob, String gender) {

        int count = 0;

        if (isExists(dob)) count++;
        if (isExists(gender)) count++;

        return count;
    }

    private boolean isExists(String value) {
        return !StringUtils.isEmpty(value);
    }

    @Data
    @AllArgsConstructor
    public static class Wrapper {
        PatientRequestInfo inputCriteria;
        List<PatientEntity> patientEntities;
    }

}
