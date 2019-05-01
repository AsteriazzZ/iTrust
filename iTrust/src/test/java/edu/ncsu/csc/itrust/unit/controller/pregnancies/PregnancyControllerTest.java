package edu.ncsu.csc.itrust.unit.controller.pregnancies;

import edu.ncsu.csc.itrust.controller.pregnancies.PregnancyController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;
import edu.ncsu.csc.itrust.model.pregnancies.PregnancyData;
import edu.ncsu.csc.itrust.model.pregnancies.PregnancyMySQL;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PregnancyControllerTest extends TestCase {
    private static final long DEFAULT_PATIENT_MID = 1L;
    private static final long DEFAULT_HCP_MID = 900000000L;
    @Spy private PregnancyController pc;
    @Spy private PregnancyController pcWithNullDataSource;
    @Spy private SessionUtils sessionUtils;

    @Mock private HttpServletRequest mockHttpServletRequest;
    @Mock private HttpSession mockHttpSession;
    @Mock private SessionUtils mockSessionUtils;


    private ApptTypeData apptData;
    private PregnancyData pData;
    private DataSource ds;
    private TestDataGenerator gen;
    // files are finished

    private Pregnancy testP;
    private LocalDateTime birthDate;
    private LocalDateTime babyDate;


    @Before
    public void setUp() throws Exception {

        ds = ConverterDAO.getDataSource();
        mockSessionUtils = Mockito.mock(SessionUtils.class);

        pc = Mockito.spy(new PregnancyController(ds, mockSessionUtils));
        Mockito.doNothing().when(pc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
//        Mockito.doNothing().when(pc).redirectToBaseOfficeVisit();
        apptData = new ApptTypeMySQLConverter(ds);
        pData = new PregnancyMySQL(ds);

        gen = new TestDataGenerator();
        gen.appointmentType();
        gen.hospitals();
        gen.patient1();
        gen.uc51();
        gen.uc52();
        gen.uc53SetUp();
        // Setup date
//        birthDate = pc.getPatientDOB(DEFAULT_PATIENT_MID).atTime(0, 0);
//        babyDate = birthDate.plusYears(1);


        testP = new Pregnancy();
//        testP.setPatientMID(DEFAULT_PATIENT_MID);
//        List<ApptType> types = apptData.getAll();
        testP.setPregnancyID(1L);
        testP.setPatientMID(2L);
        testP.setConceptionYear(2018);
        testP.setWeeksPregnant(47);
        testP.setLaborHours(10);
        testP.setWeightGain(5);
        testP.setDeliveryType("a");
        testP.setNumberOfChildren(2);

        // Initialize a office visit controller with null data source
//        pcWithNullDataSource = new PregnancyController(null, sessionUtils);

        // Mock HttpServletRequest
        mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

        // Mock HttpSession
        mockHttpSession = Mockito.mock(HttpSession.class);

    }

    @Test
    public void testAddObstetricVisitWithInvalidDate() throws DBException {
        Assert.assertTrue(pc.addReturnGeneratedId(testP) > 0);
    }

    @Test
    public void testAddReturnResult() throws DBException {
        Assert.assertTrue(pc.addReturnResult(testP));
    }

    @Test
    public void testAddReturnResultNull() throws DBException {
        Assert.assertNotNull(pc.addReturnResult(null));
    }

    @Test
    public void testAddReturnGeneratedIdNull() throws DBException {
        Assert.assertNotNull(pc.addReturnGeneratedId(null));
    }

    @Test
    public void testEdit() throws DBException {
        pc.edit(null);
        pc.edit(testP);
    }

    @Test
    public void testGetSelectedPregnancy() throws DBException {
        Assert.assertNull(pc.getSelectedPregnancy());
    }

    @Test
    public void testGetVisitByID() throws DBException {
        Assert.assertNotNull(pc.getVisitByID("1"));
        Assert.assertNull(pc.getVisitByID("a"));
        Assert.assertNull(pc.getVisitByID("-1"));
    }

    @Test
    public void testGetPregnanciesForCurrentPatient() throws DBException {
        Assert.assertNotNull(pc.getPregnanciesForCurrentPatient());
    }

    @Test
    public void testGetPregnanciesForPatient() throws DBException {
        Assert.assertNotNull(pc.getPregnanciesForPatient("1"));
    }

    @Test
    public void testAdd() throws DBException {
        pc.add(testP);
        pc.add(null);
    }

    @Test
    public void testCurrentPatientHasPregnancyVisit() throws DBException {
        Assert.assertNotNull(pc.currentPatientHasPregnancyVisit());
    }








}

