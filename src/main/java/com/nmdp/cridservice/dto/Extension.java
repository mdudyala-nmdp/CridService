package com.nmdp.cridservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Extension {

    String registry;
    String registryID;
    String registryValue;
    String cibmtrIubmID;
    String cibmtrTeam;
    String nmdpRID;

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getRegistryID() {
        return registryID;
    }

    public void setRegistryID(String registryID) {
        this.registryID = registryID;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }


    public String getCibmtrIubmID() {
        return cibmtrIubmID;
    }

    public void setCibmtrIubmID(String cibmtrIubmID) {
        this.cibmtrIubmID = cibmtrIubmID;
    }

    public String getCibmtrTeam() {
        return cibmtrTeam;
    }

    public void setCibmtrTeam(String cibmtrTeam) {
        this.cibmtrTeam = cibmtrTeam;
    }

    public String getNmdpRID() {
        return nmdpRID;
    }

    public void setNmdpRID(String nmdpRID) {
        this.nmdpRID = nmdpRID;
    }

}
