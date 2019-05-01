package edu.ncsu.csc.itrust.controller.childbirthVisit;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * form class for childbirth visit
 */
@ManagedBean(name = "childbirth_visit_form")
@ViewScoped
public class ChildbirthVisitForm {
    private ChildbirthVisitController controller;
    private ChildbirthVisit cv;
    private Long visitID;
    private Long patientMID;
    private LocalDateTime visitDate;
    private String locationID;
    private String apptType;
    private boolean preScheduled;
    private String deliveryType;
    private Long pitocin;
    private Long oxide;
    private Long pethidine;
    private Long epidural;
    private Long magnesium;
    private Long rh;
    private boolean noDrugs;

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
     * sets apptType
     * @param apptType_
     */
    public void setApptType(String apptType_) {
        this.apptType = apptType_;
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

    public ChildbirthVisitForm() {
        this(null);
    }

    /**
     * constructor for form, creates new controller if new entry, or sets global variables if not, also records if drugs have been administered
     * @param ovc
     */
    public ChildbirthVisitForm(ChildbirthVisitController ovc) {
        try {
            controller = (ovc == null) ? new ChildbirthVisitController() : ovc;
            cv = controller.getSelectedVisit();
            if (cv == null) {
                cv = new ChildbirthVisit();
            }
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("childbirthVisitId", cv.getVisitID());
            } catch (NullPointerException e) {
            }
            visitID = cv.getVisitID();
            patientMID = cv.getPatientMID();
            if (patientMID == null) {
                patientMID = Long.parseLong(
                        (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
            }
            visitDate = cv.getVisitDate();
            locationID = cv.getLocationID();
//            apptType = cv.getApptType();
            preScheduled = cv.getPreScheduled();
            deliveryType = cv.getDeliveryType();
            pitocin = cv.getPitocin();
            pethidine = cv.getPethidine();
            magnesium = cv.getMagnesium();
            oxide = cv.getOxide();
            rh = cv.getRh();
            epidural = cv.getEpidural();
            if((pitocin == null && pethidine == null && magnesium == null && oxide == null && rh == null && epidural == null) ||
                    (pitocin == 0 && pethidine == 0 && magnesium == 0 && oxide == 0 && rh == 0 && epidural == 0))
                noDrugs = true;
            else
                noDrugs = false;
            if(pitocin == null)
                pitocin = new Long(0);
            if(pethidine == null)
                pethidine = new Long(0);
            if(magnesium == null)
                magnesium = new Long(0);
            if(oxide == null)
                oxide = new Long(0);
            if(rh == null)
                rh = new Long(0);
            if(epidural == null)
                epidural = new Long(0);

        } catch (Exception e) {
            FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Childbirth Visit Controller Error",
                    "Childbirth Visit Controller Error");
            FacesContext.getCurrentInstance().addMessage(null, throwMsg);
        }
    }

    /**
     * Called when user clicks on the submit button. Takes data from form
     * and sends to ChildbirthVisitMySQLLoader.java for storage and validation
     */
    public void submit() {
        cv.setVisitDate(visitDate);
        cv.setLocationID(locationID);
        cv.setPatientMID(patientMID);
        cv.setPreScheduled(preScheduled);
        cv.setDeliveryType(deliveryType);
        cv.setPitocin(pitocin);
        cv.setEpidural(epidural);
        cv.setMagnesium(magnesium);
        cv.setRh(rh);
        cv.setOxide(oxide);
        cv.setPethidine(pethidine);
        if(noDrugs && (pitocin > 0 || epidural > 0 || magnesium > 0 || rh > 0 || oxide > 0 || pethidine > 0)) {
            controller.logTransaction(TransactionType.ADD_CHILDBIRTH_DRUGS, "");
        }
        if (isOfficeVisitCreated()) {
            controller.edit(cv);
            controller.logTransaction(TransactionType.EDIT_CHILDBIRTH_VISIT, "");
        } else {
            long pid = -1;

            FacesContext ctx = FacesContext.getCurrentInstance();

            String patientID = "";
            if (ctx != null && ctx.getExternalContext() != null && ctx.getExternalContext().getRequest() instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
                HttpSession httpSession = req.getSession(false);
                patientID = (String) httpSession.getAttribute("pid");
            }
            if (ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) {
                pid = Long.parseLong(patientID);
            }

            cv.setPatientMID(pid);
            cv.setVisitID(null);
            long generatedVisitId = controller.addReturnGeneratedId(cv);
            setVisitID(generatedVisitId);
            cv.setVisitID(generatedVisitId);
            controller.logTransaction(TransactionType.CREATE_CHILDBIRTH_VISIT, "");
            if(FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null && FacesContext.getCurrentInstance().getExternalContext().getSessionMap() != null)
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", generatedVisitId);

        }
        ExternalContext ctxx = null;
        if(FacesContext.getCurrentInstance() != null)
            ctxx = FacesContext.getCurrentInstance().getExternalContext();
        if (ctxx != null) {
            try {
                ctxx.redirect("/iTrust/auth/hcp-uap/viewOnlyObstetricsRecords.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * deletes childbirth visit from db and returns to view only records
     */
    public void delete() {
        controller.delete(visitID);
        ExternalContext ctxx = null;
        if(FacesContext.getCurrentInstance() != null)
            ctxx = FacesContext.getCurrentInstance().getExternalContext();
        if (ctxx != null) {
            try {
                ctxx.redirect("/iTrust/auth/hcp-uap/viewOnlyObstetricsRecords.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns true if childbirth visit is already in db
     * @return
     */
    public boolean isOfficeVisitCreated() {
        return (visitID != null) && (visitID > 0);
    }
}