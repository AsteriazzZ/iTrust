package edu.ncsu.csc.itrust.controller.ultrasounds;


import edu.ncsu.csc.itrust.model.ultrasounds.Ultrasound;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;


@ManagedBean(name="ultrasound_form")
public class UltrasoundForm {
    private UltrasoundController controller;
    private Ultrasound ultrasound;
    private Long ultrasoundID;
    private Integer obstetricOfficeVisitID;
    private Long patientMID;
    private Integer crl;
    private Integer bpd;
    private Integer hc;
    private Integer fl;
    private Integer ofd;
    private Integer ac;
    private Integer hl;
    private Integer efw;
    private Part img;

    private Integer obstetricID;

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

    public Long getUltrasoundID(){return ultrasoundID;}

    public void setUltrasoundID(Long ultrasoundID) {this.ultrasoundID = ultrasoundID;}

    public Integer getObstetricOfficeVisitID(){return obstetricOfficeVisitID;}

    public void setObstetricOfficeVisitID(Integer obstetricOfficeVisitID){this.obstetricOfficeVisitID = obstetricOfficeVisitID;}

    public Integer getCrl(){return crl;}

    public void setCrl(Integer crl){this.crl = crl;}

    public Integer getBpd() {return bpd;}

    public void setBpd(Integer bpd){this.bpd = bpd;}

    public Integer getHc(){return hc;}

    public void setHc(Integer hc){this.hc = hc;}

    public void setFl(Integer fl){this.fl = fl;}

    public Integer getFl(){return fl;}

    public Integer getOfd() {return ofd;}

    public void setOfd(Integer ofd) {
        this.ofd = ofd;
    }

    public Integer getAc() {return ac;}

    public void setAc(Integer ac) {
        this.ac = ac;
    }

    public Integer getHl() {return hl;}

    public void setHl(Integer hl) {
        this.hl = hl;
    }

    public Integer getEfw() {return efw;}

    public void setEfw(Integer efw) {
        this.efw = efw;
    }

    public void setImg(Part img) {
        this.img = img;
    }

    public Part getImg() {
        return img;
    }

    /**
     * Default constructor for UltrasoundForm.
     */

    public UltrasoundForm() {
        this( null);
    }

    /**
     * Constructor for testing purposes.
     */

    public UltrasoundForm(UltrasoundController ultrasoundController) {
        try {
            controller = (ultrasoundController == null) ? new UltrasoundController() : ultrasoundController;
            ultrasound = controller.getSelectedUltrasound();
            if (ultrasound == null) {
                ultrasound = new Ultrasound();
            }
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ultrasoundID", getUltrasoundID());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("visitID", controller.getObstetricOfficeVisitID());
            } catch (NullPointerException e) {
                // Do nothing
            }
            ultrasoundID = ultrasound.getUltrasoundID();
            patientMID = ultrasound.getPatientMID();
            if (patientMID == null) {
                patientMID = Long.parseLong(
                        (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
            }
            if (obstetricID==null){
                try {
                    obstetricID = Integer.parseInt(
                            (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("visitID"));
                }catch(Exception e)
                {
                    //do nothing
                }
            }
            obstetricOfficeVisitID = obstetricID;
            crl = ultrasound.getCrl();
            bpd = ultrasound.getBpd();
            hc = ultrasound.getHc();
            fl = ultrasound.getFl();
            ofd = ultrasound.getOfd();
            ac = ultrasound.getAc();
            hl = ultrasound.getHl();
            efw = ultrasound.getEfw();

        } catch (Exception e) {
            FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ultrasound Controller Error",
                    "Ultrasound Controller Error");
            if (FacesContext.getCurrentInstance()!=null)FacesContext.getCurrentInstance().addMessage(null, throwMsg);
        }
    }

    /**
     * Submit ultrasound to be stored in the ultrasounds SQL table
     */
    public void submit() {
        ultrasound.setUltrasoundID(ultrasoundID);
        ultrasound.setObstetricOfficeVisitID(obstetricOfficeVisitID);
        ultrasound.setPatientMID(patientMID);
        ultrasound.setCrl(crl);
        ultrasound.setBpd(bpd);
        ultrasound.setHc(hc);
        ultrasound.setFl(fl);
        ultrasound.setOfd(ofd);
        ultrasound.setAc(ac);
        ultrasound.setHl(hl);
        ultrasound.setEfw(efw);
        ultrasound.setImg(img);
        if (isUltrasoundCreated()) {
            controller.edit(ultrasound);
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
            ultrasound.setPatientMID(pid);
            ultrasound.setObstetricOfficeVisitID(obstetricOfficeVisitID);
            ultrasound.setUltrasoundID(null);
            long generatedUltrasoundID = controller.addReturnGeneratedId(ultrasound);
            setUltrasoundID(generatedUltrasoundID);
            ultrasound.setUltrasoundID(generatedUltrasoundID);
            controller.addUltrasoundImage(generatedUltrasoundID, ultrasound.getImg());
            controller.logTransaction(TransactionType.ULTRASOUND_UPDATE, ultrasound.getObstetricOfficeVisitID().toString());
            if (FacesContext.getCurrentInstance()!=null)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ultrasoundID", generatedUltrasoundID);

            if (FacesContext.getCurrentInstance() != null) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/iTrust/auth/hcp-uap/ultrasounds/viewUltrasounds.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @return true if ultrasound has an id
     */
    public boolean isUltrasoundCreated() {
        return (ultrasoundID != null) && (ultrasoundID > 0);
    }


}
