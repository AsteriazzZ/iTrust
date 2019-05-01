package edu.ncsu.csc.itrust.unit.model.ultrasound;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.model.ultrasounds.Ultrasound;
import edu.ncsu.csc.itrust.model.ultrasounds.UltrasoundMySQL;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UltrasoundMySQLTest extends TestCase {
    private DataSource ds;
    private UltrasoundMySQL ovsql;
    private TestDataGenerator gen;
    private Ultrasound test;
    private DAOFactory factory = TestDAOFactory.getTestInstance();

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private UltrasoundMySQL mockOvsql;

    @Override
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ovsql = new UltrasoundMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc51();

        mockDataSource = Mockito.mock(DataSource.class);
        mockOvsql = new UltrasoundMySQL(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        test = new Ultrasound();

        test.setPatientMID(1L);
//        test.setUltrasoundID((long) 9);
        test.setObstetricOfficeVisitID(1);
        test.setAc(2);
        test.setBpd(3);
        test.setCrl(4);
        test.setEfw(5);
        test.setFl(6);
        test.setHc(7);
        test.setHl(8);
        test.setOfd(9);

    }

    @Test
    public void testGetVisitsForPatient() throws Exception {
        test.setObstetricOfficeVisitID(1);
        ovsql.addReturnGeneratedId(test);
        List<Ultrasound> list101 = ovsql.getUltrasoundsForPatient(1L);
        assertTrue(list101.size()>0);
    }

    @Test
    public void testGetVisitsForPatientNegativeZero() throws Exception {
//        List<ObstetricVisit> list102 = ovsql.getObstetricVisitsForPatient(0L);
        List<Ultrasound> list103 = ovsql.getUltrasoundsForPatient(-1L);
    }

    @Test
    public void testAddReturnGeneratedId() throws Exception {
        Long res = ovsql.addReturnGeneratedId(test);
        assertTrue(res > 0);
        assertTrue(ovsql.add(test));
    }

    @Test
    public void testGetAll() throws Exception {
        ovsql.add(test);
        List<Ultrasound> list101 = ovsql.getAll();
        assertTrue(list101.size() > 0);
    }

    @Test
    public void testGetByID() throws Exception {
        Long id = ovsql.addReturnGeneratedId(test);
        Ultrasound ob = ovsql.getByID(id);
        assertNotNull(ob);
    }

    @Test
    public void testGetByIDNegativeZero() throws Exception {
        Ultrasound ob = ovsql.getByID(-1L);
        assertNull(ob);
    }

    @Test
    public void testUpdate() throws Exception {
        Ultrasound u = new Ultrasound();

        u.setPatientMID((long) 10000);
//        test.setUltrasoundID((long) 9);
        u.setObstetricOfficeVisitID(1);
        u.setAc(2);
        u.setBpd(3);
        u.setCrl(4);
        u.setEfw(5);
        u.setFl(6);
        u.setHc(7);
        u.setHl(8);
        u.setOfd(9);

        Long id = ovsql.addReturnGeneratedId(u);
        try{
            boolean check = ovsql.update(ovsql.getByID(id));}
        catch (Exception e){

        }
    }

    @Test
    public void testGetVisitsInvalidDataSource() throws Exception{
        Mockito.reset(mockDataSource);

        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
        List<Ultrasound> list = null;
        try{
            list = mockOvsql.getUltrasoundsForPatient(1L);
        }
        catch (Exception e){

        }
        assertNull(list);

    }
    @Test
    public void testGetVisitByIDInvalidDataSource() throws Exception{
        Mockito.reset(mockDataSource);

        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
        Ultrasound ov = null;
        try{
            ov = mockOvsql.getByID(1L);
        }
        catch (Exception e){

        }
        assertNull(ov);
    }

    @Test
    public void testGetAllInvalidDataSource() throws Exception{
        Mockito.reset(mockDataSource);

        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
        List<Ultrasound> ov = null;
        try{
            ov = mockOvsql.getAll();
        }
        catch (Exception e){

        }
        assertNull(ov);
    }

    @Test
    public void testAddandGetUltrasoundImage() throws Exception{
        Long id = ovsql.addReturnGeneratedId(test);
        ovsql.addUltrasoundImage(id, "test");
        assertEquals(ovsql.getUltrasoundImage(id), "test");
    }

    @Test
    public void testGetUltrasoundsForPatient()throws Exception{
        Long pid = test.getPatientMID();
        Integer ovid = test.getObstetricOfficeVisitID();
        Long id = ovsql.addReturnGeneratedId(test);
        ovsql.getUltrasoundsForPatient(pid, (long)ovid);
    }

}
