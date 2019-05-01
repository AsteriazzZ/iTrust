package edu.ncsu.csc.itrust.controller.pregnancies;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Year;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;
import edu.ncsu.csc.itrust.model.pregnancies.PregnancyMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import net.sf.cglib.core.Local;

@ManagedBean(name = "pregnancy_form")
@ViewScoped
public class PregnancyForm {
    private PregnancyController controller;
    private Pregnancy pregnancy;
    private Long pregnancyID;
    private Long patientMID;
    private Integer conceptionYear;
    private Integer weeksPregnant;
    private Integer laborHours;
    private Integer weightGain;
    private String deliveryType;
    private Integer numberOfChildren;

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
    /**
     * Default constructor for PregnancyForm.
     */
    public PregnancyForm() { this(null); }

    /**
     * Constructor for PregnancyForm for testing purpses.
     */
    public PregnancyForm(PregnancyController pregController) {
        try {
            controller = (pregController == null) ? new PregnancyController() : pregController;
            pregnancy = controller.getSelectedPregnancy();
            if (pregnancy == null) { pregnancy = new Pregnancy(); }
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pregnancyID", getPregnancyID()); }
                catch (NullPointerException e) { }
            pregnancyID = pregnancy.getPregnancyID();
            patientMID = pregnancy.getPatientMID();
            if (patientMID == null) { patientMID = Long.parseLong((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
            }
            conceptionYear = pregnancy.getConceptionYear();
            weeksPregnant = pregnancy.getWeeksPregnant();
            laborHours = pregnancy.getLaborHours();
            weightGain = pregnancy.getWeightGain();
            deliveryType = pregnancy.getDeliveryType();
            numberOfChildren = pregnancy.getNumberOfChildren();

        } catch (Exception e) {
            FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pregnancy Controller Error",
                    "Pregnancy Controller Error");
            FacesContext.getCurrentInstance().addMessage(null, throwMsg);
        }
    }

    /**
     * Called when user clicks on the submit button in pregnancyInfo.xhtml. Takes data from form
     * and sends to PregnancySQLLoader.java for storage and validation
     */
    public void submit() {
        pregnancy.setPregnancyID(pregnancyID);
        pregnancy.setPatientMID(patientMID);
        pregnancy.setConceptionYear(conceptionYear);
        pregnancy.setWeeksPregnant(weeksPregnant);
        pregnancy.setLaborHours(laborHours);
        pregnancy.setWeightGain(weightGain);
        pregnancy.setDeliveryType(deliveryType);
        pregnancy.setNumberOfChildren(numberOfChildren);
        if (isPregnancyCreated()) {
            controller.edit(pregnancy);
//            controller.logTransaction(TransactionType.OFFICE_VISIT_EDIT, ov.getVisitID().toString());
        } else {
            long pid = -1;

            FacesContext ctx = FacesContext.getCurrentInstance();

            String patientID = "";

            if (ctx != null && ctx.getExternalContext() != null && ctx.getExternalContext().getRequest() instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
                HttpSession httpSession = req.getSession(false);
                patientID = (String) httpSession.getAttribute("pid");
            }
            if (ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) { pid = Long.parseLong(patientID); }

            pregnancy.setPatientMID(pid);
            pregnancy.setPregnancyID(null);
            long generatedPregnancyID = controller.addReturnGeneratedId(pregnancy);
            setPregnancyID(generatedPregnancyID);
            pregnancy.setPregnancyID(generatedPregnancyID);
//            controller.logTransaction(TransactionType.OFFICE_VISIT_CREATE, ov.getVisitID().toString());
            if (FacesContext.getCurrentInstance() != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pregnancyID", generatedPregnancyID);

                ExternalContext ctxx = FacesContext.getCurrentInstance().getExternalContext();
                if (ctxx != null) {
                    try { ctxx.redirect("/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml"); }
                    catch (IOException e) { e.printStackTrace(); }
                }
            }
        }
    }

    public boolean isPregnancyCreated() {
        return (pregnancyID != null) && (pregnancyID > 0);
    }
}
