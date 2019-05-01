package edu.ncsu.csc.itrust.controller.obstetricsOfficeVisit;

// PACKAGE HERE

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import edu.ncsu.csc.itrust.model.ValidationFormat;
// TO DO
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitMySQL;

import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitMySQL;
import edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitController;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
//
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import net.sf.cglib.core.Local;

@ManagedBean(name = "obstetrics_office_visit_form")
@ViewScoped
public class ObstetricsOfficeVisitForm {
    private ObstetricsOfficeVisitController controller;
    private ObstetricsOfficeVisit ov;
    private Long visitID;
    private Long patientMID;
    private LocalDateTime date;
    private Integer numOfWeeksPregnant;
    private Float weight;
    private String bloodPressure;
    private Integer fetalHeartRate;
    private Integer multiplePregnancy;
    private String lowLyingPlacenta;

    @PostConstruct
    public void init(){
        date = LocalDateTime.now();
        numOfWeeksPregnant = 2;
        // Generate numOfWeeksPregnant through LMP
        try {
            ObstetricVisitController ovc = new ObstetricVisitController();
//            List<ObstetricVisit> ovList = ;
            numOfWeeksPregnant = ovc.getObstetricVisitsForPatient(String.valueOf(patientMID)).get(0).getWeeksPregnant().intValue();
        }catch (Exception e) { FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Obstetric Office Visit Controller Error", "Obstetric Office Visit Controller Error")); }

    }


    public Long getVisitID() {
        return visitID;
    }

    public void setVisitID(Long visitID) {
        this.visitID = visitID;
    }

    public Long getPatientMID() {
        return patientMID;
    }

    public void setPatientMID(Long patientMID) {
        this.patientMID = patientMID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getNumOfWeeksPregnant() {
        return numOfWeeksPregnant;
    }

    public void setNumOfWeeksPregnant(Integer NumOfWeeksPregnant) {
        this.numOfWeeksPregnant = NumOfWeeksPregnant;
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

    public String getLowLyingPlacenta() { return lowLyingPlacenta; }

    public void setLowLyingPlacenta(String lowLyingPlacenta) { this.lowLyingPlacenta = lowLyingPlacenta; }



    /**
     * Default constructor for ObstetricsOfficeVisitForm.
     */
    public ObstetricsOfficeVisitForm() {
        this(null);
    }

    /**
     * Constructor for ObstetricsOfficeVisitForm for testing purpses.
     */
    public ObstetricsOfficeVisitForm(ObstetricsOfficeVisitController ovc) {
        try {
            controller = (ovc == null) ? new ObstetricsOfficeVisitController() : ovc;
            ov = controller.getSelectedVisit();
            if (ov == null) { ov = new ObstetricsOfficeVisit(); }
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", ov.getVisitID());
            } catch (NullPointerException e) { }
            visitID = ov.getVisitID();
            patientMID = ov.getPatientMID();
            if (patientMID == null) { patientMID = Long.parseLong((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid")); }
            date = ov.getDate();
            numOfWeeksPregnant = ov.getNumOfWeeksPregnant();
            weight = ov.getWeight();
            bloodPressure = ov.getBloodPressure();
            fetalHeartRate = ov.getFetalHeartRate();
            multiplePregnancy = ov.getMultiplePregnancy();
            lowLyingPlacenta = ov.getLowLyingPlacenta();
        } catch (Exception e) { FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Obstetric Office Visit Controller Error", "Obstetric Office Visit Controller Error")); }
    }

    /**
     * Called when user clicks on the submit button in obstetric office visit form. Takes data from form
     * and sends to ObstetricsOfficeVisitMySQL.java for storage
     */

    public void submit() {
        ov.setDate(date);
        ov.setPatientMID(patientMID);
        ov.setNumOfWeeksPregnant(numOfWeeksPregnant);
        ov.setWeight(weight);
        ov.setBloodPressure(bloodPressure);
        ov.setFetalHeartRate(fetalHeartRate);
        ov.setMultiplePregnancy(multiplePregnancy);
        ov.setLowLyingPlacenta(lowLyingPlacenta);
        if (isOfficeVisitCreated()) {
            controller.edit(ov);
            controller.logEditObstetricOfficeVisit(ov.getVisitID().toString());
        } else {
            long pid = -1;
            FacesContext ctx = FacesContext.getCurrentInstance();
            String patientID = "";
            if (ctx != null && ctx.getExternalContext() != null && ctx.getExternalContext().getRequest() instanceof HttpServletRequest) {
                HttpSession httpSession = ((HttpServletRequest) ctx.getExternalContext().getRequest()).getSession(false);
                patientID = (String) httpSession.getAttribute("pid");
            }
            if (ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) { pid = Long.parseLong(patientID); }

            ov.setPatientMID(pid);
            ov.setVisitID(null);
            long generatedVisitId = controller.addReturnGeneratedId(ov);
            setVisitID(generatedVisitId);
            ov.setVisitID(generatedVisitId);
            controller.logCreateObstetricOfficeVisit(ov.getVisitID().toString());
            if (FacesContext.getCurrentInstance() != null)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", generatedVisitId);

        }
        if (FacesContext.getCurrentInstance() != null) {
            ExternalContext ctxx = FacesContext.getCurrentInstance().getExternalContext();
            try { ctxx.redirect("/iTrust/auth/hcp-uap/viewObstetricOfficeVisits.xhtml");
            } catch (IOException e) { e.printStackTrace(); }
        }

    }

    /**
     *
     * @return true if the office visit id is set
     */
    public boolean isOfficeVisitCreated() { return (visitID != null) && (visitID > 0);    }
}
