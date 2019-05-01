package edu.ncsu.csc.itrust.model.ultrasounds;

/**
 *
 */

import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import java.awt.Image;
import java.io.File;

/**
 * @author seelder
 *
 */

@ManagedBean(name="ultrasound")
public class Ultrasound {
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
//    private File img;
    private Part img;
    /**
     * Default constructor for Ultrasound
     */
    public Ultrasound(){
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
}
