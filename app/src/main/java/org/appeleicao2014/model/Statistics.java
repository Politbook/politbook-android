package org.appeleicao2014.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thaleslima on 8/20/14.
 */
public class Statistics {

    @SerializedName("faltasPlenario")
    private String faultsPlenary;

    @SerializedName("mediaPlenario")
    private String averagePlenary;

    @SerializedName("faltasComissoes")
    private String faultsCommissions;

    @SerializedName("mediaComissoes")
    private String averageCommissions;

    @SerializedName("evolucao")
    private String evolution;

    @SerializedName("anoRef")
    private String referenceYear;

    @SerializedName("emendas")
    private String amendments;

    @SerializedName("mediaEmendas")
    private String averageAmendments;

    public String getAmendments() {
        return amendments;
    }

    public void setAmendments(String amendments) {
        this.amendments = amendments;
    }


    public String getFaultsPlenary() {
        return faultsPlenary;
    }

    public void setFaultsPlenary(String faultsPlenary) {
        this.faultsPlenary = faultsPlenary;
    }

    public String getAveragePlenary() {
        return averagePlenary;
    }

    public void setAveragePlenary(String averagePlenary) {
        this.averagePlenary = averagePlenary;
    }

    public String getFaultsCommissions() {
        return faultsCommissions;
    }

    public void setFaultsCommissions(String faultsCommissions) {
        this.faultsCommissions = faultsCommissions;
    }

    public String getAverageCommissions() {
        return averageCommissions;
    }

    public void setAverageCommissions(String averageCommissions) {
        this.averageCommissions = averageCommissions;
    }

    public String getEvolution() {
        return evolution;
    }

    public void setEvolution(String evolution) {
        this.evolution = evolution;
    }

    public String getReferenceYear() {
        return referenceYear;
    }

    public void setReferenceYear(String referenceYear) {
        this.referenceYear = referenceYear;
    }

    public String getAverageAmendments() {
        return averageAmendments;
    }

    public void setAverageAmendments(String averageAmendments) {
        this.averageAmendments = averageAmendments;
    }
}
