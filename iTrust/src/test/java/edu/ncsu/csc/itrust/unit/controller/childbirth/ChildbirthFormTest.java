package edu.ncsu.csc.itrust.unit.controller.childbirth;

import edu.ncsu.csc.itrust.controller.childbirth.ChildbirthController;
import edu.ncsu.csc.itrust.controller.childbirth.ChildbirthForm;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.childbirth.Childbirth;
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

public class ChildbirthFormTest extends TestCase {
    private ChildbirthForm ovf;
    @Spy private ChildbirthForm spyovf;
    @Spy private ChildbirthController ovc;
    private ApptTypeData apptData;
    private TestDataGenerator gen;
    private DataSource ds;
    private HospitalData hData;
    private Childbirth ov;
    private Childbirth ov2;
    @Spy private ChildbirthController mockovc;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        apptData = new ApptTypeMySQLConverter(ds);
        hData = new HospitalMySQLConverter(ds);
        gen = new TestDataGenerator();
        ovc = Mockito.spy(new ChildbirthController(ds));
        mockovc = Mockito.mock(ChildbirthController.class);
        try {
            ChildbirthForm cv2 = new ChildbirthForm();
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
        ov = new Childbirth();
        ov.setVisitID(1L);
        ov.setBirthID(1L);
        ov.setDeliveryType("Vaginal Delivery");
        ov.setName("Brian Roper");
        ov.setSex("Male");
        ov2 = new Childbirth();
        ov2.setVisitID(1L);
        ov2.setBirthID(2L);
        ov2.setDeliveryType("Vaginal Delivery");
        ov2.setName("Janet Jackson");
        ov2.setSex("Female");
    }

    @Test
    public void testGetVisitID() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.setVisitID(1L);
        Assert.assertTrue(1L == ovf.getVisitID());
    }

    @Test
    public void testGetDate() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        LocalDateTime test = LocalDateTime.of(2010, Month.JANUARY, 1, 0, 0);
        ovf.setDate(test);
        Assert.assertTrue(ovf.getDate().equals(test));
    }

    @Test
    public void testBirthID() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.setBirthID(1L);
        Assert.assertTrue(1L == ovf.getBirthID());
    }

    @Test
    public void testDelivery() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.setDeliveryType("Vaginal Delivery");
        Assert.assertTrue("Vaginal Delivery" == ovf.getDeliveryType());
    }

    @Test
    public void testName() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.setName("Brian Roper");
        Assert.assertTrue("Brian Roper" == ovf.getName());
    }

    @Test
    public void testSex() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.setSex("Female");
        Assert.assertTrue("Female" == ovf.getSex());
    }

    @Test
    public void testSubmit() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.submit();
    }

    @Test
    public void testDelete() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.delete();
    }

    @Test
    public void testSubmit2() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov2);
        ovf = new ChildbirthForm(mockovc);
        ovf.submit();
    }

    @Test
    public void testBadSubmit() {
        Mockito.when(mockovc.getSelectedChildbirth()).thenReturn(ov);
        ovf = new ChildbirthForm(mockovc);
        ovf.setBirthID(-1L);
        ovf.submit();
    }


}
