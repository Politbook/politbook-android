package org.appeleicao2014.model;

/**
 * Created by thaleslima on 8/23/14.
 */
public class State {

    private String uf;
    private String description;
    private int image;

    public State()
    {

    }

    public State(String uf, String description, int image) {
        this.uf = uf;
        this.description = description;
        this.image = image;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
