package org.appeleicao2014.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by thaleslima on 8/26/14.
 */
public class ListCandidate implements Serializable {
    private List<Candidate> candidates;
    private int position;

    public ListCandidate(List<Candidate> candidates, int position) {
        this.candidates = candidates;
        this.position = position;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
