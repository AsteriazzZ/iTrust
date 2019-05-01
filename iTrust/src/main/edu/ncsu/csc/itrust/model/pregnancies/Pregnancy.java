package edu.ncsu.csc.itrust.model.pregnancies;

/**
 *
 */

import javax.faces.bean.ManagedBean;

/**
 * @author seelder
 *
 */

@ManagedBean(name="pregnancy")
public class Pregnancy {
    private Long pregnancyID;
    private Long patientMID;
    private Integer conceptionYear;
    private Integer weeksPregnant;
    private Integer laborHours;
    private Integer weightGain;
    private String deliveryType;
    private Integer numberOfChildren;

    /**
     * Default constructor for OfficeVisit
     */
    public Pregnancy(){
    }


    /**
     * @return the patientMID
     */
    public Long getPatientMID() {
        return patientMID;
    }

    /**
     * @param patientID the patient MID to set
     */
    public void setPatientMID(Long patientID) {
        this.patientMID = patientID;
    }

    public Long getPregnancyID(){return pregnancyID;}

    public void setPregnancyID(Long pregnancyID) {this.pregnancyID = pregnancyID;}

    public Integer getConceptionYear(){return conceptionYear;}

    public void setConceptionYear(Integer conceptionYear){this.conceptionYear = conceptionYear;}

    public Integer getLaborHours(){return laborHours;}

    public void setLaborHours(Integer laborHours){this.laborHours = laborHours;}

    public Integer getWeeksPregnant() {return weeksPregnant;}

    public void setWeeksPregnant(Integer weeksPregnant){this.weeksPregnant = weeksPregnant;}

    public String getDeliveryType(){return deliveryType;}

    public void setDeliveryType(String deliveryType){this.deliveryType = deliveryType;}

    public void setWeightGain(Integer weightGain){this.weightGain = weightGain;}

    public Integer getWeightGain(){return weightGain;}

    public Integer getNumberOfChildren() {return numberOfChildren;}

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
