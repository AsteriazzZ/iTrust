package edu.ncsu.csc.itrust.unit.model.obstetricsOfficeVisit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsOfficeVisitTest extends TestCase {
    private ObstetricsOfficeVisit test;
    private TestDataGenerator gen;
    private DataSource ds;

    @Override
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        test = new ObstetricsOfficeVisit();
        gen = new TestDataGenerator();
    }

    @Test
    public void testDate() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setDate(testTime);
        Assert.assertTrue(ChronoUnit.MINUTES.between(testTime, test.getDate())<1);
    }

    @Test
    public void testPatient() throws FileNotFoundException, IOException, SQLException {
        gen.patient1();
        test.setPatientMID(1L);
        Assert.assertEquals(new Long(1),test.getPatientMID());
    }

    @Test
    public void testID() {
        test.setVisitID(1L);
        long check = test.getVisitID();
        Assert.assertEquals(1L, check);
    }

    @Test
    public void testWeeks() {
        test.setNumOfWeeksPregnant(5);
        Assert.assertEquals((Integer)5, test.getNumOfWeeksPregnant());
    }

    /** Tests weight methods. */
    @Test
    public void testWeight() {
        test.setWeight(1F);
        float check = test.getWeight();
        Assert.assertEquals(1F, check, .01);
    }


    /** Tests bloodPressure methods. */
    @Test
    public void testBloodPressure() {
        String bp = "140/90";
        test.setBloodPressure(bp);
        Assert.assertEquals(bp, test.getBloodPressure());
    }

    /** Tests FHR methods. */
    @Test
    public void testFHR() {
        test.setFetalHeartRate(120);
        Assert.assertEquals((Integer)120, test.getFetalHeartRate());
    }

    @Test
    public void testMultiplePregnancy() {
        test.setMultiplePregnancy(2);
        Assert.assertEquals((Integer)2, test.getMultiplePregnancy());
    }


    @Test
    public void testLowLyingPlacenta() {
        test.setLowLyingPlacenta("Yes");
        Assert.assertEquals("Yes", test.getLowLyingPlacenta());
    }


}
