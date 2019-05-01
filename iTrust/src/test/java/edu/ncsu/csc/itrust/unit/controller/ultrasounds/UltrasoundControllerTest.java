package edu.ncsu.csc.itrust.unit.controller.ultrasounds;

import edu.ncsu.csc.itrust.controller.obstetricsOfficeVisit.ObstetricsOfficeVisitController;
import edu.ncsu.csc.itrust.controller.ultrasounds.UltrasoundController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitData;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.ultrasounds.Ultrasound;
import edu.ncsu.csc.itrust.model.ultrasounds.UltrasoundData;
import edu.ncsu.csc.itrust.model.ultrasounds.UltrasoundMySQL;
import edu.ncsu.csc.itrust.webutils.SessionUtils;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.io.File;
import java.nio.file.Files;


public class UltrasoundControllerTest extends TestCase {

    private static final long DEFAULT_PATIENT_MID = 1L;
    private static final long DEFAULT_HCP_MID = 900000000L;

    @Spy
    private UltrasoundController uc;
    @Spy private UltrasoundController ucWithNullDS;
    @Spy private SessionUtils sessionUtils;

    @Mock
    private HttpServletRequest mockHttpServletRequest;
    @Mock private HttpSession mockHttpSession;
    @Mock private SessionUtils mockSessionUtils;

    private UltrasoundData uData;
    private DataSource ds;
    private Part testImgPart;
    private File testImg;
    private Byte[] testImgByteArray;
    private String testImageString;
    // files are finished
    private Ultrasound testUltra;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        mockSessionUtils = Mockito.mock(SessionUtils.class);
        uc = Mockito.spy(new UltrasoundController(ds, mockSessionUtils));
        Mockito.doNothing().when(uc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
//        Mockito.doNothing().when(ovc).redirectToBaseOfficeVisit();
        uData = new UltrasoundMySQL(ds);

        testUltra = new Ultrasound();
        testUltra.setPatientMID(DEFAULT_PATIENT_MID);
        testUltra.setObstetricOfficeVisitID(100);
        testUltra.setCrl(100);
        testUltra.setBpd(100);
        testUltra.setHc(100);
        testUltra.setEfw(100);
        testUltra.setHl(100);
        testUltra.setOfd(100);
        testUltra.setFl(100);
        testUltra.setAc(100);

        // Initialize a office visit controller with null data source
        ucWithNullDS = new UltrasoundController(null, null);

        // Mock HttpServletRequest
        mockHttpServletRequest = Mockito.mock(HttpServletRequest.class);

        // Mock HttpSession
        mockHttpSession = Mockito.mock(HttpSession.class);

    }

    @Test
    public void testAddNullUltrasound() throws DBException {
        uc.add(null);
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }
    @Test
    public void testEditNullUltrasound() throws DBException{
        uc.edit(null);
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testAddReturnGeneratedWithNull() throws DBException{
        uc.addReturnGeneratedId(null);
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRetrieveUltrasound() throws DBException {
        Assert.assertTrue("Ultrasound should be added successfully", uc.addReturnResult(testUltra));

        // Get the visit ID from the DB
        List<Ultrasound> all = uData.getAll();
        long visitID = -1;
        for (int i = 0; i < all.size(); i++) {
            Ultrasound ovI = all.get(i);
            visitID = ovI.getUltrasoundID();
        }
        Assert.assertNotEquals(-1L, visitID);
        Ultrasound check = uc.getVisitByID(Long.toString(visitID));
        Assert.assertEquals(testUltra.getAc(), check.getAc());
        Assert.assertEquals(testUltra.getBpd(), check.getBpd());
        Assert.assertEquals(testUltra.getCrl(), check.getCrl());
        Assert.assertEquals(testUltra.getEfw(), check.getEfw());
        Assert.assertEquals(testUltra.getFl(), check.getFl());
        Assert.assertEquals(testUltra.getHc(), check.getHc());
        Assert.assertEquals(testUltra.getHl(), check.getHl());
        Assert.assertEquals(testUltra.getOfd(), check.getOfd());

        testUltra.setUltrasoundID(visitID);

        uc.edit(testUltra);

    }


    @Test
    public void testAddUltrasoundWithFacesContext() throws DBException {
        uc.add(testUltra);
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }
//
    @Test
    public void testGetUltrasoundsPatientWithException() {
        Assert.assertEquals(0,
                ucWithNullDS.getUltrasoundsForPatient(Long.toString(DEFAULT_PATIENT_MID)).size());
    }
//
    @Test
    public void testGetUltrasoundsForPatientWithNullPid() {
        Assert.assertEquals(0, uc.getUltrasoundsForPatient(null).size());
    }
//
    @Test
    public void testGetUltrasoundsForPatientWithHCPPid() {
        Assert.assertEquals(0, uc.getUltrasoundsForPatient(Long.toString(DEFAULT_HCP_MID)).size());
    }
//
    @Test
    public void testGetOfficeVisitsForCurrentPatientWithInvalidMID() {
        Mockito.doReturn(Long.toString(DEFAULT_HCP_MID)).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, uc.getUltrasoundsForCurrentPatient().size());
        Mockito.doReturn("-1").when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, uc.getUltrasoundsForCurrentPatient().size());
        Mockito.doReturn(null).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertEquals(0, uc.getUltrasoundsForCurrentPatient().size());
    }
//
//
    @Test
    public void testGetVisitByIDWithInvalidID() {
        Assert.assertNull(uc.getVisitByID("invalid id"));
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());

