package edu.ncsu.csc.itrust.unit.controller.obstetricVisit;

import edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitData;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ObstetricVisitControllerTest extends TestCase {
    private static final long DEFAULT_PATIENT_MID = 1L;
    private static final long DEFAULT_HCP_MID = 900000000L;
    @Spy private ObstetricVisitController ovc;
    @Spy private ObstetricVisitController ovcWithNullDataSource;
    @Spy private SessionUtils sessionUtils;

    @Mock private HttpServletRequest mockHttpServletRequest;
    @Mock private HttpSession mockHttpSession;
    @Mock private SessionUtils mockSessionUtils;


    private ApptTypeData apptData;
    private ObstetricVisitData ovData;
    private DataSource ds;
    private TestDataGenerator gen; // remove when ApptType, Patient, and other
    // files are finished

    private ObstetricVisit testOV;
    private LocalDateTime birthDate;
    private LocalDateTime babyDate;


    @Before
    public void setUp() throws Exception {

        ds = ConverterDAO.getDataSource();
        mockSessionUtils = Mockito.mock(SessionUtils.class);
        ovc = Mockito.spy(new ObstetricVisitController(ds, mockSessionUtils));
        Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
        Mockito.doNothing().when(ovc).redirectToBaseOfficeVisit();
        apptData = new ApptTypeMySQLConverter(ds);
        ovData = new ObstetricVisitMySQL(ds);
        gen = new TestDataGenerator();
        gen.appointmentType();
        gen.hospitals();
        gen.patient1();
        gen.uc51();
        gen.uc52();
        gen.uc53SetUp();
        // Setup date
        birthDate = ovc.getPatientDOB(DEFAULT_PATIENT_MID).atTime(0, 0);
        babyDate = birthDate.plusYears(1);


        testOV = new ObstetricVisit();
        testOV.setPatientMID(DEFAULT_PATIENT_MID);
        List<ApptType> types = apptData.getAll();
        long apptTypeID = types.get((types.size() - 1)).getID();
        testOV.setApptTypeID(apptTypeID);
        HospitalData hospitalData = new HospitalMySQLConverter(ds);
        List<Hospital> hospitals = hospitalData.getAll();
        String locID = hospitals.get((hospitals.size() - 1)).getHospitalID();
        testOV.setLocationID(locID);
        testOV.setNotes("Hello World!");
        testOV.setLMP(babyDate);

        // Default test OV to baby date
        testOV.setDate(babyDate);
        testOV.setWeeksPregnant();


        // Initialize a office visit controller with null data source
        ovcWithNullDataSource = new ObstetricVisitController(null, sessionUtils);

        // Mock HttpServletRequest
        mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

        // Mock HttpSession
        mockHttpSession = Mockito.mock(HttpSession.class);

    }
    @Test
    public void testRetrieveObstetricVisit() throws DBException {
        Assert.assertTrue("Obstetric Visit Successfully Created", ovc.addReturnResult(testOV));
        // Get the visit ID from the DB
        List<ObstetricVisit> all = ovData.getAll();
        long visitID = -1;
        for (int i = 0; i < all.size(); i++) {
            ObstetricVisit ovI = all.get(i);
            boolean bApptType = ovI.getApptTypeID().equals(testOV.getApptTypeID());
            boolean bDate = false;
            long time = ChronoUnit.MINUTES.between(testOV.getDate(), ovI.getDate());
            bDate = (time < 1);
            boolean bLoc = (testOV.getLocationID().equals(ovI.getLocationID()));
            boolean bNotes = false;
            if (testOV.getNotes() == null) {
                if (ovI.getNotes() == null)
                    bNotes = true;

            } else {
                bNotes = (testOV.getNotes().equals(ovI.getNotes()));
            }
            if (bApptType && bDate && bLoc && bNotes) {
                visitID = ovI.getVisitID();
            }
        }
        Assert.assertNotEquals(-1L, visitID);
        ObstetricVisit check = ovc.getVisitByID(Long.toString(visitID));
        Assert.assertEquals(testOV.getApptTypeID(), check.getApptTypeID());
        long dif = ChronoUnit.MINUTES.between(testOV.getDate(), check.getDate());
        Assert.assertTrue(dif < 1);
        Assert.assertEquals(testOV.getLocationID(), check.getLocationID());
        Assert.assertEquals(testOV.getNotes(), check.getNotes());
        testOV.setVisitID(visitID);
        testOV.setNotes("testNotes");
        ovc.edit(testOV);

    }

    @Test
    public void testAddObstetricVisitWithInvalidDate() throws DBException {
        LocalDateTime date = birthDate.minusYears(100);
        testOV.setDate(date);
        Assert.assertTrue("Office Visit date cannot be set prior to patient birthday", ovc.addReturnResult(testOV));
    }

    @Test
    public void testAddObstetricVisitWithInvalidDateAndFacesContext() throws DBException {
        LocalDateTime date = birthDate.minusDays(1);
        testOV.setDate(date);
        ovc.add(testOV);
    }

    @Test
    public void testAddObstetricVisitWithFacesContext() throws DBException {
        ovc.add(testOV);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }


    @Test
    public void testGetObstetricVisitsForPatientWithException() {
        Assert.assertEquals(0,
                ovcWithNullDataSource.getObstetricVisitsForPatient(Long.toString(DEFAULT_PATIENT_MID)).size());
    }

    @Test
    public void testGetObstetricVisitsForPatientWithNullPid() {
        Assert.assertEquals(0, ovc.getObstetricVisitsForPatient(null).size());
    }

    @Test
    public void testGetObstetricVisitsForPatientWithHCPPid() {
        Assert.assertEquals(0, ovc.getObstetricVisitsForPatient(Long.toString(DEFAULT_HCP_MID)).size());
    }


    @Test
    public void testGetObstetricVisitsForCurrentPatientWithInvalidMID() {
        Mockito.doReturn(Long.toString(DEFAULT_HCP_MID)).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getObstetricVisitsForCurrentPatient().size());
        Mockito.doReturn("-1").when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getObstetricVisitsForCurrentPatient().size());
        Mockito.doReturn(null).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getObstetricVisitsForCurrentPatient().size());
    }

    @Test
    public void testGetSelectedVisit() throws DBException {
        // Add a test office visit
        ovc.add(testOV);
        List<ObstetricVisit> officeVisitList = ovData.getAll();

        Assert.assertNotNull(officeVisitList);
        Assert.assertFalse(officeVisitList.isEmpty());

        // Return office visit id in mocked httpServletRequest
        ObstetricVisit expected = officeVisitList.get(0);
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).parseString(Mockito.any());
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).getRequestParameter("visitID");

        ObstetricVisit actual = ovc.getSelectedVisit();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getPatientMID(), actual.getPatientMID());
        Assert.assertEquals(expected.getApptTypeID(), actual.getApptTypeID());
        long dif = ChronoUnit.MINUTES.between(expected.getDate(), actual.getDate());
        Assert.assertTrue(dif < 1);
        Assert.assertEquals(expected.getLocationID(), actual.getLocationID());
        Assert.assertEquals(expected.getNotes(), actual.getNotes());
    }

    @Test
    public void testGetSelectedVisitWithNullRequest() {
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
        ObstetricVisit ov = ovc.getSelectedVisit();
        Assert.assertNull(ov);
    }

    @Test
    public void testAddReturnResultNullObject() throws DBException {
        ObstetricVisit ov = null;
        Assert.assertFalse(ovc.addReturnResult(ov));
    }

