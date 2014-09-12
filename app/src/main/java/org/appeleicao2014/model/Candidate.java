package org.appeleicao2014.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by thaleslima on 8/20/14.
 */
public class Candidate implements Serializable {
    private String id;

    @SerializedName("apelido")
    private String nickname;

    @SerializedName("nome")
    private String name;

    @SerializedName("numero")
    private String number;

    @SerializedName("titulo")
    private String title;

    @SerializedName("CPF")
    private String CPF;

    @SerializedName("matricula")
    private String enrollment;

    @SerializedName("cargo")
    private String jobTitle;

    @SerializedName("estado")
    private String state;

    @SerializedName("partido")
    private String party;

    @SerializedName("idade")
    private String age;

    @SerializedName("instrucao")
    private String instruction;

    @SerializedName("ocupacao")
    private String occupation;

    @SerializedName("miniBio")
    private String miniBio;

    @SerializedName("cargos")
    private String positions;

    @SerializedName("previsao")
    private String prediction;

    @SerializedName("bancadas")
    private String countertops;

    @SerializedName("processos")
    private String processes;

    @SerializedName("casaAtual")
    private String currentHome;

    @SerializedName("reeleicao")
    private String reelection;

    @SerializedName("foto")
    private String photo;

    private String idJobTitle;

    private String idParty;

    private float average;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getIdJobTitle() {
        return idJobTitle;
    }

    public void setIdJobTitle(String idJobTitle) {
        this.idJobTitle = idJobTitle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMiniBio() {
        return miniBio;
    }

    public void setMiniBio(String miniBio) {
        this.miniBio = miniBio;
    }

    public String getPositions() {
        return positions;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getCountertops() {
        return countertops;
    }

    public void setCountertops(String countertops) {
        this.countertops = countertops;
    }

    public String getProcesses() {
        return processes;
    }

    public void setProcesses(String processes) {
        this.processes = processes;
    }

    public String getCurrentHome() {
        return currentHome;
    }

    public void setCurrentHome(String currentHome) {
        this.currentHome = currentHome;
    }

    public String getReelection() {

        if(reelection == null)
            return "";

        return reelection;
    }

    public void setReelection(String reelection) {
        this.reelection = reelection;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getIdParty() {
        return idParty;
    }

    public void setIdParty(String idParty) {
        this.idParty = idParty;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }
}
