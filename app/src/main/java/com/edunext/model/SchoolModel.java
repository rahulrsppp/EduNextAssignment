package com.edunext.model;

import com.google.gson.annotations.SerializedName;

public class SchoolModel  {

    @SerializedName("id")
    private String id;

    @SerializedName("school_code")
    private String school_code;

    @SerializedName("url")
    private String url;

    @SerializedName("addedon")
    private String addedon;

    @SerializedName("status")
    private String status;

    public SchoolModel(String id, String school_code, String url, String addedon, String status) {
        this.id = id;
        this.school_code = school_code;
        this.url = url;
        this.addedon = addedon;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getSchool_code() {
        return school_code;
    }

    public String getUrl() {
        return url;
    }

    public String getAddedon() {
        return addedon;
    }

    public String getStatus() {
        return status;
    }

}


