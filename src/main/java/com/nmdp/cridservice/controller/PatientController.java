/**
 * 
 */
package com.nmdp.cridservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nmdp.cridservice.constants.PatientErrorMessages;
import com.nmdp.cridservice.dto.PatientRequest;
import com.nmdp.cridservice.dto.PatientRequestInfo;
import com.nmdp.cridservice.dto.PatientResponseInfo;
import com.nmdp.cridservice.exception.CridException;
import com.nmdp.cridservice.service.PatientSearchService;
import com.nmdp.cridservice.util.CridserviceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mdudyala
 *
 */
@RestController
@RequestMapping("/CRID")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PatientController {

    @Autowired
    PatientSearchService patientSearchService;

    private static String CCN_REGEX_PATTERN = "\\d\\d\\d\\d\\d";

    public static final Logger logger = LoggerFactory.getLogger(PatientController.class);
	
	// -------------------Retrieve PatientRequestInfo------------------------------------------
	
	//ccn- cibmtr center number
    @RequestMapping(value="/ccn/{ccn}/{patient}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatient(@PathVariable("ccn") Integer ccn, @PathVariable("patient") String patientInfo) {
    	

        if ( !CridserviceUtils.isValidJson( patientInfo) ) {
			return throwBadRequestException("GET: patient is not valid JSON");
        }

        PatientRequestInfo patientRequestInfo;
        PatientRequest patientRequest = new PatientRequest();
        String error = null;

        try {
            patientRequestInfo = new ObjectMapper().readValue(patientInfo, PatientRequestInfo.class);
		} catch (IOException e) {
            return throwBadRequestException("GET: Unable to parse the patient value");
		}

		// check for missing ccn value in the request param
        if ( StringUtils.isEmpty(patientRequest.getCcn())) {
            return throwBadRequestException(PatientErrorMessages.MISSING_CCN);
        }

        // ccn pattern
        Pattern pattern = Pattern.compile(CCN_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(Integer.toString(ccn));
        boolean matches = matcher.matches();
        if (!matches) {
            return throwBadRequestException(String.format("ccn %s is not valid", ccn));
        }

        // check for missing values
        error = CridserviceUtils.checkForMissingValues(patientRequestInfo);
        if ( error != null ) {
            return throwBadRequestException(error);
        }

        // check for matching pattern
        error = CridserviceUtils.checkValidPattern(patientRequestInfo);
        if ( error != null ) {
            return throwBadRequestException(error);
        }

        // search
        patientRequest.setCcn( ccn );
        patientRequest.setPatient( patientRequestInfo );

        PatientResponseInfo responseInfo = patientSearchService.search(patientRequest);

        // if there are more than one perfect match then throw an error
        if ( responseInfo.getPerfectMatch().size() > 1 ) {
            return throwException("Error : Multiple perfect matches found for the given criteria");
        }

    	return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }

    // -------------------create Patient------------------------------------------

    //ccn- cibmtr center number
    @RequestMapping(method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPatient(@RequestBody PatientRequest patientRequest) {

        String error = null;

        // check for missing ccn
        if ( StringUtils.isEmpty(patientRequest.getCcn())) {
            return throwBadRequestException(PatientErrorMessages.MISSING_CCN);
        }

        // ccn pattern
        Pattern pattern = Pattern.compile(CCN_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(Long.toString(patientRequest.getCcn()));
        boolean matches = matcher.matches();
        if (!matches) {
            return throwBadRequestException(String.format("ccn %s is not valid", patientRequest.getCcn()));
        }

        error = CridserviceUtils.checkForMissingValues(patientRequest.getPatient());
        if ( error != null ) {
            return throwBadRequestException(error);
        }

        // check for matching pattern
        error = CridserviceUtils.checkValidPattern(patientRequest.getPatient());
        if ( error != null ) {
            return throwBadRequestException(error);
        }


        // search with the given inputs
        PatientResponseInfo responseInfo = patientSearchService.search(patientRequest);

        // when there are no perfect matches then create the request and send 201(created) http status code

        // || (responseInfo.getHiddenMatch().size() == 0) removed as we are not supposed to update the hidden Match record as because the person has different center and we will not have permission to do
        if ( (responseInfo.getPerfectMatch().size() == 0) ) {
            responseInfo = patientSearchService.create(patientRequest, responseInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseInfo);
        } else {

            // if there are more than one perfect match then throw an error
            if ( responseInfo.getPerfectMatch().size() > 1 ) {
                return throwException("Error : Multiple perfect matches found for the given criteria");
            }

            responseInfo = patientSearchService.update(patientRequest, responseInfo);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseInfo);
    }

    /**
     *
     * @param message
     * @return
     */
    private ResponseEntity throwBadRequestException( String message ) {
        return new ResponseEntity(new CridException(message),
                HttpStatus.BAD_REQUEST);    	
    }

    /**
     *
     * @param errorMessage
     * @return
     */
    private ResponseEntity throwException( String errorMessage ) {
        return new ResponseEntity(new CridException(errorMessage),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}