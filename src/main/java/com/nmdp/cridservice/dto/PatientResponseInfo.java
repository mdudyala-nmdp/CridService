package com.nmdp.cridservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mdudyala
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientResponseInfo {

    List<Match> perfectMatch = new ArrayList<>();
    List<Match> fuzzyMatch = new ArrayList<>();

    //Hidden Match where ccn is null (patient is not found in center number and exist)
    List<Match> hiddenMatch = new ArrayList<>();

    // This crid is when there was no perfect match in the db and we created a new crid
    Long crid;

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Match {
        String[] matchedCriteria;
        String matchType;
        Long crid;
        List<Extension> extensions;

        Integer ccn;

        String firstName;
        String lastName;
        String birthDate;
        String gender;
        String ssn;
        String mothersMaidenName;

        Integer nmdpRid;
        String cibmtrIubmid;
        Integer cibmtrTeam;
        String ebmtId;
        String ebmtCic;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Match match = (Match) o;
            return Objects.equals(crid, match.crid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(crid);
        }
    }
}
