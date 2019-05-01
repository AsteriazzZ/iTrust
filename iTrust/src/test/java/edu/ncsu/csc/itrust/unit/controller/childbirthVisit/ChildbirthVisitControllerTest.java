package edu.ncsu.csc.itrust.unit.controller.childbirthVisit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.childbirthVisit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisitData;
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

public class ChildbirthVisitControllerTest extends TestCase {
    private static final long DEFAULT_PATIENT_MID = 1L;
    private static final long DEFAULT_HCP_MID = 900000000L;

    @Spy private ChildbirthVisitController ovc;
    @Spy private ChildbirthVisitController ovcWithNullDataSource;
    @Spy private SessionUtils sessionUtils;

    @Mock private HttpServletRequest mockHttpServletRequest;
    @Mock private HttpSession mockHttpSession;
    @Mock private SessionUtils mockSessionUtils;

    private ApptTypeData apptData;
    private ChildbirthVisitData ovData;
    private DataSource ds;
    // files are finished
    private ChildbirthVisit testOV;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        mockSessionUtils = Mockito.mock(SessionUtils.class);
        ovc = Mockito.spy(new ChildbirthVisitController(ds, mockSessionUtils));
        Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
//        Mockito.doNothing().when(ovc).redirectToBaseOfficeVisit();
        ovData = new ChildbirthVisitMySQL(ds);

        // Setup test ObstetricsOfficeVisit
        testOV = new ChildbirthVisit();
        testOV.setPatientMID(DEFAULT_PATIENT_MID);
        testOV.setVisitDate(LocalDateTime.now());
        testOV.setLocationID("1");
        testOV.setPreScheduled(true);
        testOV.setDeliveryType("Vaginal Delivery");
        testOV.setPitocin(new Long(10));
        testOV.setOxide(new Long(10));
        testOV.setPethidine(new Long(10));
        testOV.setMagnesium(new Long(10));
        testOV.setRh(new Long(10));
        testOV.setEpidural(new Long(10));

        // Initialize a office visit controller with null data source
        ovcWithNullDataSource = new ChildbirthVisitController(null, null);

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
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testAddReturnGeneratedWithNull() throws DBException{
        ovc.addReturnGeneratedId(null);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRetrieveObstetricsOfficeVisit() throws DBException {
        ovc.addReturnResult(testOV);
        Assert.assertTrue("Office visit should be added successfully", ovc.addReturnResult(testOV));

        // Get the visit ID from the DB
        List<ChildbirthVisit> all = ovData.getAll();
        long visitID = -1;
        for (int i = 0; i < all.size(); i++) {
            ChildbirthVisit ovI = all.get(i);
            visitID = ovI.getVisitID();
        }
        Assert.assertNotEquals(-1L, visitID);
        ChildbirthVisit check = ovc.getVisitByID(Long.toString(visitID));
        Assert.assertEquals(testOV.getDeliveryType(), check.getDeliveryType());
        Assert.assertEquals(testOV.getLocationID(), check.getLocationID());
        Assert.assertEquals(testOV.getPreScheduled(), check.getPreScheduled());
        Assert.assertEquals(testOV.getPitocin(), check.getPitocin());
        Assert.assertEquals(testOV.getOxide(), check.getOxide());
        Assert.assertEquals(testOV.getPethidine(), check.getPethidine());
        Assert.assertEquals(testOV.getMagnesium(), check.getMagnesium());
        Assert.assertEquals(testOV.getEpidural(), check.getEpidural());
        Assert.assertEquals(testOV.getRh(), check.getRh());

        long dif = ChronoUnit.MINUTES.between(testOV.getVisitDate(), check.getVisitDate());
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
                ovcWithNullDataSource.getChildbirthVisitsForPatient(Long.toString(DEFAULT_PATIENT_MID)).size());
    }

    @Test
    public void testGetObstetricsOfficeVisitsForPatientWithNullPid() {
        Assert.assertEquals(0, ovc.getChildbirthVisitsForPatient(null).size());
    }

    @Test
    public void testGetObstetricsOfficeVisitsForPatientWithHCPPid() {
        Assert.assertEquals(0, ovc.getChildbirthVisitsForPatient(Long.toString(DEFAULT_HCP_MID)).size());
    }

    @Test
    public void testGetOfficeVisitsForCurrentPatientWithInvalidMID() {
        Mockito.doReturn(Long.toString(DEFAULT_HCP_MID)).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getChildbirthVisitsForCurrentPatient().size());
        Mockito.doReturn("-1").when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getChildbirthVisitsForCurrentPatient().size());
        Mockito.doReturn(null).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, ovc.getChildbirthVisitsForCurrentPatient().size());
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
        List<ChildbirthVisit> officeVisitList = ovData.getAll();

        Assert.assertNotNull(officeVisitList);
        Assert.assertFalse(officeVisitList.isEmpty());

        // Return office visit id in mocked httpServletRequest
        ChildbirthVisit expected = officeVisitList.get(0);
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).parseString(Mockito.any());
        Mockito.doReturn(expected.getVisitID().toString()).when(mockSessionUtils).getRequestParameter("visitID");

        ChildbirthVisit actual = ovc.getSelectedVisit();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getDeliveryType(), actual.getDeliveryType());
        Assert.assertEquals(expected.getLocationID(), actual.getLocationID());
        Assert.assertEquals(expected.getPreScheduled(), actual.getPreScheduled());
        Assert.assertEquals(expected.getPitocin(), actual.getPitocin());
        Assert.assertEquals(expected.getOxide(), actual.getOxide());
        Assert.assertEquals(expected.getPethidine(), actual.getPethidine());
        Assert.assertEquals(expected.getMagnesium(), actual.getMagnesium());
        Assert.assertEquals(expected.getEpidural(), actual.getEpidural());
        Assert.assertEquals(expected.getRh(), actual.getRh());

        long dif = ChronoUnit.MINUTES.between(expected.getVisitDate(), actual.getVisitDate());
        Assert.assertTrue(dif < 1);
    }

    @Test
    public void testGetSelectedVisitWithNullRequest() {
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
        ChildbirthVisit ov = ovc.getSelectedVisit();
        Assert.assertNull(ov);
    }

    @Test
    public void testGetSelectedVisitWithNullVisitId() {
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
        ChildbirthVisit ov = ovc.getSelectedVisit();
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

        Assert.assertFalse(ovc.CurrentPatientHasChildbirthVisit());
    }

    @Test
    public void testAddReturnGeneratedId() {
        long id = ovc.addReturnGeneratedId(testOV);
        Assert.assertTrue(id >= 0);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testDeliveryTypes() {
        ArrayList<String> actualList = new ArrayList<String>();
        actualList.add("Vaginal Delivery");
        actualList.add("Vaginal Delivery Vacuum Assist");
        actualList.add("Vaginal Delivery Forceps Assist");
        actualList.add("Caesarean Section");
        actualList.add("Miscarriage");
        assertEquals(actualList, ovc.getDeliveryTypes());

    }

    @Test
    public void testDeleteWithInvalidID() throws DBException{
        ovc.delete(new Long(1));
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }


}
