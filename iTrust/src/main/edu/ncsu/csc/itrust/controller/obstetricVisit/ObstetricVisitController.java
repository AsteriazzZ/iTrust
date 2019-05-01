package edu.ncsu.csc.itrust.controller.obstetricVisit;


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
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitData;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obstetric_visit_controller")
@SessionScoped
public class ObstetricVisitController extends iTrustController {

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

    private ObstetricVisitData obstetricVisitData;
    private SessionUtils sessionUtils;

    /**
     * Constructor for ObstetricVisitController
     * @throws DBException
     */
    public ObstetricVisitController() throws DBException {
        obstetricVisitData = new ObstetricVisitMySQL();
        sessionUtils = SessionUtils.getInstance();
    }

    /**
     * For testing purposes
     *
     * @param ds
     *            DataSource
     * @param sessionUtils
     *            SessionUtils instance
     */
    public ObstetricVisitController(DataSource ds, SessionUtils sessionUtils) {
        obstetricVisitData = new ObstetricVisitMySQL(ds);
        this.sessionUtils = sessionUtils;
    }

    /**
     * For testing purposes
     *
     * @param ds
     *            DataSource
     */
    public ObstetricVisitController(DataSource ds) {
        obstetricVisitData = new ObstetricVisitMySQL(ds);
        this.sessionUtils = SessionUtils.getInstance();
    }

    /**
     * Adding obstetric visit used in testing.
     *
     * @param ov Obstetric visit
     * @return true if successfully added, false if otherwise
     */
    public boolean addReturnResult(ObstetricVisit ov) {
        boolean res = false;

        try {
            res = obstetricVisitData.add(ov);
        } catch (Exception e) {
            // do nothing
        }
        if (res) {
//            logEditBasicHealthInformation();
        }

        return res;
    }

    /**
     * Add obstetric visit to the database and return the generated VisitID.
     *
     * @param ov obstetric visit to add to the database
     * @return VisitID generated from the database insertion, -1 if nothing was
     * generated
     * @throws DBException if error occurred in inserting obstetric visit
     */
    public long addReturnGeneratedId(ObstetricVisit ov) {
        long generatedId = -1;

        try {
            generatedId = obstetricVisitData.addReturnGeneratedId(ov);
        } catch (DBException e) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_CANNOT_BE_CREATED,
                    OFFICE_VISIT_CANNOT_BE_CREATED, null);
        }

        if (generatedId >= 0) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_CREATED,
                    OFFICE_VISIT_SUCCESSFULLY_CREATED, null);
        }

        return generatedId;
    }

    /**
     * Sends a FacesMessage for FacesContext to display.
     *
     * @param severity severity of the message
     * @param summary  localized summary message text
     * @param detail   localized detail message text
     * @param clientId The client identifier with which this message is associated
     *                 (if any)
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
     * Redirects to Base Visit
     * @throws IOException
     */
    public void redirectToBaseOfficeVisit() throws IOException {
        if (FacesContext.getCurrentInstance() != null) {
            NavigationController.baseOfficeVisit();
        }
    }

    /**
     * Add Obstetric Visit
     *
     * @param ov
     */
    public void add(ObstetricVisit ov) {
        boolean res = false;

        try {
            res = obstetricVisitData.add(ov);
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
     * @param pid patient mid
     * @return sorted list of obstetric visit for the given patient
     */
    public List<ObstetricVisit> getObstetricVisitsForPatient(String pid) {
        List<ObstetricVisit> ret = Collections.emptyList();
        long mid = -1;
        if ((pid != null) && ValidationFormat.NPMID.getRegex().matcher(pid).matches()) {
            mid = Long.parseLong(pid);
            try {
                ret = obstetricVisitData.getObstetricVisitsForPatient(mid).stream().sorted((o1, o2) -> {
                    return o2.getDate().compareTo(o1.getDate());
                }).collect(Collectors.toList());
            } catch (Exception e) {
                printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visits",
                        "Unable to Retrieve Office Visits", null);
            }
        }
        return ret;
    }

    /**
     * @return list of obstetric visit sorted by date, empty list if no obstetric
     * visit exists
     */
    public List<ObstetricVisit> getObstetricVisitsForCurrentPatient() {
        return getObstetricVisitsForPatient(sessionUtils.getCurrentPatientMID());
    }

    /**
     * Get Obstetric Visit using given VisitID
     *
     * @param VisitID
     * @return Obstetric Visit with selected VisitID, null if no obstetric visit
     */
    public ObstetricVisit getVisitByID(String VisitID) {
        long id = -1;
        try {
            id = Long.parseLong(VisitID);
        } catch (NumberFormatException ne) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
                    "Unable to Retrieve Office Visit", null);
            return null;
        }
        try {
            return obstetricVisitData.getByID(id);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
                    "Unable to Retrieve Office Visit", null);
            return null;
        }
    }

    /**
     * @return Obstetric Visit of the selected patient in the HCP session
     */
    public ObstetricVisit getSelectedVisit() {
        String visitID = sessionUtils.getRequestParameter("visitID");
        if (visitID == null || visitID.isEmpty()) {
            return null;
        }
        return getVisitByID(visitID);
    }

    /**
     * @param patientID Patient MID
     * @return true if selected patient MID has at least 1 obstetric visit, false
     * otherwise
     */
    public boolean hasPatientVisited(String patientID) {
        boolean ret = false;
        if ((patientID != null) && (ValidationFormat.NPMID.getRegex().matcher(patientID).matches())) {
            if (getObstetricVisitsForPatient(patientID).size() > 0) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     *
     * @param patientMID
     * @return Date of Birth of a patient
     */
    public LocalDate getPatientDOB(final Long patientMID) {
        return obstetricVisitData.getPatientDOB(patientMID);
    }



    /**
     * @return true if patient selected in HCP session has at least 1 obstetric
     * visit, false if otherwise
     */
    public boolean CurrentPatientHasObstetricVisit() {
        return hasPatientVisited(sessionUtils.getCurrentPatientMID());
    }

    public void edit(ObstetricVisit ov) {
        boolean res = false;

        try {
            res = obstetricVisitData.update(ov);
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
        }
    }

    /**
     * Logs that the currently selected obstetric visit has been viewed (if any
     * obstetric visit is selected)
     */
    public void logViewObstetricVisit() {
        String edd = getSelectedVisit().getEDD().toString();
        logTransaction(TransactionType.OBSTETRICS_RECORD_VIEW, "Expected Due Date: " + edd);
    }

    /**
     * Logs that a obstetric visit has been created
     *
     * @param edd
     */
    public void logCreateObstetricVisit(String edd){
        logTransaction(TransactionType.OBSTETRICS_RECORD_CREATE, "Expected Due Date: " + edd);
    }

}
