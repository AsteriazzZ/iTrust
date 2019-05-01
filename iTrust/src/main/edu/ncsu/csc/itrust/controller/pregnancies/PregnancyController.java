package edu.ncsu.csc.itrust.controller.pregnancies;

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
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;
import edu.ncsu.csc.itrust.model.pregnancies.PregnancyData;
import edu.ncsu.csc.itrust.model.pregnancies.PregnancyMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "pregnancy_controller")
@SessionScoped
public class PregnancyController extends iTrustController {


    private PregnancyData pregnancyData;
    private SessionUtils sessionUtils;

    public PregnancyController() throws DBException {
        pregnancyData = new PregnancyMySQL();
        sessionUtils = SessionUtils.getInstance(); }

    /**
     * For testing purposes
     *
     * @param ds
     *            DataSource
     * @param sessionUtils
     *            SessionUtils instance
     */
    public PregnancyController(DataSource ds, SessionUtils sessionUtils) {
        pregnancyData = new PregnancyMySQL(ds);
        this.sessionUtils = sessionUtils;
    }

    public boolean addReturnResult(Pregnancy p) {
        boolean res = false;

        try {
            res = pregnancyData.add(p);
        } catch (Exception e) {
            // do nothing
        }
        if (res) {
//            logEditBasicHealthInformation();
        }

        return res;
    }

    public long addReturnGeneratedId(Pregnancy p) {
        long generatedId = -1;

        try {
            generatedId = pregnancyData.addReturnGeneratedId(p);
            System.out.println("genereated id " + generatedId);
        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_INFO, "DB Error Adding Pregnancy", e.getExtendedMessage(), null); }
        catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Exception adding pregnancy",
                    e.getMessage(), null);
        }

        if (generatedId >= 0) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Pregnancy Added Successfully",
                    "Pregnancy Added Successfully", null);
//            logEditBasicHealthInformation();
        }

        return generatedId;
    }

    /**
     * Sends a FacesMessage for FacesContext to display.
     *
     * @param severity
     *            severity of the message
     * @param summary
     *            localized summary message text
     * @param detail
     *            localized detail message text
     * @param clientId
     *            The client identifier with which this message is associated
     *            (if any)
     */
    public void printFacesMessage(Severity severity, String summary, String detail, String clientId) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) { return; }
        ctx.getExternalContext().getFlash().setKeepMessages(true);
        ctx.addMessage(clientId, new FacesMessage(severity, summary, detail)); }

//    public void redirectToBaseOfficeVisit() throws IOException {
//        if (FacesContext.getCurrentInstance() != null) {
//            NavigationController.baseOfficeVisit();
//        }
//    }

    public void add(Pregnancy p) {
        boolean res = false;
        System.out.println();
        try {
            res = pregnancyData.add(p);
        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "DB Error Adding Pregnancy", e.getExtendedMessage(),null); }
        catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding pregnancy", e.getMessage(), null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Pregnancy Added Successfully",
                    "Pregnancy Added Successfully", null);
//            logEditBasicHealthInformation();
        }
    }

    /**
     * @param pid
     *            patient mid
     * @return sorted list of prior pregnancies for the given patient
     */
    public List<Pregnancy> getPregnanciesForPatient(String pid) {
        List<Pregnancy> ret = Collections.emptyList();
        long mid = -1;
        System.out.println();
        if ((pid != null) && ValidationFormat.NPMID.getRegex().matcher(pid).matches()) {
            mid = Long.parseLong(pid);
            try {
                ret = pregnancyData.getPregnanciesForPatient(mid).stream().sorted((o1, o2) -> {
                    return o2.getConceptionYear().compareTo(o1.getConceptionYear());
                }).collect(Collectors.toList());
            } catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve prior pregnancies", e.getMessage(), null);
            }
        }
        return ret;
    }

    /**
     * @return list of office visit sorted by date, empty list if no office
     *         visit exists
     */
    public List<Pregnancy> getPregnanciesForCurrentPatient() {
        List<Pregnancy> list = getPregnanciesForPatient(sessionUtils.getCurrentPatientMID());
        return list;
    }

    /**
     * @return true if patient selected in HCP session has at least 1 prior pregnancy
     *         , false if otherwise
     */
    public boolean currentPatientHasPregnancyVisit() {
        boolean check = getPregnanciesForCurrentPatient().size() > 0;
        return check;
    }

    public Pregnancy getVisitByID(String VisitID) {
        long id = -1;
        try {
            id = Long.parseLong(VisitID);
        }
        catch (NumberFormatException ne) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Prior Pregnancy", "Unable to Retrieve Prior Pregnancy", null);
            return null;
        }
        try {
            return pregnancyData.getByID(id);
        } catch (Exception e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Prior Pregnancy", "Unable to Retrieve Prior Pregnancy", null);
            return null;
        }
    }

    /**
     * @return Pregnancy of the selected patient in the HCP session
     */
    public Pregnancy getSelectedPregnancy() {
        Pregnancy ret = null;
        String pregnancyID = sessionUtils.getRequestParameter("pregnancyID");
        if (pregnancyID == null || pregnancyID.isEmpty()) {
            return null;
        }
        return getVisitByID(pregnancyID);
    }


    public void edit(Pregnancy p) {
        boolean res = false;

        try {
            res = pregnancyData.update(p);
        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating pregnancy", e.getExtendedMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating pregnancy",
                    e.getMessage(), null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Updated Prior Pregnancy Successfully",
                    "Updated Prior Pregnancy Successfully", null);
//            logEditBasicHealthInformation();
        }
    }



    /**
     * Logs that the currently selected office visit has been viewed (if any
     * office visit is selected)
     */
//    public void logViewOfficeVisit() {
//        Long id = getSessionUtils().getCurrentOfficeVisitId();
//        if (id != null) {
//            logTransaction(TransactionType.OFFICE_VISIT_VIEW, id.toString());
//            OfficeVisit ov = getVisitByID(Long.toString(id));
//            long patientMID = ov.getPatientMID();
//            LocalDateTime d = ov.getDate();
//            logTransaction(TransactionType.VIEW_BASIC_HEALTH_METRICS, "Age: " + calculatePatientAge(patientMID, d));
//        }
//    }

    /**
     * Logs that the current user viewed a patient's health metrics page
     */
//    public void logViewHealthMetrics(){
//        String role = sessionUtils.getSessionUserRole();
//        if ("hcp".equals(role)){
//            logTransaction(TransactionType.HCP_VIEW_BASIC_HEALTH_METRICS, "");
//        } else if ("patient".equals(role)){
//            logTransaction(TransactionType.PATIENT_VIEW_BASIC_HEALTH_METRICS, Long.parseLong(sessionUtils.getSessionLoggedInMID()), null, "");
//        }
//    }

//    public void logViewBasicHealthInformation() {
//        logTransaction(TransactionType.PATIENT_HEALTH_INFORMATION_VIEW, "");
//    }

    /**
     * Editing basic health information is synonymous with editing or adding an
     * office visit, so this method should be called whenever an OV is
     * added/edited.
     */
//    private void logEditBasicHealthInformation() {
//        logTransaction(TransactionType.PATIENT_HEALTH_INFORMATION_EDIT, "");
//    }
}
