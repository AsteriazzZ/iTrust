package edu.ncsu.csc.itrust.controller.childbirth;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.childbirth.Childbirth;
import edu.ncsu.csc.itrust.model.childbirth.ChildbirthData;
import edu.ncsu.csc.itrust.model.childbirth.ChildbirthMySQL;
//import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import java.util.ArrayList;

@ManagedBean(name = "childbirth_controller")
@SessionScoped
public class ChildbirthController extends iTrustController {

    private ChildbirthData childbirthData;
    private SessionUtils sessionUtils;

    public ChildbirthController() throws DBException { childbirthData = new ChildbirthMySQL(); sessionUtils = SessionUtils.getInstance(); }

    public ChildbirthController(DataSource ds) {
        childbirthData = new ChildbirthMySQL(ds);
        sessionUtils = SessionUtils.getInstance();
    }

    public ChildbirthController(DataSource ds, SessionUtils su) {
        childbirthData = new ChildbirthMySQL(ds);
        sessionUtils = su;
    }

    /**
     * adds childbirth record and returns true if successful
     * @param ov
     * @return
     */
    public boolean addReturnResult(Childbirth ov) {
        boolean res = false;

        try {
            res = childbirthData.add(ov);
        } catch (Exception e) {
            // do nothing
        }
//        if (res) {
//            logEditBasicHealthInformation();
//        }

        return res;
    }

    /**
     * adds childbirth record and returns generated birthID
     * @param ov
     * @return
     */
    public long addReturnGeneratedId(Childbirth ov) {
        long generatedId = -1;


        try {
            generatedId = childbirthData.addReturnGeneratedId(ov);
        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Create Childbirth Record", e.getExtendedMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Create Childbirth Record",
                    "Cannot Create Childbirth Record", null);
        }

        if (generatedId >= 0) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Created Childbirth Record",
                    "Created Childbirth Record", null);
//            logEditBasicHealthInformation();
        }

        return generatedId;
    }

    /**
     * prints face messages to front end of site with updates of db operations
     * @param severity
     * @param summary
     * @param detail
     * @param clientId
     */
    public void printFacesMessage(Severity severity, String summary, String detail, String clientId) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) {
            return;
        }
        ctx.getExternalContext().getFlash().setKeepMessages(true);
        ctx.addMessage(clientId, new FacesMessage(severity, summary, detail));
    }

    /**
     * adds childbirth record to db
     * @param ov
     */
    public void add(Childbirth ov) {
        boolean res = false;

        try {
            res = childbirthData.add(ov);
        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Create Childbirth Record", e.getExtendedMessage(),
                    null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Create Childbirth Record",
                    "Cannot Create Childbirth Record", null); }
        if (res) { printFacesMessage(FacesMessage.SEVERITY_INFO, "Created Childbirth Record",
                    "Created Childbirth Record", null);
//            logEditBasicHealthInformation();
        }
    }

    /**
     * gets childbirth records for certain visitID
     * @param visitID
     * @return
     */
    public List<Childbirth> getChildbirthsForVisit(String visitID) {
        List<Childbirth> ret = Collections.emptyList();
        long mid = -1;
        if ((visitID != null)) {
            mid = Long.parseLong(visitID);
            try {
                ret = childbirthData.getChildbirthsForVisit(mid).stream().sorted((o1, o2) -> {
                    return o2.getDate().compareTo(o1.getDate());
                }).collect(Collectors.toList());
            } catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Childbirths",
                        "Unable to Retrieve Childbirths", null);
            }
        }
        return ret;
    }

    /**
     * get all childbirth records for current visit
     * @return
     */
    public List<Childbirth> getChildbirthsForCurrentVisit() {
        return getChildbirthsForVisit(sessionUtils.getRequestParameter("visitID"));
    }

    /**
     * gets childbirth record corresponding to birthID
     * @param birthID
     * @return
     */
    public Childbirth getChildbirthByID(String birthID) {
        long id = -1;
        try {
            id = Long.parseLong(birthID);
        } catch (NumberFormatException ne) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Childbirth",
                    "Unable to Retrieve Childbirth", null);
            return null;
        }
        try {
            return childbirthData.getByID(id);
        } catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Childbirth",
                    "Unable to Retrieve Childbirth", null); return null;
        }
    }

    /**
     * gets childbirth record which is selected on front end
     * @return
     */
    public Childbirth getSelectedChildbirth() {
        String birthID = sessionUtils.getRequestParameter("birthID");
        if (birthID == null || birthID.isEmpty()) {
            return null;
        }
        return getChildbirthByID(birthID);
    }

    /**
     * gets all possible actual delivery types
     * @return
     */
    public List<String> getDeliveryTypes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Vaginal Delivery");
        list.add("Vaginal Delivery Vacuum Assist");
        list.add("Vaginal Delivery Forceps Assist");
        list.add("Caesarean Section");
        list.add("Miscarriage");
        return list;
    }

    /**
     * gets all possible sexes of child
     * @return
     */
    public List<String> getSexes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        return list;
    }

    /**
     * returns true if current selected visit has childbirths
     * @return
     */
    public boolean CurrentVisitHasChildbirths() {
        String visitID_ = sessionUtils.getRequestParameter("visitID");
        return (getChildbirthsForVisit(visitID_).size() > 0);
    }

    /**
     * updates childbirth record correpsonding to ov.birthID
     * @param ov
     */
    public void edit(Childbirth ov) {
        boolean res = false;

        try {
            res = childbirthData.update(ov); } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Update Childbirth Record", e.getExtendedMessage(),
                    null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Update Childbirth Record",
                    "Cannot Update Childbirth Record", null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Updated Childbirth Record",
                    "Updated Childbirth Record", null);
//            logEditBasicHealthInformation();
        }
    }

    /**
     * removes childbirth record in db corresponging to ov.birthID
     * @param ov
     */
    public void delete(Long ov) {
        boolean res = false;

        try {
            res = childbirthData.delete(ov);
        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Delete Childbirth Record", e.getExtendedMessage(),
                    null);
        } catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Delete Childbirth Record",
                    "Cannot Update Childbirth Record", null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Deleted Childbirth Record",
                    "Deleted Childbirth Record", null);
//            logEditBasicHealthInformation();
        }
    }
}

