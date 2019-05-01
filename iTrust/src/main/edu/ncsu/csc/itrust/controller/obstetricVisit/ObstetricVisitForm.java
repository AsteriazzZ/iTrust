package edu.ncsu.csc.itrust.controller.obstetricVisit;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import net.sf.cglib.core.Local;

@ManagedBean(name = "obstetric_visit_form")
@ViewScoped
public class ObstetricVisitForm {
    private ObstetricVisitController controller;
    private ObstetricVisit ov;
    private Long visitID;
    private Long patientMID;
    private LocalDateTime date;
    private String locationID;
    private Long apptTypeID;
    private String notes;
    private LocalDateTime lmp;

    /**
     * To Initialize the object with current date
     */
    @PostConstruct
    public void init(){
        date = LocalDateTime.now();
    }

    /**
     * Returns the VisitID
     *
     * @return the VisitID
     */
    public Long getVisitID() {
        return visitID;
    }

    public void setVisitID(Long visitID) {
        this.visitID = visitID;
    }

    /**
     * Returns the PatientMID
     *
     * @return the PatientMID
     */
    public Long getPatientMID() {
        return patientMID;
    }

    public void setPatientMID(Long patientMID) {
        this.patientMID = patientMID;
    }

    /**
     * Returns the Date recorded at the obstetric visit.
     *
     * @return the Date recorded at the obstetric visit.
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the LocationID
     *
     * @return the LocationID
     */
    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    /**
     * Returns the Appointment Type ID recorded at the office visit.
     *
     * @return the Appointment Type ID  recorded at the office visit.
     */
    public Long getApptTypeID() {
        return apptTypeID;
    }

    public void setApptTypeID(Long apptTypeID) {
        this.apptTypeID = apptTypeID;
    }

    /**
     * Returns the Date recorded at the obstetric visit.
     *
     * @return the Date recorded at the obstetric visit.
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setLMP(LocalDateTime lmp) {
        this.lmp = lmp;
    }

    public LocalDateTime getLMP() {
        return lmp;
    }

    /**
     * Default constructor for OfficeVisitForm.
     */
    public ObstetricVisitForm() { this(null); }

    /**
     * Constructor for OfficeVisitForm for testing purposes.
     */
    public ObstetricVisitForm(ObstetricVisitController ovc) {
        try {
            controller = (ovc == null) ? new ObstetricVisitController() : ovc;
            ov = controller.getSelectedVisit();
            if (ov == null) { ov = new ObstetricVisit(); }
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", ov.getVisitID());
            } catch (NullPointerException e) { }
            visitID = ov.getVisitID();
            patientMID = ov.getPatientMID();
            if (patientMID == null) { patientMID = Long.parseLong((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
            }
            date = ov.getDate();
            locationID = ov.getLocationID();
            apptTypeID = ov.getApptTypeID();
            notes = ov.getNotes();
            lmp = ov.getLMP();
        } catch (Exception e) {
            FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Office Visit Controller Error",
                    "Office Visit Controller Error");
            FacesContext.getCurrentInstance().addMessage(null, throwMsg);
        }
    }

    /**
     * Called when user clicks on the submit button in the front-end. Takes data from form
     * and sends to ObstetricVisitMySQLLoader.java for storage and validation
     */
    public void submit() {
        ov.setApptTypeID(apptTypeID);
        ov.setDate(date);
        ov.setLocationID(locationID);
        ov.setNotes(notes);
        ov.setPatientMID(patientMID);
        ov.setLMP(lmp);
        if (isOfficeVisitCreated()) {
            controller.edit(ov);
        } else {
            long pid = -1;

            FacesContext ctx = FacesContext.getCurrentInstance();

            String patientID = "";

            if (ctx != null && ctx.getExternalContext() != null && ctx.getExternalContext().getRequest() instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
                HttpSession httpSession = req.getSession(false);
                patientID = (String) httpSession.getAttribute("pid");
            }
            if (ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) { pid = Long.parseLong(patientID);
            }

            ov.setPatientMID(pid);
            ov.setVisitID(null);
            long generatedVisitId = controller.addReturnGeneratedId(ov);
            setVisitID(generatedVisitId);
            ov.setVisitID(generatedVisitId);
            controller.logCreateObstetricVisit(ov.getLMP().plusDays(280).toString());
            if (FacesContext.getCurrentInstance() != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", generatedVisitId);

                ExternalContext ctxx = FacesContext.getCurrentInstance().getExternalContext();
                if (ctxx != null) {
                    try { ctxx.redirect("/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml"); }
                    catch (IOException e) { e.printStackTrace(); }
                }
            }
        }
    }

    /**
     * Check if visit is created
     * @return true if there is visit, otherwise false
     */
    public boolean isOfficeVisitCreated() {
        return (visitID != null) && (visitID > 0);
    }
}
