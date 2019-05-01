package edu.ncsu.csc.itrust.unit.model.Pregnancy;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PregnancyTest extends TestCase {
    private Pregnancy test;
    private TestDataGenerator gen;
    private DataSource ds;

    @Override
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        test = new Pregnancy();
        gen = new TestDataGenerator();
    }

    @Test
    public void testPatient() throws FileNotFoundException, IOException, SQLException {
        gen.patient1();
        test.setPatientMID(1L);
        Assert.assertEquals(new Long(1),test.getPatientMID());
    }

    @Test
    public void testPregnancyID() {
        test.setPregnancyID(12L);
        Assert.assertTrue(test.getPregnancyID() == 12L);
    }

    @Test
    public void testConceptionYear() {
        test.setConceptionYear(2002);
        Assert.assertTrue(test.getConceptionYear() == 2002);
    }

    @Test
    public void testLaborHours() {
        test.setLaborHours(10);
        Assert.assertTrue(test.getLaborHours() == 10);
    }

    @Test
    public void testWeeksPregnant() {
        test.setWeeksPregnant(40);
        Assert.assertTrue(test.getWeeksPregnant() == 40);
    }

    @Test
    public void testDeliveryType() {
        test.setDeliveryType("b");
        Assert.assertTrue(test.getDeliveryType() == "b");
    }

    @Test
    public void testWeightGain() {
        test.setWeightGain(10);
        Assert.assertTrue(test.getWeightGain() == 10);
    }

    @Test
    public void testNumOfChildren() {
        test.setNumberOfChildren(3);
        Assert.assertTrue(test.getNumberOfChildren() == 3);
    }






}