        Assert.assertNull(uc.getVisitByID("-1"));

    }
//
//
    @Test
    public void testGetSelectedVisit() throws DBException {
        // Add a test office visit
        uc.add(testUltra);
        List<Ultrasound> ultrasoundList = uData.getAll();

        Assert.assertNotNull(ultrasoundList);
        Assert.assertFalse(ultrasoundList.isEmpty());

        // Return office visit id in mocked httpServletRequest
        Ultrasound expected = ultrasoundList.get(0);
        Mockito.doReturn(expected.getUltrasoundID().toString()).when(mockSessionUtils).parseString(Mockito.any());
        Mockito.doReturn(expected.getUltrasoundID().toString()).when(mockSessionUtils).getRequestParameter("ultrasoundID");

        Ultrasound actual = uc.getSelectedUltrasound();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getAc(), actual.getAc());
        Assert.assertEquals(expected.getBpd(), actual.getBpd());
        Assert.assertEquals(expected.getCrl(), actual.getCrl());
        Assert.assertEquals(expected.getEfw(), actual.getEfw());
        Assert.assertEquals(expected.getFl(), actual.getFl());
        Assert.assertEquals(expected.getHc(), actual.getHc());
        Assert.assertEquals(expected.getHl(), actual.getHl());
        Assert.assertEquals(expected.getOfd(), actual.getOfd());

    }
//
    @Test
    public void testGetSelectedVisitWithNullRequest() {
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("ultrasoundID");
        Ultrasound ov = uc.getSelectedUltrasound();
        Assert.assertNull(ov);
    }
//
//    @Test
//    public void testGetSelectedVisitWithNullVisitId() {
//        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
//        ObstetricsOfficeVisit ov = ovc.getSelectedVisit();
//        Assert.assertNull(ov);
//    }
//
    @Test
    public void testCurrentPatientHasUltrasounds() {
        uc.add(testUltra);
        Mockito.doReturn(Long.toString(DEFAULT_PATIENT_MID)).when(mockSessionUtils).getCurrentPatientMID();
        Assert.assertTrue(uc.currentPatientHasUltrasounds());
    }
//
    @Test
    public void testGetUltrasoundByObstetricVisitID(){
        Mockito.doReturn(Long.toString(DEFAULT_PATIENT_MID)).when(mockSessionUtils).getCurrentPatientMID();
        Mockito.doReturn(null).when(mockSessionUtils).getSessionVariable("visitID");
        try{
            uc.getUltrasoundsForCurrentObstetricOfficeVisit();
        }catch(Exception e){
            Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                    Mockito.anyString(), Mockito.anyString());

        }
    }
    @Test
    public void testGetNullUltrasoundIMG(){
        uc.getUltrasoundImage(null);
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());

    }

    @Test
    public void testAddUltrasoundImgNullID(){
        uc.addUltrasoundImage(-1, null);
        Mockito.verify(uc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());

    }
    @Test
    public void testGetSelectedUltrasoundImage(){
        Long id = uc.addReturnGeneratedId(testUltra);
        Mockito.doReturn(id.toString()).when(mockSessionUtils).getRequestParameter("ultrasoundID");
        uc.getSelectedUltrasoundImage();
    }
    @Test
    public void testGetSelectedNullUltrasoundImage(){
        Mockito.doReturn(null).when(mockSessionUtils).getRequestParameter("ultrasoundID");
        assertEquals(null, uc.getSelectedUltrasoundImage());
    }


}
