package com.androidapp.manunya.gridimagesearch.models;

import java.io.Serializable;

/**
 * Created by Manunya on 5/14/2015.
 */
public class SearchSetting implements Serializable {
    public String size;
    public String colorFilter;
    public String type;
    public String siteFilter;

    public SearchSetting(String size, String colorFilter, String type, String siteFilter) {
        this.size = size;
        this.colorFilter = colorFilter;
        this.type = type;
        this.siteFilter = siteFilter;
    }

    public SearchSetting() {
        this("any", "any", "any", "any");
    }

    public static boolean hasValue(String value) {
        if (value.equals("any") || value.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColorFilter() {
        return colorFilter;
    }

    public void setColorFilter(String colorFilter) {
        this.colorFilter = colorFilter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }
}
