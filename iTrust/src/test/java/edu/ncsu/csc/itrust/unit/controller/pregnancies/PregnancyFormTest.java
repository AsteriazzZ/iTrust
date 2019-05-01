package edu.ncsu.csc.itrust.unit.controller.pregnancies;

import edu.ncsu.csc.itrust.controller.pregnancies.PregnancyForm;
import edu.ncsu.csc.itrust.controller.pregnancies.PregnancyController;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;
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

public class PregnancyFormTest extends TestCase {

    private PregnancyForm pf;
    @Spy private PregnancyForm spypf;
    @Spy private PregnancyController pc;
    private ApptTypeData apptData;
    private TestDataGenerator gen;
    private DataSource ds;
    private HospitalData hData;
    private Pregnancy p;
    @Spy private PregnancyController mockpc;

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
//        pc = Mockito.spy(new PregnancyController(ds));
        mockpc = Mockito.mock(PregnancyController.class);

        // Set up Pregnancy Visit
        createP();

        // Generate data
        gen = new TestDataGenerator();
        gen.appointmentType();
        gen.hospitals();
        gen.patient1();
        gen.uc51();
        gen.uc52();
        gen.uc53SetUp();
    }
    private void createP() {
        p = new Pregnancy();
        p.setPregnancyID(1L);
        p.setPatientMID(1L);
        p.setConceptionYear(2018);
        p.setWeeksPregnant(47);
        p.setLaborHours(10);
        p.setWeightGain(5);
        p.setDeliveryType("a");
        p.setNumberOfChildren(2);

    }

    @Test
    public void testGetPregnancyID() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setPregnancyID(1L);
        Assert.assertTrue(1L == pf.getPregnancyID());
    }

    @Test
    public void testGetPatientMID() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        Assert.assertTrue(pf.getPatientMID().equals(1L));
        pf.setPatientMID(2L);
        Assert.assertTrue(pf.getPatientMID().equals(2L));
    }

    @Test
    public void testGetConceptionYear() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setConceptionYear(2003);
        Assert.assertTrue(pf.getConceptionYear().equals(2003));
    }

    @Test
    public void testGetLaborHours() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setLaborHours(10);
        Assert.assertTrue(pf.getLaborHours() == 10);
    }

    @Test
    public void testGetWeeksPregnant() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setWeeksPregnant(37);
        Assert.assertTrue(pf.getWeeksPregnant() == 37);
    }

    @Test
    public void testGetDeliveryType() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setDeliveryType("a");
        Assert.assertTrue(pf.getDeliveryType().equals("a"));
    }

    @Test
    public void testGetWeightGain() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setWeightGain(5);
        Assert.assertTrue(pf.getWeightGain().equals(5));
    }

    @Test
    public void testGetNumChildren() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setNumberOfChildren(2);
        Assert.assertTrue(pf.getNumberOfChildren().equals(2));
    }

    @Test
    public void testSubmit() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.submit();
    }

    @Test
    public void testBadSubmit() {
        Mockito.when(mockpc.getSelectedPregnancy()).thenReturn(p);
        pf = new PregnancyForm(mockpc);
        pf.setPregnancyID(null);
        pf.submit();
    }

}
