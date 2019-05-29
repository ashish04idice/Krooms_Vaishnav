package com.krooms.hostel.rental.property.app.modal;

/**
 * Created by user on 3/30/2016.
 */
public class TenantUserIdProofModal {

    private String idProofType = "";
    private String idProofImagePath = "";
    private String fromLocalOrServer = "";

    public static final String FROMLOCAL = "local";
    public static final String FROMSERVER = "server";

    public void setIdProofImagePath(String idProofImagePath) {
        this.idProofImagePath = idProofImagePath;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public void setFromLocalOrServer(String fromLocalOrServer) {
        this.fromLocalOrServer = fromLocalOrServer;
    }

    public String getFromLocalOrServer() {
        return fromLocalOrServer;
    }



    public String getIdProofImagePath() {

        idProofImagePath = idProofImagePath.replaceAll(" ", "%20");
        return idProofImagePath;
    }

    public String getIdProofType() {
        return idProofType;
    }

}
