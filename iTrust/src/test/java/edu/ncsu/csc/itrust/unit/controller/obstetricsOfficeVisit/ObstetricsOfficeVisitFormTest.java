package edu.ncsu.csc.itrust.unit.controller.obstetricsOfficeVisit;

import edu.ncsu.csc.itrust.controller.obstetricsOfficeVisit.ObstetricsOfficeVisitForm;
import edu.ncsu.csc.itrust.controller.obstetricsOfficeVisit.ObstetricsOfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.apptType.ApptType;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.mockito.InjectMocks;
import java.lang.reflect.Method;

public class ObstetricsOfficeVisitFormTest {
    @InjectMocks
    private ObstetricsOfficeVisitForm target;

    private ObstetricsOfficeVisitForm ovf;
    @Spy
    private ObstetricsOfficeVisitForm spyovf;
    @Spy
    private ObstetricsOfficeVisitController ovc;
    private ApptTypeData apptData;
    private TestDataGenerator gen;
    private DataSource ds;
    private ObstetricsOfficeVisit ov;
    @Spy
    private ObstetricsOfficeVisitController mockovc;

    private static final Float FLOAT_VALUE = 12.3F;
    private static final String BP_VALUE = "140/90";
    private static final String STRING_VALUE = "YES";
    private static final Integer INTEGER_VALUE = 1;
    private static final Integer HDL = 45;
    private static final Integer TRI = 350;
    private static final Integer LDL = 300;

    private static final Float FLOAT_TEST = 11.1F;
    private static final String BP_TEST = "190/80";
    private static final Integer INTEGER_TEST = 3;
    private static final Integer HDL_TEST = 60;
    private static final Integer TRI_TEST = 200;
    private static final Integer LDL_TEST = 500;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
//        apptData = new ApptTypeMySQLConverter(ds);
//        hData = new HospitalMySQLConverter(ds);
        gen = new TestDataGenerator();
        ovc = Mockito.spy(new ObstetricsOfficeVisitController(ds));
        mockovc = Mockito.mock(ObstetricsOfficeVisitController.class);

//        Method postConstruct =  ObstetricsOfficeVisitForm.class.getDeclaredMethod("init",null); // methodName,parameters
//        postConstruct.setAccessible(true);
//        postConstruct.invoke(target);

        // Set up office visits
        createObstetricOV();

        // Generate data
        gen = new TestDataGenerator();
        gen.appointmentType();
        gen.hospitals();
        gen.patient1();
        gen.uc51();
        gen.uc52();
        gen.uc53SetUp();
    }

    private void createObstetricOV() {
        ov = new ObstetricsOfficeVisit();
        ov.setPatientMID(1L);
        ov.setWeight(FLOAT_VALUE);
        ov.setNumOfWeeksPregnant(INTEGER_VALUE);
        ov.setMultiplePregnancy(INTEGER_VALUE);
        ov.setLowLyingPlacenta(STRING_VALUE);
        ov.setFetalHeartRate(INTEGER_VALUE);
        ov.setBloodPressure(BP_VALUE);
    }

//    private void createOVChild() {
//        ovChild = new OfficeVisit();
//        ovChild.setPatientMID(1L);
//        ovChild.setHeight(FLOAT_VALUE);
//        ovChild.setWeight(FLOAT_VALUE);
//        ovChild.setBloodPressure(BP_VALUE);
//        ovChild.setHouseholdSmokingStatus(INTEGER_VALUE);
//    }
//
//    private void createOVAdult() throws SQLException, FileNotFoundException, IOException, DBException {
//        ovAdult = new OfficeVisit();
//        gen.appointmentType();
//        List<ApptType> apptList = apptData.getAll();
//        if(apptList.size()>0){
//            ovAdult.setApptTypeID(apptList.get(0).getID());
//        }
//        ovAdult.setDate(LocalDateTime.now());
//        ovAdult.setPatientMID(1L);
//        ovAdult.setVisitID(1L);
//        ovAdult.setSendBill(true);
//        ovAdult.setHeight(FLOAT_VALUE);
//        ovAdult.setWeight(FLOAT_VALUE);
//        ovAdult.setBloodPressure(BP_VALUE);
//        ovAdult.setHouseholdSmokingStatus(INTEGER_VALUE);
//        ovAdult.setPatientSmokingStatus(INTEGER_VALUE);
//        ovAdult.setHDL(HDL);
//        ovAdult.setTriglyceride(TRI);
//        ovAdult.setLDL(LDL);
//    }

    @Test
    public void testGetVisitID() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        ovf.setVisitID(1L);
        Assert.assertTrue(1L == ovf.getVisitID());
    }

    @Test
    public void testGetPatientMID() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        Assert.assertTrue(ovf.getPatientMID().equals(1L));
        ovf.setPatientMID(2L);
        Assert.assertTrue(ovf.getPatientMID().equals(2L));
    }

    @Test
    public void testGetDate() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        LocalDateTime test = LocalDateTime.of(2010, Month.JANUARY, 1, 0, 0);
        ovf.setDate(test);
        Assert.assertTrue(ovf.getDate().equals(test));
    }

    @Test
    public void testGetNumberWeeksPreg() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        ovf.setNumOfWeeksPregnant(INTEGER_VALUE);
        Assert.assertEquals(INTEGER_VALUE, ovf.getNumOfWeeksPregnant());
    }
    @Test
    public void testGetWeight() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        Assert.assertTrue(ovf.getWeight().equals(FLOAT_VALUE));
        ovf.setWeight(FLOAT_TEST);
        Assert.assertTrue(ovf.getWeight().equals(FLOAT_TEST));
    }


    @Test
    public void testGetBP() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        Assert.assertTrue(ovf.getBloodPressure().equals(BP_VALUE));
        ovf.setBloodPressure(BP_TEST);
        Assert.assertTrue(ovf.getBloodPressure().equals(BP_TEST));
    }

    @Test
    public void testGetFetalHeart() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        Assert.assertTrue(ovf.getFetalHeartRate().equals(INTEGER_VALUE));
        ovf.setFetalHeartRate(INTEGER_TEST);
        Assert.assertTrue(ovf.getFetalHeartRate().equals(INTEGER_TEST));
    }

    @Test
    public void testGetMultiplePreg() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        Assert.assertTrue(ovf.getMultiplePregnancy().equals(INTEGER_VALUE));
        ovf.setMultiplePregnancy(INTEGER_TEST);
        Assert.assertTrue(ovf.getMultiplePregnancy().equals(INTEGER_TEST));
    }

    @Test
    public void testGetLowLyingP() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        Assert.assertTrue(ovf.getLowLyingPlacenta().equals(STRING_VALUE));
        ovf.setLowLyingPlacenta("NO");
        Assert.assertTrue(ovf.getLowLyingPlacenta().equals("NO"));
    }


    @Test
    public void testSubmit() {
        Mockito.when(mockovc.getSelectedVisit()).thenReturn(ov);
        ovf = new ObstetricsOfficeVisitForm(mockovc);
        ovf.submit();

        Assert.assertTrue(ov.getWeight().equals(FLOAT_VALUE));
        Assert.assertTrue(ov.getNumOfWeeksPregnant().equals(INTEGER_VALUE));
        Assert.assertTrue(ov.getMultiplePregnancy().equals(INTEGER_VALUE));
        Assert.assertTrue(ov.getLowLyingPlacenta().equals(STRING_VALUE));
        Assert.assertTrue(ov.getFetalHeartRate().equals(INTEGER_VALUE));
        Assert.assertTrue(ov.getBloodPressure().equals(BP_VALUE));
    }


}
