package edu.ncsu.csc.itrust.controller.ultrasounds;


import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;

import java.util.Base64;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;


import com.sun.jna.platform.win32.WinDef;
import edu.ncsu.csc.itrust.controller.NavigationController;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.ultrasounds.Ultrasound;
import edu.ncsu.csc.itrust.model.ultrasounds.UltrasoundData;
import edu.ncsu.csc.itrust.model.ultrasounds.UltrasoundMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import org.apache.commons.io.IOUtils;
import javax.sql.DataSource;

@ManagedBean(name = "ultrasound_controller")
@SessionScoped
public class UltrasoundController extends iTrustController {


    private UltrasoundData ultrasoundData;
    private SessionUtils sessionUtils;

    public UltrasoundController() throws DBException {
        ultrasoundData = new UltrasoundMySQL();
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
    public UltrasoundController(DataSource ds, SessionUtils sessionUtils) {
        ultrasoundData = new UltrasoundMySQL(ds);
        this.sessionUtils = sessionUtils;
    }

    public UltrasoundController(DataSource ds) {
        ultrasoundData = new UltrasoundMySQL(ds);
        sessionUtils = SessionUtils.getInstance();
    }

    /**
     *
     * @param u ultrasound to add to storage
     * @return true if ultrasound is added correctly
     */
    public boolean addReturnResult(Ultrasound u) {
        boolean res = false;

        try {
            res = ultrasoundData.add(u);
        } catch (Exception e) {
        }
        if (res) {
//            logEditBasicHealthInformation();
        }

        return res;
    }

    /**
     *
     * @param u ultrasound to be stored
     * @return id of ultrasound stored
     */
    public long addReturnGeneratedId(Ultrasound u) {
        long generatedId = -1;

        try {
            generatedId = ultrasoundData.addReturnGeneratedId(u);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Exception adding ultrasound", e.getMessage(), null);
        }

        if (generatedId >= 0) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Ultrasound Added Successfully",
                    "Ultrasound Added Successfully", null);
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


    /**
     *
     * @param u ultrasound to be stored
     */
    public void add(Ultrasound u) {
        boolean res = false;

        try {
            res = ultrasoundData.add(u);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding Ultrasound", e.getMessage(), null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Ultrasound Added Successfully", "Ultrasound Added Successfully", null);
        }
    }

    /**
     * @param pid
     *            patient mid
     * @return sorted list of prior ultrasounds for the given patient
     */
    public List<Ultrasound> getUltrasoundsForPatient(String pid) {
        List<Ultrasound> ret = Collections.emptyList();
        long mid = -1;
        if ((pid != null) && ValidationFormat.NPMID.getRegex().matcher(pid).matches()) {
            mid = Long.parseLong(pid);
            try {
                ret = ultrasoundData.getUltrasoundsForPatient(mid).stream().sorted((o1, o2) -> {
                    return o2.getUltrasoundID().compareTo(o1.getUltrasoundID());
                }).collect(Collectors.toList());
            } catch (Exception e) {
                printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve prior ultrasounds",
                        e.getMessage(), null);
            }
        }
        return ret;
    }
    /**
     * @return list of ultrasounds for current patient
     */
    public List<Ultrasound> getUltrasoundsForCurrentPatient(){
        return getUltrasoundsForPatient(sessionUtils.getCurrentPatientMID());
    }

    /**
     *
     * @param ultrasoundID
     * @return encoded string of the ultrasound image
     */
    public String getUltrasoundImage(Long ultrasoundID){
        try{
            String ret = ultrasoundData.getUltrasoundImage(ultrasoundID);
            return ret;
        }catch (Exception e){
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Exception getting ultrasound img", e.getMessage(), null);
        }
        return null;
    }

    /**
     *
     * @param VisitID
     * @return ultrasound with given id
     */
    public Ultrasound getVisitByID(String VisitID) {
        long id = -1;
        try {
            id = Long.parseLong(VisitID);
        } catch (NumberFormatException ne) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Prior Ultrasound",
                    "Unable to Retrieve Prior ultrasound", null);
            return null;
        }
        try {
            return ultrasoundData.getByID(id);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Prior Ultrasound", "Unable to Retrieve Prior Ultrasound", null);
            return null;
        }
    }

    /**
     * @return Ultrasound of the selected patient in the HCP session
     */
    public Ultrasound getSelectedUltrasound() {
        String ultrasoundID = sessionUtils.getRequestParameter("ultrasoundID");
        if (ultrasoundID == null || ultrasoundID.isEmpty()) {
            return null;
        }
        return getVisitByID(ultrasoundID);
    }

    public String getObstetricOfficeVisitID(){
        return sessionUtils.getRequestParameter("visitID");
    }

    /**
     *
     * @param u ultrasound to be updated
     */
    public void edit(Ultrasound u) {
        boolean res = false;

        try {
            res = ultrasoundData.update(u);
        } catch (DBException e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating ultrasound", e.getExtendedMessage(), null);
        } catch (Exception e) {
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating ultrasound", e.getMessage(), null);
        }
        if (res) {
            printFacesMessage(FacesMessage.SEVERITY_INFO, "Updated Prior Ultrasound Successfully",
                    "Updated Prior Ultrasound Successfully", null);
        }
    }

    /**
     *
     * @return true if current patient has any ultrasounds
     */
    public boolean currentPatientHasUltrasounds() {
        return getUltrasoundsForCurrentPatient().size() > 0;
    }

    /**
     * Stores ultrasound image for an ultrasound
     * @param ultrasoundID
     * @param image
     */
    public void addUltrasoundImage(long ultrasoundID, Part image) {
        String encodedImage = null;
        try{
            //byte[] imageContent = IOUtils.toByteArray(image.getInputStream());
            //encodedImage = Base64.getEncoder().encodeToString(imageContent);
            //ultrasoundData.addUltrasoundImage(ultrasoundID, encodedImage);
            ultrasoundData.addUltrasoundImage(ultrasoundID, Base64.getEncoder().encodeToString(IOUtils.toByteArray(image.getInputStream())));


        } catch (Exception e){
            printFacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding ultrasound image", e.getMessage(),
                    null);
        }

    }

    /**
     *
     * @return encoded ultrasound image for an ultrasound
     */
    public String getSelectedUltrasoundImage() {
        String ultrasoundID = sessionUtils.getRequestParameter("ultrasoundID");
        if (ultrasoundID == null) {
            return null;
        }
        return getUltrasoundImage(Long.parseLong(ultrasoundID));
    }

    /**
     *
     * @return ultrasounds associated with current obstetric office visit
     * @throws Exception
     */
    public List<Ultrasound> getUltrasoundsForCurrentObstetricOfficeVisit() throws Exception{
        String pid = sessionUtils.getCurrentPatientMID();
        String obstetricOfficeVisitID = getObstetricOfficeVisitID();
        List<Ultrasound> ret = null;
        try{
            ret= ultrasoundData.getUltrasoundsForPatient(Long.parseLong(pid), Long.parseLong(obstetricOfficeVisitID));
        } catch(Exception e){

        }
        return ret;
    }

}
