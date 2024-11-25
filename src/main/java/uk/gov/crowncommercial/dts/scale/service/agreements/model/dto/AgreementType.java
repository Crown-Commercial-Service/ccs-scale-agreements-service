package uk.gov.crowncommercial.dts.scale.service.agreements.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public enum AgreementType {

    @JsonProperty("Dynamic Purchasing System")
    DYNAMIC_PURCHASING_SYSTEM("Dynamic Purchasing System"),

    @JsonProperty("Dynamic Market")
    DYNAMIC_MARKET("Dynamic Market"),

    @JsonProperty("Open Framework")
    OPEN_FRAMEWORK("Open Framework"),

    @JsonProperty("Closed Framework")
    CLOSED_FRAMEWORK("Closed Framework"),

    @JsonProperty("PCR15 Framework")
    PCR15_FRAMEWORK("PCR15 Framework"),

    @JsonProperty("PCR06 Framework")
    PCR06_FRAMEWORK("PCR06 Framework");

    private String name;

    private AgreementType(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @JsonCreator
    public static AgreementType getAgreementTypeFromName(String value) {

        for (AgreementType a : AgreementType.values()) {
            if (a.getName().equalsIgnoreCase(value)) {
                return a;
            }
        }

        return null;
    }

    public static String getStringFormatForAgreementTypes( List<AgreementType> agreementTypeList){

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < agreementTypeList.size(); i++) {
            sb.append(agreementTypeList.get(i).getName());
            if (i < agreementTypeList.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
