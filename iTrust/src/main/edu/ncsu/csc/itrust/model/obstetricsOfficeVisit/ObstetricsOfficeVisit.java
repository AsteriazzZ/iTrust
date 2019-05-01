package edu.ncsu.csc.itrust.model.obstetricsOfficeVisit;


import java.time.LocalDateTime;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="obstetrics_office_visit")
public class ObstetricsOfficeVisit {
    private Long visitID;
    private Long patientMID;
    private LocalDateTime date;
    private Integer numOfWeeksPregnant;
    private Float weight;
    private String bloodPressure;
    private Integer fetalHeartRate;
    private Integer multiplePregnancy;
    private String lowLyingPlacenta;

    /*
     * Default Constructor
     */
    public ObstetricsOfficeVisit(){}

    public Long getVisitID(){
        return visitID;
    }

    public void setVisitID(Long visitID){
        this.visitID = visitID;
    }

    public Long getPatientMID(){
        return patientMID;
    }

    public void setPatientMID(Long patientMID){
        this.patientMID = patientMID;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public Integer getNumOfWeeksPregnant(){
        return numOfWeeksPregnant;
    }

    public void setNumOfWeeksPregnant(Integer numOfWeeksPregnant){
        this.numOfWeeksPregnant = numOfWeeksPregnant;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public Integer getFetalHeartRate() {
        return fetalHeartRate;
    }

    public void setFetalHeartRate(Integer fetalHeartRate) {
        this.fetalHeartRate = fetalHeartRate;
    }

    public Integer getMultiplePregnancy() {
        return multiplePregnancy;
    }

    public void setMultiplePregnancy(Integer multiplePregnancy) {
        this.multiplePregnancy = multiplePregnancy;
    }

    public String getLowLyingPlacenta() {
        return lowLyingPlacenta;
    }

    public void setLowLyingPlacenta(String lowLyingPlacenta) {
        this.lowLyingPlacenta = lowLyingPlacenta;
    }


}
