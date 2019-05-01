package edu.ncsu.csc.itrust.controller.childbirthVisit;

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
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisitData;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisitMySQL;
//import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import java.util.ArrayList;

/**
 * controller for childbirth visit records
 */
@ManagedBean(name = "childbirth_visit_controller")
@SessionScoped
public class ChildbirthVisitController extends iTrustController {

    /**
     * Constant for the error message to be displayed if the Office Visit is
     * invalid.
     */
    private static final String OFFICE_VISIT_CANNOT_BE_UPDATED = "Invalid Office Visit";

    /**
     * Constant for the message to be displayed if the office visit was
     * unsuccessfully updated
     */
    private static final String OFFICE_VISIT_CANNOT_BE_CREATED = "Office Visit Cannot Be Updated";

    /**
     * Constant for the message to be displayed if the office visit was
     * successfully created
     */
    private static final String OFFICE_VISIT_SUCCESSFULLY_CREATED = "Office Visit Successfully Created";

    /**
     * Constant for the message to be displayed if the office visit was
     * successfully updated
     */
    private static final String OFFICE_VISIT_SUCCESSFULLY_UPDATED = "Office Visit Successfully Updated";

    private ChildbirthVisitData childbirthVisitData;
    private SessionUtils sessionUtils;

    public ChildbirthVisitController() throws DBException {
        childbirthVisitData = new ChildbirthVisitMySQL();
        sessionUtils = SessionUtils.getInstance();
    }

    public ChildbirthVisitController(DataSource ds) {
        childbirthVisitData = new ChildbirthVisitMySQL(ds);
        this.sessionUtils = SessionUtils.getInstance();
    }

    public ChildbirthVisitController(DataSource ds, SessionUtils su) {
        childbirthVisitData = new ChildbirthVisitMySQL(ds);
        sessionUtils = su;
    }

    /**
     * Adding office visit used in testing.
     *
     * @param ov
     *            Office visit
     * @return true if successfully added, false if otherwise
     */
    public boolean addReturnResult(ChildbirthVisit ov) {
        boolean res = false;
        try {
            res = childbirthVisitData.add(ov);
        } catch (Exception e) {
            // do nothing
        }
//        if (res) {
//            logEditBasicHealthInformation();
//        }

        return res;
    }

    /**
     * adds childbirth visit record and returns generated visitID
     * @param ov
     * @return
     */
    public long addReturnGeneratedId(ChildbirthVisit ov) {
        long generatedId = -1;


        try {
            generatedId = childbirthVisitData.addReturnGeneratedId(ov); } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED,
                    OFFICE_VISIT_CANNOT_BE_CREATED, null);
        }

        if (generatedId >= 0) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_CREATED,
                    OFFICE_VISIT_SUCCESSFULLY_CREATED, null);
//            logEditBasicHealthInformation();
        }

        return generatedId;
    }

    /**
     * prints face messages to front end based off severity and message
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
     * adds childbirth visit record to db
     * @param ov
     */
    public void add(ChildbirthVisit ov) {
        boolean res = false;

        try {
            res = childbirthVisitData.add(ov);
        } catch (DBException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(),
                    null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED,
                    OFFICE_VISIT_CANNOT_BE_CREATED, null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_CREATED,
                    OFFICE_VISIT_SUCCESSFULLY_CREATED, null);
//            logEditBasicHealthInformation();
        }
    }

    /**
     * gets all childbirth visit records for certain patient
     * @param pid
     * @return
     */
    public List<ChildbirthVisit> getChildbirthVisitsForPatient(String pid) {
        List<ChildbirthVisit> ret = Collections.emptyList();
        long mid = -1;
        if ((pid != null) && ValidationFormat.NPMID.getRegex().matcher(pid).matches()) {
            mid = Long.parseLong(pid);
            try {
                ret = childbirthVisitData.getChildbirthVisitsForPatient(mid).stream().sorted((o1, o2) -> {
                    return o2.getVisitDate().compareTo(o1.getVisitDate());
                }).collect(Collectors.toList());
            } catch (Exception e) {
                printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visits",
                        "Unable to Retrieve Office Visits", null);
            }
        }
        return ret;
    }

    /**
     * gets all childbirth visit records for current patient
     * @return
     */
    public List<ChildbirthVisit> getChildbirthVisitsForCurrentPatient() {
        return getChildbirthVisitsForPatient(sessionUtils.getCurrentPatientMID());
    }

    /**
     * gets childbirth visit corresponding to visitID
     * @param VisitID
     * @return
     */
    public ChildbirthVisit getVisitByID(String VisitID) {
        long id = -1;
        try {
            id = Long.parseLong(VisitID);
        } catch (NumberFormatException ne) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
                    "Unable to Retrieve Office Visit", null);
            return null;
        }
        try {
            return childbirthVisitData.getByID(id); } catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
                    "Unable to Retrieve Office Visit", null);
            return null;
        }
    }

    /**
     * gets current selected childbirth visit
     * @return
     */
    public ChildbirthVisit getSelectedVisit() {
        String visitID = sessionUtils.getRequestParameter("visitID");
        if (visitID == null || visitID.isEmpty()) {
            return null;
        }
        return getVisitByID(visitID);
    }

    /**
     * returns true if patient has childbirth visit
     * @param patientID
     *            Patient MID
     * @return true if selected patient MID has at least 1 office visit, false
     *         otherwise
     */
    public boolean hasPatientVisited(String patientID) {
        boolean ret = false;
        if ((patientID != null) && (ValidationFormat.NPMID.getRegex().matcher(patientID).matches())) {
            if (getChildbirthVisitsForPatient(patientID).size() > 0) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * gets all types of delivery types
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
     * @return true if patient selected in HCP session has at least 1 office
     *         visit, false if otherwise
     */
    public boolean CurrentPatientHasChildbirthVisit() {
        return hasPatientVisited(sessionUtils.getCurrentPatientMID());
    }

    /**
     * edits childbirth visit record
     * @param ov
     */
    public void edit(ChildbirthVisit ov) {
        boolean res = false;

        try {
            res = childbirthVisitData.update(ov);
        } catch (DBException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_UPDATED, e.getExtendedMessage(),
                    null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_UPDATED,
                    OFFICE_VISIT_CANNOT_BE_UPDATED, null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_UPDATED,
                    OFFICE_VISIT_SUCCESSFULLY_UPDATED, null);
//            logEditBasicHealthInformation();
        }
    }

    /**
     * deletes childbirth visit record
     * @param visitID
     */
    public void delete(Long visitID) {
        boolean res = false;

        try {
            res = childbirthVisitData.delete(visitID);
        } catch (DBException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Delete Childbirth Record", e.getExtendedMessage(),
                    null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Delete Childbirth Record",
                    OFFICE_VISIT_CANNOT_BE_UPDATED, null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Deleted Childbirth Record",
                    "Deleted Childbirth Record", null);
//            logEditBasicHealthInformation();
        }
    }
}