package edu.ncsu.csc.itrust.unit.controller.obstetricsOfficeVisit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.time.Period;

import edu.ncsu.csc.itrust.controller.childbirthVisit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitController;
import edu.ncsu.csc.itrust.controller.obstetricsOfficeVisit.ObstetricsOfficeVisitController;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitData;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;

public class ObstetricsOfficeVisitControllerTest extends TestCase {

    private static final long DEFAULT_PATIENT_MID = 1L;
    private static final long DEFAULT_HCP_MID = 900000000L;
    private DAOFactory factory = TestDAOFactory.getTestInstance();

    @Spy private ObstetricsOfficeVisitController ovc;
    @Spy private ObstetricVisitController initializationController;
    @Spy private ChildbirthVisitController childbirthVisitController;
    @Spy private ObstetricsOfficeVisitController ovcWithNullDataSource;
    @Spy private SessionUtils sessionUtils;

    @Mock private HttpServletRequest mockHttpServletRequest;
    @Mock private HttpSession mockHttpSession;
    @Mock private SessionUtils mockSessionUtils;

    private ApptTypeData apptData;
    private ObstetricsOfficeVisitData ovData;
    private DataSource ds;
    // files are finished
    private ObstetricsOfficeVisit testOV;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        mockSessionUtils = Mockito.mock(SessionUtils.class);
        ovc = Mockito.spy(new ObstetricsOfficeVisitController(ds, mockSessionUtils));
        initializationController = Mockito.spy(new ObstetricVisitController(ds, mockSessionUtils));
        childbirthVisitController = Mockito.spy(new ChildbirthVisitController(ds, mockSessionUtils));
        Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
//        Mockito.doNothing().when(ovc).redirectToBaseOfficeVisit();
        ovData = new ObstetricsOfficeVisitMySQL(ds);

        // Setup test ObstetricsOfficeVisit
        testOV = new ObstetricsOfficeVisit();
        testOV.setPatientMID(DEFAULT_PATIENT_MID);
        testOV.setDate(LocalDateTime.now());
        testOV.setBloodPressure("10");
        testOV.setFetalHeartRate(10);
        testOV.setLowLyingPlacenta("Yes");
        testOV.setMultiplePregnancy(1);
        testOV.setNumOfWeeksPregnant(10);
        testOV.setWeight(Float.parseFloat("10"));

        // Initialize a office visit controller with null data source
        ovcWithNullDataSource = new ObstetricsOfficeVisitController(null, null);

        // Mock HttpServletRequest
        mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

