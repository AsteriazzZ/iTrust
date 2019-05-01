package edu.ncsu.csc.itrust.unit.controller.childbirthVisit;

import edu.ncsu.csc.itrust.controller.childbirthVisit.ChildbirthVisitController;
import edu.ncsu.csc.itrust.controller.childbirthVisit.ChildbirthVisitForm;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.when;

public class ChildbirthVisitFormTest extends TestCase {

    private ChildbirthVisitForm ovf;
    @Spy private ChildbirthVisitForm spyovf;
    @Spy private ChildbirthVisitController ovc;
    private ApptTypeData apptData;
    private TestDataGenerator gen;
    private DataSource ds;
    private HospitalData hData;
    private ChildbirthVisit ov;
    private ChildbirthVisit ov2;
    @Spy private ChildbirthVisitController mockovc;

    private static final Float FLOAT_VALUE = 12.3F;
    private static final String BP_VALUE = "140/90";
    private static final Integer INTEGER_VALUE = 1;
    private static final Integer HDL = 45;
    private static final Integer TRI = 350;
    private static final Integer LDL = 300;


    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        apptData = new ApptTypeMySQLConverter(ds);
        hData = new HospitalMySQLConverter(ds);
        gen = new TestDataGenerator();
        ovc = Mockito.spy(new ChildbirthVisitController(ds));
        mockovc = Mockito.mock(ChildbirthVisitController.class);
        try {
            ChildbirthVisitForm cv2 = new ChildbirthVisitForm();
        } catch (Exception e) {}
        // Set up Obs Visit
        createOV();

        // Generate data
        gen = new TestDataGenerator();
        gen.appointmentType();
        gen.hospitals();
        gen.patient1();
        gen.uc51();
        gen.uc52();
        gen.uc53SetUp();
    }

    private void createOV() {
        ov = new ChildbirthVisit();
        ov.setPatientMID(1L);
        ov.setVisitID(1L);
        ov.setVisitDate(LocalDateTime.now());
        ov.setLocationID("1");
        ov.setPreScheduled(true);
        ov.setDeliveryType("Vaginal Delivery");
        ov.setPitocin(new Long(10));
        ov.setOxide(new Long(10));
        ov.setPethidine(new Long(10));
        ov.setMagnesium(new Long(10));
        ov.setRh(new Long(10));
        ov.setEpidural(new Long(10));
        ov2 = new ChildbirthVisit();
        ov2.setPatientMID(2L);
        ov2.setVisitID(2L);
        ov2.setVisitDate(LocalDateTime.now());
        ov2.setLocationID("1");
        ov2.setPreScheduled(true);
        ov2.setDeliveryType("Vaginal Delivery");
        ov2.setPitocin(null);
        ov2.setOxide(null);
        ov2.setPethidine(null);
        ov2.setMagnesium(null);
        ov2.setRh(null);
        ov2.setEpidural(null);
    }

    @Test
    public void testGetVisitID() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setVisitID(1L);
        Assert.assertTrue(1L == ovf.getVisitID());
    }

    @Test
    public void testGetPatientMID() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        Assert.assertTrue(ovf.getPatientMID().equals(1L));
        ovf.setPatientMID(2L);
        Assert.assertTrue(ovf.getPatientMID().equals(2L));
    }

    @Test
    public void testGetDate() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        LocalDateTime test = LocalDateTime.of(2010, Month.JANUARY, 1, 0, 0);
        ovf.setVisitDate(test);
        Assert.assertTrue(ovf.getVisitDate().equals(test));
    }

    @Test
    public void testGetLocationID() {
        final String locationId = "1";
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setLocationID(locationId);
        Assert.assertEquals(locationId, ovf.getLocationID());
    }

    @Test
    public void testGetPrescheduled() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setPreScheduled(true);
        Assert.assertEquals(true, ovf.getPreScheduled());
    }

    @Test
    public void testDelivery() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setDeliveryType("Vaginal Delivery");
        Assert.assertEquals("Vaginal Delivery", ovf.getDeliveryType());
    }

    @Test
    public void testAppt() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setApptType("Childbirth");
        Assert.assertEquals("Childbirth", ovf.getApptType());
    }

    @Test
    public void testPitocin() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setPitocin(1L);
        Assert.assertEquals(new Long(1), ovf.getPitocin());
    }

    @Test
    public void testOxide() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setOxide(1L);
        Assert.assertEquals(new Long(1), ovf.getOxide());
    }
    @Test
    public void testPethidine() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setPethidine(1L);
        Assert.assertEquals(new Long(1), ovf.getPethidine());
    }
    @Test
    public void testMagnesium() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setMagnesium(1L);
        Assert.assertEquals(new Long(1), ovf.getMagnesium());
    }
    @Test
    public void testRH() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setRh(1L);
        Assert.assertEquals(new Long(1), ovf.getRh());
    }
    @Test
    public void testEpidural() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setEpidural(1L);
        Assert.assertEquals(new Long(1), ovf.getEpidural());
    }

    @Test
    public void testSubmit() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.submit();
    }

    @Test
    public void testDelete() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.delete();
    }

    @Test
    public void testSubmit2() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov2);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.submit();
    }

    @Test
    public void testBadSubmit() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ChildbirthVisitForm(mockovc);
        ovf.setVisitID(null);
        ovf.submit();
    }


}
