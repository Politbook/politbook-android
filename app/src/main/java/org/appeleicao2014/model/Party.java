package org.appeleicao2014.model;

/**
 * Created by thaleslima on 8/31/14.
 */
public class Party {

    private String id;
    private String party;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    @Override
    public String toString() {
        return getParty();
    }
}
