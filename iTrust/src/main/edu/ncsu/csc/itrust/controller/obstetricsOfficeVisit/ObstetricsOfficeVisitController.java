package edu.ncsu.csc.itrust.controller.obstetricsOfficeVisit;

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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import java.time.DayOfWeek;

import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.childbirthVisit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitData;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

@ManagedBean(name = "obstetrics_office_visit_controller")
@SessionScoped
public class ObstetricsOfficeVisitController extends iTrustController {

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

    private ObstetricsOfficeVisitData obstetricsOfficeVisitData;
    private ObstetricVisitController initializationController;
    private ChildbirthVisitController childbirthVisitController;
    private SessionUtils sessionUtils;

    /**
     * Constructor for ObstetricsOfficeVisitController
     */
    public ObstetricsOfficeVisitController() throws DBException {
        obstetricsOfficeVisitData = new ObstetricsOfficeVisitMySQL();
        initializationController = new ObstetricVisitController();
        childbirthVisitController = new ChildbirthVisitController();
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
    public ObstetricsOfficeVisitController(DataSource ds, SessionUtils sessionUtils) throws Exception{
            obstetricsOfficeVisitData = new ObstetricsOfficeVisitMySQL(ds);
            initializationController = new ObstetricVisitController(ds);
            childbirthVisitController = new ChildbirthVisitController(ds);
            this.sessionUtils = sessionUtils;
    }
    /**
     * For testing purposes
     *
     * @param ds
     *            DataSource
     */

    public ObstetricsOfficeVisitController(DataSource ds) throws Exception{
            obstetricsOfficeVisitData = new ObstetricsOfficeVisitMySQL(ds);
            initializationController = new ObstetricVisitController(ds);
            childbirthVisitController = new ChildbirthVisitController(ds);
        sessionUtils = SessionUtils.getInstance();
    }

    /**
     * Adding office visit used in testing.
     *
     * @param ov
     *            Office visit
     * @return true if successfully added, false if otherwise
     */
    public boolean addReturnResult(ObstetricsOfficeVisit ov) {
        boolean res = false;

        try {
            res = obstetricsOfficeVisitData.add(ov);
        } catch (Exception e) { }
        if (res) {
//            logEditBasicHealthInformation();
        }

        return res;
    }

    /**
     * Add office visit to the database and return the generated VisitID.
     *
     * @param ov
     *            Office visit to add to the database
     * @return VisitID generated from the database insertion, -1 if nothing was
     *         generated
     * @throws DBException
     *             if error occurred in inserting office visit
     */
    public long addReturnGeneratedId(ObstetricsOfficeVisit ov) {
        long generatedId = -1;

        try {
            generatedId = obstetricsOfficeVisitData.addReturnGeneratedId(ov);
//        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(), null);
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
        if (ctx == null) {
            return;
        }
        ctx.getExternalContext().getFlash().setKeepMessages(true);
        ctx.addMessage(clientId, new FacesMessage(severity, summary, detail));
    }

//    public void redirectToBaseOfficeVisit() throws IOException {
//        if (FacesContext.getCurrentInstance() != null) {
//            NavigationController.baseOfficeVisit();
//        }
//    }

    /**
     * Add office visit to the database.
     *
     * @param ov
     *            Office visit to add to the database
     */
    public void add(ObstetricsOfficeVisit ov) {
        boolean res = false;

        try {
            res = obstetricsOfficeVisitData.add(ov);
//        } catch (DBException e) { printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED, e.getExtendedMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_CREATED,
                    OFFICE_VISIT_CANNOT_BE_CREATED, null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_CREATED,
                    OFFICE_VISIT_SUCCESSFULLY_CREATED, null);
            //logEditBasicHealthInformation();
        }
    }

    /**
     * @param pid
     *            patient mid
     * @return sorted list of office visit for the given patient
     */
    public List<ObstetricsOfficeVisit> getObstetricsOfficeVisitsForPatient(String pid) {
        List<ObstetricsOfficeVisit> ret = Collections.emptyList();
        long mid = -1;
        if ((pid != null) && ValidationFormat.NPMID.getRegex().matcher(pid).matches()) {
            mid = Long.parseLong(pid);
            try {
                ret = obstetricsOfficeVisitData.getObstetricsOfficeVisitsForPatient(mid).stream().sorted((o1, o2) -> {
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
     * @return list of office visit sorted by date, empty list if no office
     *         visit exists
     */
    public List<ObstetricsOfficeVisit> getObstetricsOfficeVisitsForCurrentPatient() {
        return getObstetricsOfficeVisitsForPatient(sessionUtils.getCurrentPatientMID());
    }

    /**
     * @param VisitID
     *            obstetrics office visit id
     * @return ObstetricsOfficeVisit with given id
     */
    public ObstetricsOfficeVisit getVisitByID(String VisitID) {
        long id = -1;
        try {
            id = Long.parseLong(VisitID);
        } catch (NumberFormatException ne) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
                    "Unable to Retrieve Office Visit", null);
            return null;
        }
        try {
            return obstetricsOfficeVisitData.getByID(id);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Office Visit",
                    "Unable to Retrieve Office Visit", null);
            return null;
        }
    }

    /**
     * @return Office Visit of the selected patient in the HCP session
     */
    public ObstetricsOfficeVisit getSelectedVisit() {
        String visitID = sessionUtils.getRequestParameter("visitID");
        if (visitID == null || visitID.isEmpty()) {
            return null;
        }
        return getVisitByID(visitID);
    }

    /**
     * @param patientID
     *            Patient MID
     * @return true if selected patient MID has at least 1 office visit, false
     *         otherwise
     */
    public boolean hasPatientVisited(String patientID) {
        boolean ret = false;
        if ((patientID != null) && (ValidationFormat.NPMID.getRegex().matcher(patientID).matches())) {
            if (getObstetricsOfficeVisitsForPatient(patientID).size() > 0) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * @return true if patient selected in HCP session has at least 1 office
     *         visit, false if otherwise
     */
    public boolean CurrentPatientHasVisited() {
        return hasPatientVisited(sessionUtils.getCurrentPatientMID());
    }

    public void edit(ObstetricsOfficeVisit ov) {
        boolean res = false;

        try {
            res = obstetricsOfficeVisitData.update(ov);
//        } catch (DBException e) {
//            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_UPDATED, e.getExtendedMessage(),
//                    null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, OFFICE_VISIT_CANNOT_BE_UPDATED,
                    OFFICE_VISIT_CANNOT_BE_UPDATED, null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, OFFICE_VISIT_SUCCESSFULLY_UPDATED,
                    OFFICE_VISIT_SUCCESSFULLY_UPDATED, null);
 //           logEditBasicHealthInformation();
        }
    }

    /**
     * Checks if current patient needs an RH Shot
     * @return true if patient needs the shot
     * @throws DBException
     */
    public boolean CurrentPatientNeedsRHShot() throws DBException{
        boolean ret = false;
        String patientMID = sessionUtils.getCurrentPatientMID();
        Long pid = Long.parseLong(patientMID);
        if (initializationController.getObstetricVisitsForPatient(patientMID).size() > 0){
            Long weeksPregnant = initializationController.getObstetricVisitsForPatient(patientMID).get(0).getWeeksPregnant();
//            String bloodType = obstetricsOfficeVisitData.getPatient(pid).getBloodType();
            Patient p = obstetricsOfficeVisitData.getPatient(pid);
            if (p == null){
                return false;
            }
            String bloodType = p.getBloodType();
            if (bloodType.contains("-") && weeksPregnant > 28){
                ret = true;
            }
        }

        return ret;
    }

    /**
     * Automatically schedules the next obstetrics office visit
     * or child birth visit based on how many weeks pregnant
     * the current patient is
     * @throws Exception
     */
    public void scheduleNextVisit() throws Exception{
        String patientMID = sessionUtils.getCurrentPatientMID();
//        String weeksPreg = sessionUtils.getCurrentFacesContext().getExternalContext().getRequestParameterMap().get("weeksPregnant");
//        String currentOfficeVisitID = sessionUtils.getCurrentFacesContext().getExternalContext().getRequestParameterMap().get("currentVisitID");
        String weeksPreg = getWeeksPregnantFacesContext();
        String currentOfficeVisitID = getOfficeVisitIDFacesContext();

        Long weeksPregnant = Long.parseLong(weeksPreg);

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime nextAppointment = null;

        boolean childBirthVisit = false;

        if (weeksPregnant >= 0 && weeksPregnant < 14){
            //Monthly
            nextAppointment = today.plusMonths(1);

        }else if(weeksPregnant >= 14 && weeksPregnant < 29){
            //Every 2 weeks
            nextAppointment = today.plusWeeks(2);

        } else if(weeksPregnant >= 29 && weeksPregnant < 40){
            //Weekly
            nextAppointment = today.plusWeeks(1);

        } else if(weeksPregnant >=40 && weeksPregnant < 43){
            //Every other weekday
            nextAppointment = today.plusDays(2);
            if (weeksPregnant == 42){
                childBirthVisit = true;
            }

        }else{
            String message = "Unable to automatically schedule next visit for patient that is " + weeksPregnant + " weeks pregnant.";
            printFacesMessage(FacesMessage.SEVERITY_INFO, message,
                    message, null);
            return;
        }

        DayOfWeek day = nextAppointment.getDayOfWeek();
        //Make sure next appointment is on a weekday
        if (day.equals(DayOfWeek.SATURDAY)){
            nextAppointment = nextAppointment.plusDays(2);
        }else if (day.equals(DayOfWeek.SUNDAY)){
            nextAppointment = nextAppointment.plusDays(1);
        }

        //Set appointment to 9:00 am
        nextAppointment = nextAppointment.withHour(9);
        nextAppointment = nextAppointment.withMinute(0);
        nextAppointment = nextAppointment.withSecond(0);

        if (childBirthVisit){
            //Childbirth Visit
            ChildbirthVisit nextVisit = new ChildbirthVisit();
            nextVisit.setPatientMID(Long.parseLong(patientMID));
            nextVisit.setVisitDate(nextAppointment);
            nextVisit.setLocationID("1");
            nextVisit.setDeliveryType(childbirthVisitController.getDeliveryTypes().get(0));
            nextVisit.setEpidural(0L);
            nextVisit.setMagnesium(0L);
            nextVisit.setOxide(0L);
            nextVisit.setPethidine(0L);
            nextVisit.setPitocin(0L);
            nextVisit.setRh(0L);
            nextVisit.setPreScheduled(false);

            Long newVisitID = childbirthVisitController.addReturnGeneratedId(nextVisit);
            logTransaction(TransactionType.SCHEDULE_NEXT_CHILDBIRTH_OFFICE_VISIT, "Current Obstetric Initialization Visit ID:" + currentOfficeVisitID + ", Next Childbirth Office Visit ID:" + newVisitID);
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            if (ctx != null) {
                ctx.redirect("/iTrust/auth/hcp-uap/viewChildbirthVisitRecord.xhtml?visitID=" + newVisitID);
            }

        }else{
            //Obstetric Office Visit
            ObstetricsOfficeVisit nextVisit = new ObstetricsOfficeVisit();
            nextVisit.setDate(nextAppointment);
            nextVisit.setPatientMID(Long.parseLong(patientMID));

            Long newVisitID = obstetricsOfficeVisitData.addReturnGeneratedId(nextVisit);
            logTransaction(TransactionType.SCHEDULE_NEXT_OBSTETRICS_OFFICE_VISIT, "Current Obstetric Initialization Visit ID:" + currentOfficeVisitID + ", Next Obstetric Office Visit ID:" + newVisitID);
            ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
            if (ctx != null) {
                ctx.redirect("/iTrust/auth/hcp-uap/viewObstetricOfficeVisitRecord.xhtml?visitID=" + newVisitID);
            }
        }

    }

    /**
     *
     * @return weeks pregnant passed in from face context
     */
    public String getWeeksPregnantFacesContext(){
        return sessionUtils.getCurrentFacesContext().getExternalContext().getRequestParameterMap().get("weeksPregnant");
    }

    /**
     *
     * @return obstetric visit id passed in from face context
     */
    public String getOfficeVisitIDFacesContext(){
        return sessionUtils.getCurrentFacesContext().getExternalContext().getRequestParameterMap().get("currentVisitID");
    }

    /**
     * log for viewing an obstetric office visit
     */
    public void logViewObstetricOfficeVisit() {
        logTransaction(TransactionType.VIEW_OBSTETRIC_OFFICE_VISIT, "Obstetric Office Visit ID: " + getSelectedVisit().getVisitID().toString());
    }

    /**
     * log for creating an obstetric office visit
     */
    public void logCreateObstetricOfficeVisit(String id){
        logTransaction(TransactionType.CREATE_OBSTETRIC_OFFICE_VISIT, "Obstetric Office Visit ID: " + id);
    }

    /**
     * log for editing an obstetric office visit
     */
    public void logEditObstetricOfficeVisit(String id) {
        logTransaction(TransactionType.EDIT_OBSTETRIC_OFFICE_VISIT, "Obstetric Office Visit ID: " + id);
    }

}