//    @Test
//    public void testObstetricVisitController() throws DBException {
//        ObstetricVisitController ovcontroller = new ObstetricVisitController();
//    }

    @Test
    public void testAddReturnGeneratedID() throws DBException {
        List<ObstetricVisit> all = ovData.getAll();
        ObstetricVisit ov = all.get(1);
        Assert.assertNotEquals(-1, ovc.addReturnGeneratedId(ov));
    }

    @Test
    public void testAddReturnGeneratedIDNullObject() throws DBException {
        ObstetricVisit ov = null;
        Assert.assertEquals(-1, ovc.addReturnGeneratedId(ov));
    }

    @Test
    public void testAddNullObject() throws DBException {
        ovc.add(null);
        List<ObstetricVisit> officeVisitList = ovData.getAll();
        Assert.assertNotNull(officeVisitList);
        Assert.assertFalse(officeVisitList.isEmpty());
    }

    @Test
    public void testGetVisitByIDNullString() throws DBException {
        ObstetricVisit check = ovc.getVisitByID(null);
        Assert.assertNull(check);
    }

    @Test
    public void testCurrentPatientHasObstetricVisitWithNoPatient() throws DBException {
        Assert.assertFalse(ovc.CurrentPatientHasObstetricVisit());
    }

    @Test
    public void testHasPatientVisited() throws DBException {
        Assert.assertTrue(ovc.hasPatientVisited("1"));
    }

    @Test
    public void testEditNullObject() throws DBException {
        ovc.edit(null);
    }

//    @Test
//    public void testRedirectToBaseOfficeVisit() throws DBException, IOException {
//        ovc.redirectToBaseOfficeVisit();
//    }

    @Test
    public void testLogging() throws DBException {
        ovc.add(testOV);
        List<ObstetricVisit> officeVisitList = ovData.getAll();

        Assert.assertNotNull(officeVisitList);
        Assert.assertFalse(officeVisitList.isEmpty());

        // Return office visit id in mocked httpServletRequest
        ObstetricVisit expected = officeVisitList.get(0);
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).parseString(Mockito.any());
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).getRequestParameter("visitID");

        ObstetricVisit actual = ovc.getSelectedVisit();

        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.OBSTETRICS_RECORD_CREATE), Mockito.anyString());
        Mockito.doNothing().when(ovc).logTransaction(Mockito.eq(TransactionType.OBSTETRICS_RECORD_VIEW), Mockito.anyString());

        ovc.logViewObstetricVisit();
        ovc.logCreateObstetricVisit("testString");
    }



}