        // Mock HttpSession
        mockHttpSession = Mockito.mock(HttpSession.class);
    }

    @Test
    public void testAddNullObstetricOfficeVisit() throws DBException{
        ovc.add(null);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }
    @Test
    public void testEditNullObstetricOfficeVisit() throws DBException{
        ovc.edit(null);
//        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
//                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testAddReturnGeneratedWithNull() throws DBException{
        ovc.addReturnGeneratedId(null);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRetrieveObstetricsOfficeVisit() throws DBException {
        Assert.assertTrue("Office visit should be added successfully", ovc.addReturnResult(testOV));

        // Get the visit ID from the DB
        List<ObstetricsOfficeVisit> all = ovData.getAll();
        long visitID = -1;
        for (int i = 0; i < all.size(); i++) {
            ObstetricsOfficeVisit ovI = all.get(i);
            visitID = ovI.getVisitID();
        }
        Assert.assertNotEquals(-1L, visitID);
        ObstetricsOfficeVisit check = ovc.getVisitByID(Long.toString(visitID));
        Assert.assertEquals(testOV.getBloodPressure(), check.getBloodPressure());
        Assert.assertEquals(testOV.getFetalHeartRate(), check.getFetalHeartRate());
        Assert.assertEquals(testOV.getLowLyingPlacenta(), check.getLowLyingPlacenta());
        Assert.assertEquals(testOV.getMultiplePregnancy(), check.getMultiplePregnancy());
        Assert.assertEquals(testOV.getNumOfWeeksPregnant(), check.getNumOfWeeksPregnant());
        Assert.assertEquals(testOV.getWeight(), check.getWeight());

        long dif = ChronoUnit.MINUTES.between(testOV.getDate(), check.getDate());
        Assert.assertTrue(dif < 1);

        testOV.setVisitID(visitID);

        ovc.edit(testOV);

    }


    @Test
    public void testAddOfficeVisitWithFacesContext() throws DBException {
        ovc.add(testOV);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetObstetricsOfficeVisitsForPatientWithException() {
        Assert.assertEquals(0,
                ovcWithNullDataSource.getObstetricsOfficeVisitsForPatient(Long.toString(DEFAULT_PATIENT_MID)).size());
    }

    @Test
    public void testGetObstetricsOfficeVisitsForPatientWithNullPid() {
        Assert.assertEquals(0, ovc.getObstetricsOfficeVisitsForPatient(null).size());
    }

    @Test
    public void testGetObstetricsOfficeVisitsForPatientWithHCPPid() {
        Assert.assertEquals(0, ovc.getObstetricsOfficeVisitsForPatient(Long.toString(DEFAULT_HCP_MID)).size());
    }

    @Test
    public void testGetOfficeVisitsForCurrentPatientWithInvalidMID() {
        Mockito.doReturn(Long.toString(DEFAULT_HCP_MID)).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getObstetricsOfficeVisitsForCurrentPatient().size());
        Mockito.doReturn("-1").when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getObstetricsOfficeVisitsForCurrentPatient().size());
        Mockito.doReturn(null).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getObstetricsOfficeVisitsForCurrentPatient().size());
    }


    @Test
    public void testGetVisitByIDWithInvalidID() {
        Assert.assertNull(ovc.getVisitByID("invalid id"));
        Assert.assertNull(ovc.getVisitByID("-1"));
    }


    @Test
    public void testGetSelectedVisit() throws DBException {
        // Add a test office visit
        ovc.add(testOV);
        List<ObstetricsOfficeVisit> officeVisitList = ovData.getAll();

        Assert.assertNotNull(officeVisitList);
        Assert.assertFalse(officeVisitList.isEmpty());

        // Return office visit id in mocked httpServletRequest
        ObstetricsOfficeVisit expected = officeVisitList.get(0);
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).parseString(Mockito.any());
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).getRequestParameter("visitID");

        ObstetricsOfficeVisit actual = ovc.getSelectedVisit();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getBloodPressure(), actual.getBloodPressure());
        Assert.assertEquals(expected.getFetalHeartRate(), actual.getFetalHeartRate());
        Assert.assertEquals(expected.getLowLyingPlacenta(), actual.getLowLyingPlacenta());
        Assert.assertEquals(expected.getMultiplePregnancy(), actual.getMultiplePregnancy());
        Assert.assertEquals(expected.getNumOfWeeksPregnant(), actual.getNumOfWeeksPregnant());
        Assert.assertEquals(expected.getWeight(), actual.getWeight());

        long dif = ChronoUnit.MINUTES.between(expected.getDate(), actual.getDate());
        Assert.assertTrue(dif < 1);
    }

    @Test
    public void testGetSelectedVisitWithNullRequest() {
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
        ObstetricsOfficeVisit ov = ovc.getSelectedVisit();
        Assert.assertNull(ov);
    }

    @Test
    public void testGetSelectedVisitWithNullVisitId() {
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
        ObstetricsOfficeVisit ov = ovc.getSelectedVisit();
        Assert.assertNull(ov);
    }

    @Test
    public void testHasPatientVisited() {
        ovc.add(testOV);
        Assert.assertTrue(ovc.hasPatientVisited("1"));
    }

    @Test
    public void testHasPatientVisitedWithNulls() {
        Assert.assertFalse(ovc.hasPatientVisited(null));
        Assert.assertFalse(ovc.hasPatientVisited("-1"));
    }

    @Test
    public void testCurrentPatientHasVisited() {
        final String MID = "101";
        final String PATIENT = "patient";

        Mockito.doReturn(PATIENT).when(mockSessionUtils).getSessionUserRole();
        Mockito.doReturn(MID).when(mockSessionUtils).getCurrentPatientMID();

        Assert.assertFalse(ovc.CurrentPatientHasVisited());
    }

    @Test
    public void testAddReturnGeneratedId() {
        long id = ovc.addReturnGeneratedId(testOV);
        Assert.assertTrue(id >= 0);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogViewObstetricsOfficeVisit() {
        Mockito.when(mockSessionUtils.getRequestParameter("visitID")).thenReturn("1");
        ovc.setSessionUtils(mockSessionUtils);
        Mockito.doNothing().when(ovc).logTransaction(Mockito.any(), Mockito.anyString());
        ovc.logViewObstetricOfficeVisit();
        Mockito.verify(ovc, Mockito.times(1)).logTransaction(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testLogCreateObstetricOfficeVisit() {
        Mockito.doNothing().when(ovc).logTransaction(Mockito.any(), Mockito.anyString());
        ovc.logCreateObstetricOfficeVisit("1");
        Mockito.verify(ovc, Mockito.times(1)).logTransaction(Mockito.any(), Mockito.anyString());
    }
    @Test
    public void testLogEditObstetricOfficeVisit() {
        Mockito.doNothing().when(ovc).logTransaction(Mockito.any(), Mockito.anyString());
        ovc.logEditObstetricOfficeVisit("1");
        Mockito.verify(ovc, Mockito.times(1)).logTransaction(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void testCurrentPatientNeedsShot() throws Exception{
        //Set up patient
        PatientDAO dao = new PatientDAO(factory);
        Long pid = dao.addEmptyPatient();
        PatientBean p = new PatientBean();
        p.setMID(pid);
        BloodType btype = BloodType.ONeg;
        p.setBloodType(btype);
        p.setBloodTypeStr(btype.toString());
        dao.editPatient(p, DEFAULT_HCP_MID);

        //Set up weeks pregnant for patient
        ObstetricVisit visit = new ObstetricVisit();
        LocalDateTime now = LocalDateTime.now();
        visit.setDate(now);
        visit.setLMP(now.minus(Period.ofWeeks(30)));
        visit.setWeeksPregnant();
        visit.setPatientMID(pid);
        visit.setApptTypeID(0L);
        visit.setEDD();
        visit.setLocationID("test");

        initializationController.add(visit);
        Mockito.doReturn(Long.toString(pid)).when(mockSessionUtils).getCurrentPatientMID();

        Assert.assertTrue(ovc.CurrentPatientNeedsRHShot());
    }

    @Test
    public void testScheduleNextVisitMonthly() throws Exception{
        Mockito.doReturn("1").when(mockSessionUtils).getCurrentPatientMID();
        Mockito.doReturn("10").when(ovc).getWeeksPregnantFacesContext();
        Mockito.doReturn("11").when(ovc).getOfficeVisitIDFacesContext();
        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.SCHEDULE_NEXT_OBSTETRICS_OFFICE_VISIT), Mockito.anyString());
        try{
            ovc.scheduleNextVisit();
        }catch(Exception e){

        }
    }

    @Test
    public void testScheduleNextVisit2weeks() throws Exception{
        Mockito.doReturn("1").when(mockSessionUtils).getCurrentPatientMID();
        Mockito.doReturn("15").when(ovc).getWeeksPregnantFacesContext();
        Mockito.doReturn("11").when(ovc).getOfficeVisitIDFacesContext();
        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.SCHEDULE_NEXT_OBSTETRICS_OFFICE_VISIT), Mockito.anyString());
        try{
            ovc.scheduleNextVisit();
        }catch(Exception e){

        }
    }

    @Test
    public void testScheduleNextVisitWeekly() throws Exception{
        Mockito.doReturn("1").when(mockSessionUtils).getCurrentPatientMID();
        Mockito.doReturn("29").when(ovc).getWeeksPregnantFacesContext();
        Mockito.doReturn("11").when(ovc).getOfficeVisitIDFacesContext();
        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.SCHEDULE_NEXT_OBSTETRICS_OFFICE_VISIT), Mockito.anyString());
        try{
            ovc.scheduleNextVisit();
        }catch(Exception e){

        }
    }
    @Test
    public void testScheduleNextChildbirthVisit() throws Exception{
        Mockito.doReturn("1").when(mockSessionUtils).getCurrentPatientMID();
        Mockito.doReturn("42").when(ovc).getWeeksPregnantFacesContext();
        Mockito.doReturn("11").when(ovc).getOfficeVisitIDFacesContext();
        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.SCHEDULE_NEXT_CHILDBIRTH_OFFICE_VISIT), Mockito.anyString());
        try {
            ovc.scheduleNextVisit();
        }
        catch (Exception e){

        }
    }

    @Test
    public void testCantScheduleNextVisit() throws Exception{
        Mockito.doReturn("1").when(mockSessionUtils).getCurrentPatientMID();
        Mockito.doReturn("50").when(ovc).getWeeksPregnantFacesContext();
        Mockito.doReturn("11").when(ovc).getOfficeVisitIDFacesContext();
        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.SCHEDULE_NEXT_CHILDBIRTH_OFFICE_VISIT), Mockito.anyString());
        ovc.scheduleNextVisit();
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

}
