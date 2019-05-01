package edu.ncsu.csc.itrust.unit.model.obstetricVisit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.sql.DataSource;

import net.sf.cglib.core.Local;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ObstetricsVisitTest extends TestCase {
    private ObstetricVisit test;
    private TestDataGenerator gen;
    private DataSource ds;

    @Override
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        test = new ObstetricVisit();
        gen = new TestDataGenerator();
    }

    @Test
    public void testPatient() throws FileNotFoundException, IOException, SQLException {
        gen.patient1();
        test.setPatientMID(1L);
        Assert.assertEquals(new Long(1),test.getPatientMID());
    }

    @Test
    public void testDate() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setDate(testTime);
        Assert.assertTrue(ChronoUnit.MINUTES.between(testTime, test.getDate())<1);
    }

    @Test
    public void testLocation() {
        test.setLocationID("testLoc");
        Assert.assertTrue(test.getLocationID() == "testLoc");
    }

    @Test
    public void testAppointmentType() {
        test.setApptTypeID(12L);
        Assert.assertTrue(test.getApptTypeID() == 12L);
    }

    @Test
    public void testVisitID() {
        test.setVisitID(12L);
        Assert.assertTrue(test.getVisitID() == 12L);
    }

    @Test
    public void testNotes() {
        test.setNotes("testNote");
        Assert.assertTrue(test.getNotes() == "testNote");
    }

    @Test
    public void testLMP() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setLMP(testTime);
        Assert.assertTrue(ChronoUnit.MINUTES.between(testTime, test.getLMP())<1);
    }

    @Test
    public void testEDD() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setLMP(testTime);
        test.setEDD();
        LocalDateTime comparison = test.getLMP().plusDays(280);
        Assert.assertEquals(test.getEDD(), comparison);
    }

    @Test
    public void testWeeksPregnant() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setLMP(testTime.minusDays(7));
        test.setDate(testTime);
        test.setWeeksPregnant();

        Assert.assertTrue(test.getWeeksPregnant() == 1L);
    }


}
