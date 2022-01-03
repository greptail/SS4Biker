
package com.generalmobi.ss4bikers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OverviewPolyline implements Serializable {

    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
