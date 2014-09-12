package org.appeleicao2014.model;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by thaleslima on 9/2/14.
 */
public class Comment implements Serializable {
    private static SimpleDateFormat simpleDateFormat;
    private static Calendar calendar;
    private int idUser;
    private String nameUser;
    private String idSocial;
    private String idCandidate;
    private int rating;
    private String date;
    private String comment;
    private int day;
    private int month;
    private int year;
    private double latitude;
    private double longitude;

    private CommentCandidate candidate;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getIdCandidate() {
        return idCandidate;
    }

    public void setIdCandidate(String idCandidate) {
        this.idCandidate = idCandidate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        if(date == null) {
            getCalendar().set(year, month, day);
            date = getSimpleDateFormat().format(getCalendar().getTime());
        }

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentCandidate getCandidate() {
        return candidate;
    }

    public void setCandidate(CommentCandidate candidate) {
        this.candidate = candidate;
    }

    public static SimpleDateFormat getSimpleDateFormat() {

        if(simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        }

        return simpleDateFormat;
    }

    public static Calendar getCalendar() {
        if(calendar == null)
        {
            calendar = Calendar.getInstance();
        }

        return calendar;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIdSocial() {

        if(idSocial == null)
        {
            idSocial = "";
        }

        return idSocial;
    }

    public void setIdSocial(String idSocial) {
        this.idSocial = idSocial;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
