package edu.ncsu.csc.itrust.unit.controller.childbirth;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.childbirth.ChildbirthController;
import edu.ncsu.csc.itrust.model.childbirth.Childbirth;
import edu.ncsu.csc.itrust.model.childbirth.ChildbirthMySQL;
import edu.ncsu.csc.itrust.model.childbirth.ChildbirthData;
import edu.umd.cs.findbugs.ba.type.ExceptionObjectType;
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

public class ChildbirthControllerTest extends TestCase {
    private static final long DEFAULT_PATIENT_MID = 1L;
    private static final long DEFAULT_HCP_MID = 900000000L;

    @Spy private ChildbirthController ovc;
    @Spy private ChildbirthController ovcWithNullDataSource;
    @Spy private SessionUtils sessionUtils;

    @Mock private HttpServletRequest mockHttpServletRequest;
    @Mock private HttpSession mockHttpSession;
    @Mock private SessionUtils mockSessionUtils;

    private ApptTypeData apptData;
    private ChildbirthData ovData;
    private DataSource ds;
    // files are finished
    private Childbirth testOV;
    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        mockSessionUtils = Mockito.mock(SessionUtils.class);
        ovc = Mockito.spy(new ChildbirthController(ds, mockSessionUtils));
        try {
            ChildbirthController ovc2 = new ChildbirthController();
        } catch (Exception e) {}
        try {
            ChildbirthController ovc3 = new ChildbirthController(ds);
        } catch (Exception e) {}
        Mockito.doNothing().when(ovc).printFacesMessage(Matchers.any(FacesMessage.Severity.class), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
//        Mockito.doNothing().when(ovc).redirectToBaseOfficeVisit();
        ovData = new ChildbirthMySQL(ds);

        // Setup test ObstetricsOfficeVisit
        testOV = new Childbirth();
        testOV.setVisitID(1L);
        testOV.setDate(LocalDateTime.now());
        testOV.setSex("Male");
        testOV.setName("Brian Roper");
        testOV.setDeliveryType("Vaginal Delivery");


        // Initialize a office visit controller with null data source
        ovcWithNullDataSource = new ChildbirthController(null, null);

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
    public void testDeleteWithInvalidID() throws DBException{
        ovc.delete(new Long(-1));
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }


    @Test
    public void testAddReturnGeneratedWithNull() throws DBException{
        ovc.addReturnGeneratedId(null);
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testDeliveryTypes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Vaginal Delivery");
        list.add("Vaginal Delivery Vacuum Assist");
        list.add("Vaginal Delivery Forceps Assist");
        list.add("Caesarean Section");
        list.add("Miscarriage");
        Assert.assertEquals(list, ovc.getDeliveryTypes());
    }

    @Test
    public void testSexes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        Assert.assertEquals(list, ovc.getSexes());
    }

    @Test
    public void testID() {
        Long birthID = ovc.addReturnGeneratedId(testOV);
        Childbirth c = ovc.getChildbirthByID(birthID.toString());
        Assert.assertEquals(c.getVisitID(), testOV.getVisitID());
        Assert.assertEquals(c.getName(), testOV.getName());
        Assert.assertEquals(c.getDeliveryType(), testOV.getDeliveryType());
        Childbirth c2 = ovc.getChildbirthByID("s");
        Mockito.verify(ovc).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_ERROR), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testBirthsPerVisit() {
        Long birthID = ovc.addReturnGeneratedId(testOV);
        Childbirth c = new Childbirth();
        c.setVisitID(1L);
        c.setDate(LocalDateTime.now());
        c.setSex("Male");
        c.setName("Brian Roper");
        c.setDeliveryType("Vaginal Delivery");
        List<Childbirth> list = ovc.getChildbirthsForVisit("1");
        Assert.assertTrue(list.size() >= 2);

    }

    @Test
    public void testSelect() throws Exception {
        Childbirth c = ovc.getSelectedChildbirth();
        Assert.assertTrue(c == null);
    }

    @Test
    public void testCurrentPatient() {
        boolean c = ovc.CurrentVisitHasChildbirths();
        Assert.assertTrue(c == false);
    }

    @Test
    public void testGetChildbirthsForCurrentVisit() {
        List<Childbirth> list = ovc.getChildbirthsForCurrentVisit();
        Assert.assertTrue(list.size() == 0);
    }





    @Test
    public void testRetrieveObstetricsOfficeVisit() throws DBException {
        ovc.addReturnResult(testOV);
        Assert.assertTrue("Office visit should be added successfully", ovc.addReturnResult(testOV));

        // Get the visit ID from the DB
        List<Childbirth> all = ovData.getAll();
        long birthID = -1;
        for (int i = 0; i < all.size(); i++) {
            Childbirth ovI = all.get(i);
            birthID = ovI.getBirthID();
        }
        Assert.assertNotEquals(-1L, birthID);

        testOV.setBirthID(birthID);

        ovc.edit(testOV);

    }
}
