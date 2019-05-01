package edu.ncsu.csc.itrust.model.childbirthVisit;

import java.time.LocalDateTime;
import java.time.Duration;
import javax.faces.bean.ManagedBean;

/**
 * class to store childbirth visit records
 */
@ManagedBean(name="childbirth_visit")
public class ChildbirthVisit {
    private Long visitID;
    private Long patientMID;
    private LocalDateTime visitDate;
    private String locationID;
    private String apptType = "Childbirth";
    private boolean preScheduled;
    private String deliveryType;
    private Long pitocin;
    private Long oxide;
    private Long pethidine;
    private Long epidural;
    private Long magnesium;
    private Long rh;
    public ChildbirthVisit() {

    }

    /**
     * gets visitID
     * @return
     */
    public Long getVisitID() {
        return visitID;
    }

    /**
     * sets visitID
     * @param visitID_
     */
    public void setVisitID(Long visitID_) {
        visitID = visitID_;
    }
    /**
     * gets patientMID
     * @return
     */
    public Long getPatientMID() {
        return patientMID;
    }

    /**
     * sets patientMID
     * @param patientMID_
     */
    public void setPatientMID(Long patientMID_) {
        patientMID = patientMID_;
    }
    /**
     * gets visitDate
     * @return
     */
    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    /**
     * sets visitDate
     * @param visitDate_
     */
    public void setVisitDate(LocalDateTime visitDate_) {
        visitDate = visitDate_;
    }
    /**
     * gets locationID
     * @return
     */
    public String getLocationID() {
        return locationID;
    }

    /**
     * sets locationID
     * @param locationID_
     */
    public void setLocationID(String locationID_) {
        locationID = locationID_;
    }
    /**
     * gets apptType
     * @return
     */
    public String getApptType() {
        return apptType;
    }

    /**
     * gets preScheduled
     * @return
     */
    public boolean getPreScheduled() {
        return preScheduled;
    }

    /**
     * sets preScheduled
     * @param preScheduled_
     */
    public void setPreScheduled(boolean preScheduled_) {
        preScheduled = preScheduled_;
    }
    /**
     * gets deliveryType
     * @return
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * sets deliveryType
     * @param deliveryType_
     */
    public void setDeliveryType(String deliveryType_) {
        deliveryType = deliveryType_;
    }
    /**
     * gets pitocin
     * @return
     */
    public Long getPitocin() { return pitocin; }

    /**
     * sets pitocin
     * @param pit
     */
    public void setPitocin(Long pit) { this.pitocin = pit; }
    /**
     * gets oxide
     * @return
     */
    public Long getOxide() { return oxide; }

    /**
     * sets oxide
     * @param pit
     */
    public void setOxide(Long pit) { this.oxide = pit; }
    /**
     * gets pethidine
     * @return
     */
    public Long getPethidine() { return pethidine; }

    /**
     * sets pethidine
     * @param pit
     */
    public void setPethidine(Long pit) { this.pethidine = pit; }
    /**
     * gets epidural
     * @return
     */
    public Long getEpidural() { return epidural; }

    /**
     * sets epidural
     * @param pit
     */
    public void setEpidural(Long pit) { this.epidural = pit; }
    /**
     * gets magnesium
     * @return
     */
    public Long getMagnesium() { return magnesium; }

    /**
     * sets magnesium
     * @param pit
     */
    public void setMagnesium(Long pit) { this.magnesium = pit; }
    /**
     * gets rh
     * @return
     */
    public Long getRh() { return rh; }

    /**
     * sets rh
     * @param pit
     */
    public void setRh(Long pit) { this.rh = pit; }
}