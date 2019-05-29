package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by user on 2/17/2016.
 */
public class FeatureListModal  {

    private String featureTitle;
    private int featureIcon;

    public void setFeatureIcon(int featureIcon) {
        this.featureIcon = featureIcon;
    }

    public void setFeatureTitle(String featureTitle) {
        this.featureTitle = featureTitle;
    }

    public int getFeatureIcon() {
        return featureIcon;
    }

    public String getFeatureTitle() {
        return featureTitle;
    }
}
