package org.appeleicao2014.ui.adapter;

/**
 * Created by thaleslima on 8/24/14.
 */
public class Item {
    private int icon;
    private String description;
    private int flag;
    private boolean locale;

    public Item(String description) {
        this.description = description;
    }

    public Item(int icon, String description, int flag, boolean locale) {
        this.icon = icon;
        this.description = description;
        this.flag = flag;
        this.locale = locale;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isLocale() {
        return locale;
    }

    public void setLocale(boolean locale) {
        this.locale = locale;
    }
}
