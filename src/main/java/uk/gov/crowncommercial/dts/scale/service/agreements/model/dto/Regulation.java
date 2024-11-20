package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Regulation
 */
public enum Regulation {

    @JsonProperty("PA2023")
    PA2023("PA2023"),

    @JsonProperty("PCR2015")
    PCR2015("PCR2015"),

    @JsonProperty("PCR2006")
    PCR2006("PCR2006");

    private String name;

    private Regulation(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @JsonCreator
    public static Regulation getRegulationFromName(String value) {

        for (Regulation r : Regulation.values()) {
            if (r.getName().equalsIgnoreCase(value)) {
                return r;
            }
        }

        return null;
    }
}