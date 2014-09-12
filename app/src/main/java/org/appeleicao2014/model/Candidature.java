package org.appeleicao2014.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thaleslima on 8/26/14.
 */
public class Candidature {

    @SerializedName("anoEleitoral")
    private String electionYear;

    @SerializedName("cargo")
    private String jobTitle;

    @SerializedName("partidoSigla")
    private String party;

    @SerializedName("municipio")
    private String city;

    @SerializedName("estadoSigla")
    private String state;

    @SerializedName("resultado")
    private String result;

    @SerializedName("votosObtidos")
    private String votes;

    @SerializedName("recursosFinanceiros")
    private String financialResources;


    public String getFinancialResources() {
        return financialResources;
    }

    public void setFinancialResources(String financialResources) {
        this.financialResources = financialResources;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getElectionYear() {
        return electionYear;
    }

    public void setElectionYear(String electionYear) {
        this.electionYear = electionYear;
    }




}
