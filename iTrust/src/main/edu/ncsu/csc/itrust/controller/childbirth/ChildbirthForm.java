package edu.ncsu.csc.itrust.controller.childbirth;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import edu.ncsu.csc.itrust.controller.childbirthVisit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.childbirth.Childbirth;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "childbirth_form")
@ViewScoped
public class ChildbirthForm {
    private ChildbirthController controller;
    private Childbirth cv;
    private Long birthID;
    private Long visitID;
    private LocalDateTime date;
    private String deliveryType;
    private String sex;
    private String name;

    /**
     *
     * @return birthID
     */
    public Long getBirthID() {
        return birthID;
    }

    /**
     * sets birthID
     * @param birthID_
     */
    public void setBirthID(Long birthID_) {
        this.birthID = birthID_;
    }
    /**
     *
     * @return visitID
     */
    public Long getVisitID() {
        return visitID;
    }

    /**
     * sets visitID
     * @param visitID_
     */
    public void setVisitID(Long visitID_) {
        this.visitID = visitID_;
    }
    /**
     *
     * @return Date
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * sets Date
     * @param date_
     */
    public void setDate(LocalDateTime date_) {
        this.date = date_;
    }
    /**
     *
     * @return DeliveryType
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * sets deliveryType
     * @param delivery
     */
    public void setDeliveryType(String delivery) {
        this.deliveryType = delivery;
    }
    /**
     *
     * @return Sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * sets Sex
     * @param sex_
     */
    public void setSex(String sex_) {
        this.sex = sex_;
    }
    /**
     *
     * @return Name
     */
    public String getName() {return name; }

    /**
     * sets Name
     * @param name_
     */
    public void setName(String name_) { this.name = name_; }

    public ChildbirthForm() { this(null); }

    /**
     * form constructor which creates new controller if passed in null, or loads pre-existing data if record exists already
     * @param ovc
     */
    public ChildbirthForm(ChildbirthController ovc) {
        try {
//            visitController = new ChildbirthVisitController();
            controller = (ovc == null) ? new ChildbirthController() : ovc;
            cv = controller.getSelectedChildbirth();
            if (cv == null) {
                cv = new Childbirth();
            }
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("birthID", cv.getBirthID());
            } catch (NullPointerException e) {
            }
            birthID = cv.getBirthID();
            visitID = cv.getVisitID();
            date = cv.getDate();
            sex = cv.getSex();
            deliveryType = cv.getDeliveryType();
            name = cv.getName();


        } catch (Exception e) {
            FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Childbirth Controller Error",
                    "Childbirth Controller Error");
            FacesContext.getCurrentInstance().addMessage(null, throwMsg);
        }
    }

    /**
     * called when user submits update on childbirth records, reloads or loads new parameters and executes proper query
     */
    public void submit() {
        cv.setVisitID(visitID);
        cv.setDate(date);
        cv.setSex(sex);
        cv.setDeliveryType(deliveryType);
        cv.setName(name);
        if (isChildbirthCreated()) {
            controller.edit(cv);
//            controller.logTransaction(TransactionType.OFFICE_VISIT_EDIT, cv.getVisitID().toString());
        } else {
            long vID = -1;

            FacesContext ctx = FacesContext.getCurrentInstance();


            String currVisitID = "";
            if (ctx != null && ctx.getExternalContext() != null && ctx.getExternalContext().getRequest() instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
                HttpSession httpSession = req.getSession(false);
                currVisitID = (String) httpSession.getAttribute("visitID");
            }

            if (ValidationFormat.NPMID.getRegex().matcher(currVisitID).matches()) {
                vID = Long.parseLong(currVisitID);
            }

            cv.setVisitID(vID);
            cv.setBirthID(null);
            long generatedBirthId = controller.addReturnGeneratedId(cv);
            setBirthID(generatedBirthId);
            cv.setBirthID(generatedBirthId);
            controller.logTransaction(TransactionType.BABY_BORN, "");
            if(FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null && FacesContext.getCurrentInstance().getExternalContext().getSessionMap() != null)
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("childbirthID", generatedBirthId);
        }
        ExternalContext ctxx = null;
        if(FacesContext.getCurrentInstance() != null) { ctxx = FacesContext.getCurrentInstance().getExternalContext(); }
        if (ctxx != null) {
            try {
                ctxx.redirect("/iTrust/auth/hcp-uap/viewChildbirthVisitRecord.xhtml?visitID=" + cv.getVisitID()); } catch (IOException e) { e.printStackTrace();
            }
        }

    }

    /**
     * removes birthID childbirth record from db
     */
    public void delete() {
        controller.delete(birthID);
        ExternalContext ctxx = null;
        if(FacesContext.getCurrentInstance() != null)
            ctxx = FacesContext.getCurrentInstance().getExternalContext();
        if (ctxx != null) {
            try {
                ctxx.redirect("/iTrust/auth/hcp-uap/viewChildbirthVisitRecord.xhtml?visitID=" + cv.getVisitID()); } catch (IOException e) { e.printStackTrace();
            }
        }
    }

    /**
     * returns true if childbirth of form has been created in db
     * @return
     */
    public boolean isChildbirthCreated() {
        return (birthID != null) && (birthID > 0);
    }
}
