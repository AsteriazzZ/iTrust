package edu.ncsu.csc.itrust.unit.controller.ultrasounds;


import edu.ncsu.csc.itrust.controller.ultrasounds.UltrasoundController;
import edu.ncsu.csc.itrust.controller.ultrasounds.UltrasoundForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.ultrasounds.Ultrasound;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.Month;

public class UltrasoundFormTest {
    private UltrasoundForm uf;
    @Spy
    private UltrasoundForm spyuf;
    @Spy
    private UltrasoundController uc;
    private TestDataGenerator gen;
    private DataSource ds;
    private Ultrasound u;
    @Spy
    private UltrasoundController mockuc;

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
        gen = new TestDataGenerator();
        uc = Mockito.spy(new UltrasoundController(ds));
        mockuc = Mockito.mock(UltrasoundController.class);

        // Set up office visits
        createUltrasound();

        // Generate data
        gen = new TestDataGenerator();
        gen.appointmentType();
        gen.hospitals();
        gen.patient1();
        gen.uc51();
        gen.uc52();
        gen.uc53SetUp();
    }

    private void createUltrasound() {
        u = new Ultrasound();
        u.setPatientMID(1L);
        u.setObstetricOfficeVisitID(100);
        u.setCrl(100);
        u.setBpd(100);
        u.setHc(100);
        u.setEfw(100);
        u.setHl(100);
        u.setOfd(100);
        u.setFl(100);
        u.setAc(100);

    }

    @Test
    public void testGetUltrasoundID() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setUltrasoundID(1L);
        Assert.assertTrue(1L == uf.getUltrasoundID());
    }

    @Test
    public void testGetPatientMID() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setPatientMID(2L);
        Assert.assertTrue(2L == uf.getPatientMID());
    }

    @Test
    public void testGetObstetricsVisitID() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setObstetricOfficeVisitID(200);
        Assert.assertTrue(200 == uf.getObstetricOfficeVisitID());
    }

    @Test
    public void testGetCRL() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setCrl(10);
        Assert.assertTrue(10 == uf.getCrl());
    }
    @Test
    public void testGetBpd() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setBpd(10);
        Assert.assertTrue(10 == uf.getBpd());
    }

    @Test
    public void testGetHc() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setHc(10);
        Assert.assertTrue(10 == uf.getHc());
    }

    @Test
    public void testGetFl() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setFl(10);
        Assert.assertTrue(10 == uf.getFl());
    }

    @Test
    public void testGetOfd() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setOfd(10);
        Assert.assertTrue(10 == uf.getOfd());
    }
    @Test
    public void testGetAc() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setAc(10);
        Assert.assertTrue(10 == uf.getAc());
    }
    @Test
    public void testGetHl() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setHl(10);
        Assert.assertTrue(10 == uf.getHl());
    }
    @Test
    public void testGetEfw() {
        Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
        uf = new UltrasoundForm(uc);
        uf.setEfw(10);
        Assert.assertTrue(10 == uf.getEfw());
    }


    @Test
    public void testSubmit() {
        try {
            Mockito.when(mockuc.getSelectedUltrasound()).thenReturn(u);
            uf = new UltrasoundForm(mockuc);
            uf.submit();
        } catch (Exception e) {}

//        Assert.assertTrue(u.getObstetricOfficeVisitID().equals(100));
//        Assert.assertTrue(u.getOfd().equals(100));
//        Assert.assertTrue(u.getHl().equals(100));
//        Assert.assertTrue(u.getHc().equals(100));
//        Assert.assertTrue(u.getFl().equals(100));
//        Assert.assertTrue(u.getEfw().equals(100));
//        Assert.assertTrue(u.getCrl().equals(100));
//        Assert.assertTrue(u.getAc().equals(100));
//        Assert.assertTrue(u.getBpd().equals(100));



    }


}
