/**
 *
 */
package edu.ncsu.csc.itrust.model.obstetricVisit;

//import net.sf.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.Duration;
import javax.faces.bean.ManagedBean;

/**
 * @author T819
 *
 */

@ManagedBean(name="obstetric_visit")
public class ObstetricVisit {
    private Long visitID;
    private Long patientMID;
    private LocalDateTime visitDate;
    private String locationID;
    private Long apptTypeID;
    private String notes;
    private LocalDateTime lmp;
    private LocalDateTime edd;
    private Long weeksPregnant;


    /**
     * Default constructor for OfficeVisit
     */
    public ObstetricVisit(){
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

    /**
     * @return the date
     */
    public LocalDateTime getDate() {
        return visitDate;
    }

    /**
     * @param date the date of the office visit
     */
    public void setDate(LocalDateTime date) {
        this.visitDate = date;
    }

    /**
     * @return the locationID
     */
    public String getLocationID() {
        return locationID;
    }

    /**
     * @param location the location ID to set
     */
    public void setLocationID(String location) {
        this.locationID = location;
    }

    /**
     * @return the apptTypeID
     */
    public Long getApptTypeID() {
        return apptTypeID;
    }

    /**
     * @param apptType the appointment type ID to set
     */
    public void setApptTypeID(Long apptType) {
        this.apptTypeID = apptType;
    }

    /**
     * @return the visitID
     */
    public Long getVisitID() {
        return visitID;
    }

    /**
     * @param visitID the visit ID to set
     */
    public void setVisitID(Long visitID) {
        this.visitID = visitID;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @param lmp the last menstrual period (LMP) to set
     */
    public void setLMP(LocalDateTime lmp) {
        this.lmp = lmp;
    }

    /**
     * the estimated due date (edd) to set
     * based on last menstrual period (LMP)
     */
    public void setEDD() {
        this.edd = lmp.plusDays(280);
    }

    /**
     * the weeks pregnant to set
     * based on last menstrual period (LMP)
     */
    public void setWeeksPregnant() {
        LocalDateTime localDateTime = getDate();
        Duration d1 = Duration.between(lmp, localDateTime);
        long daysPregnant = d1.toDays();
        this.weeksPregnant = (daysPregnant/7);
    }

    /**
     * @return the lmp (Last Menstrual Period)
     */
    public LocalDateTime getLMP() {return lmp;}

    /**
     * @return the edd (Estimated Due Date)
     */
    public LocalDateTime getEDD() {
        return edd;
    }

    /**
     * @return the weeks pregnant
     */
    public Long getWeeksPregnant() {
        return weeksPregnant;
    }

}
