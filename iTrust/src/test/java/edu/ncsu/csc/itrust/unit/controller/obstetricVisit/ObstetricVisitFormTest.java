package edu.ncsu.csc.itrust.unit.controller.obstetricVisit;

import edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitController;
import edu.ncsu.csc.itrust.controller.obstetricVisit.ObstetricVisitForm;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
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

public class ObstetricVisitFormTest extends TestCase {

    private ObstetricVisitForm ovf;
    @Spy private ObstetricVisitForm spyovf;
    @Spy private ObstetricVisitController ovc;
    private ApptTypeData apptData;
    private TestDataGenerator gen;
    private DataSource ds;
    private HospitalData hData;
    private ObstetricVisit ov;
    @Spy private ObstetricVisitController mockovc;

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
        ovc = Mockito.spy(new ObstetricVisitController(ds));
        mockovc = Mockito.mock(ObstetricVisitController.class);

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
        ov = new ObstetricVisit();
        ov.setPatientMID(1L);
        ov.setVisitID(1L);
        ov.setDate(LocalDateTime.now());
        ov.setLMP(LocalDateTime.now().minusMonths(2));
        ov.setWeeksPregnant();
        ov.setNotes("hi");
        ov.setEDD();

    }

    @Test
    public void testGetVisitID() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.setVisitID(1L);
        Assert.assertTrue(1L == ovf.getVisitID());
    }

    @Test
    public void testGetPatientMID() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        Assert.assertTrue(ovf.getPatientMID().equals(1L));
        ovf.setPatientMID(2L);
        Assert.assertTrue(ovf.getPatientMID().equals(2L));
    }

    @Test
    public void testGetDate() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        LocalDateTime test = LocalDateTime.of(2010, Month.JANUARY, 1, 0, 0);
        ovf.setDate(test);
        Assert.assertTrue(ovf.getDate().equals(test));
    }

    @Test
    public void testGetLocationID() {
        final String locationId = "1";
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.setLocationID(locationId);
        Assert.assertEquals(locationId, ovf.getLocationID());
    }

    @Test
    public void testGetApptTypeID() {
        final Long apptTypeId = 1L;
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.setApptTypeID(apptTypeId);
        Assert.assertEquals(apptTypeId, ovf.getApptTypeID());
    }

    @Test
    public void testGetNotes() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.setNotes("abc");
        Assert.assertTrue(ovf.getNotes().equals("abc"));
    }

    @Test
    public void testInit() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.init();
    }

    @Test
    public void testSetLMP() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.setLMP(LocalDateTime.now());
    }

    @Test
    public void testSubmit() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.submit();
    }

    @Test
    public void testGetLMP() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        LocalDateTime dateTime = ovf.getLMP();
        Assert.assertNotNull(dateTime);
    }

    @Test
    public void testBadSubmit() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricVisitForm(mockovc);
        ovf.setVisitID(null);
        ovf.submit();
    }

}
