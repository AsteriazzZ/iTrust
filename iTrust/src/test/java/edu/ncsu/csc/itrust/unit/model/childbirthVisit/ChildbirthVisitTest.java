package edu.ncsu.csc.itrust.unit.model.childbirthVisit;

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
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ChildbirthVisitTest extends TestCase {
    private ChildbirthVisit test;
    private TestDataGenerator gen;
    private DataSource ds;

    @Override
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        test = new ChildbirthVisit();
        gen = new TestDataGenerator();
    }

    @Test
    public void testDate() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setVisitDate(testTime);
        Assert.assertTrue(ChronoUnit.MINUTES.between(testTime, test.getVisitDate())<1);
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
    public void testLoc() {
        test.setLocationID("1");
        String check = test.getLocationID();
        Assert.assertEquals("1", check);
    }

    @Test
    public void testPrescheduled() {
        test.setPreScheduled(true);
        boolean check = test.getPreScheduled();
        Assert.assertEquals(true, check);
    }

    @Test
    public void testDelivery() {
        test.setDeliveryType("Vaginal Delivery");
        String check = test.getDeliveryType();
        Assert.assertEquals("Vaginal Delivery", check);
    }

    @Test
    public void testPitocin() {
        test.setPitocin(1L);
        long check = test.getPitocin();
        Assert.assertEquals(1L, check);
    }

    @Test
    public void testOxide() {
        test.setOxide(1L);
        long check = test.getOxide();
        Assert.assertEquals(1L, check);
    }

    @Test
    public void testPethidine() {
        test.setPethidine(1L);
        long check = test.getPethidine();
        Assert.assertEquals(1L, check);
    }

    @Test
    public void testMagnesium() {
        test.setMagnesium(1L);
        long check = test.getMagnesium();
        Assert.assertEquals(1L, check);
    }

    @Test
    public void testEpidural() {
        test.setEpidural(1L);
        long check = test.getEpidural();
        Assert.assertEquals(1L, check);
    }


}
