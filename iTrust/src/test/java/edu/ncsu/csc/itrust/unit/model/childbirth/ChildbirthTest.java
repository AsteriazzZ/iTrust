package edu.ncsu.csc.itrust.unit.model.childbirth;

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
import edu.ncsu.csc.itrust.model.childbirth.Childbirth;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

public class ChildbirthTest extends TestCase {
    private Childbirth test;
    private TestDataGenerator gen;
    private DataSource ds;

    @Override
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        test = new Childbirth();
        gen = new TestDataGenerator();
    }

    @Test
    public void testDate() {
        LocalDateTime testTime = LocalDateTime.now();
        test.setDate(testTime);
        Assert.assertTrue(ChronoUnit.MINUTES.between(testTime, test.getDate())<1);
    }

    @Test
    public void testID() {
        test.setVisitID(1L);
        long check = test.getVisitID();
        Assert.assertEquals(1L, check);
    }

    @Test
    public void testDelivery() {
        test.setDeliveryType("1");
        String check = test.getDeliveryType();
        Assert.assertEquals("1", check);
    }

    @Test
    public void testName() {
        test.setName("1");
        String check = test.getName();
        Assert.assertEquals("1", check);
    }

    @Test
    public void testSex() {
        test.setSex("1");
        String check = test.getSex();
        Assert.assertEquals("1", check);
    }
}
