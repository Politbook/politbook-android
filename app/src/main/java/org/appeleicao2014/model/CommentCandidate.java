package org.appeleicao2014.model;

import java.io.Serializable;

/**
 * Created by thaleslima on 9/7/14.
 */
public class CommentCandidate implements Serializable {
    public String id;
    public String name;
    public String state;
    public String idTitleJob;
    public float average;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdTitleJob() {
        return idTitleJob;
    }

    public void setIdTitleJob(String idTitleJob) {
        this.idTitleJob = idTitleJob;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }
}
