package edu.ncsu.csc.itrust.unit.model.obstetricsOfficeVisit;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitMySQL;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.BloodType;
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import junit.framework.TestCase;

public class ObstetricsOfficeVisitMySQLTest extends TestCase {
    private DataSource ds;
    private ObstetricsOfficeVisitMySQL ovsql;
    private TestDataGenerator gen;
    private ObstetricsOfficeVisit test;
    private DAOFactory factory = TestDAOFactory.getTestInstance();

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private ObstetricsOfficeVisitMySQL mockOvsql;

    @Override
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ovsql = new ObstetricsOfficeVisitMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc51();

        mockDataSource = Mockito.mock(DataSource.class);
        mockOvsql = new ObstetricsOfficeVisitMySQL(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        test = new ObstetricsOfficeVisit();
        test.setPatientMID(1L);
        test.setDate(LocalDateTime.now());
//        test.setVisitID(100L);
        test.setBloodPressure("test");
        test.setFetalHeartRate(2);
        test.setLowLyingPlacenta("yes");
        test.setMultiplePregnancy(1);
        test.setNumOfWeeksPregnant(30);
        test.setWeight(new Float(20));

    }

    @Test
    public void testGetVisitsForPatient() throws Exception {
        ovsql.addReturnGeneratedId(test);
        List<ObstetricsOfficeVisit> list101 = ovsql.getObstetricsOfficeVisitsForPatient(1L);
        assertTrue(list101.size()>0);
    }

    @Test
    public void testGetVisitsForPatientNegativeZero() throws Exception {
//        List<ObstetricVisit> list102 = ovsql.getObstetricVisitsForPatient(0L);
        List<ObstetricsOfficeVisit> list103 = ovsql.getObstetricsOfficeVisitsForPatient(-1L);
    }

    @Test
    public void testAddReturnGeneratedId() throws Exception {
        Long res = ovsql.addReturnGeneratedId(test);
        assertTrue(res > 0);
        assertTrue(ovsql.add(test));
    }

    @Test
    public void testGetAll() throws Exception {
        List<ObstetricsOfficeVisit> list101 = ovsql.getAll();
		assertTrue(list101.size() > 0);
    }

    @Test
    public void testGetByID() throws Exception {
        Long id = ovsql.addReturnGeneratedId(test);
        ObstetricsOfficeVisit ob = ovsql.getByID(id);
        assertNotNull(ob);
    }

    @Test
    public void testGetByIDNegativeZero() throws Exception {
        ObstetricsOfficeVisit ob = ovsql.getByID(-1L);
        assertNull(ob);
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = ovsql.addReturnGeneratedId(test);
        test.setVisitID(id);
        boolean check = ovsql.update(test);
        assertTrue(check);
        ovsql.update(new ObstetricsOfficeVisit());
    }

    @Test
    public void testGetPatient() throws DBException {
        //Set up patient
        PatientDAO dao = new PatientDAO(factory);
        Long pid = dao.addEmptyPatient();
        PatientBean p = new PatientBean();
        p.setMID(pid);
        p.setFirstName("first");
        BloodType btype = BloodType.ONeg;
        p.setBloodType(btype);
        p.setBloodTypeStr(btype.toString());
        dao.editPatient(p, 900000000L);

        Patient testGet = ovsql.getPatient(pid);
        assertTrue(testGet.getFirstName().equals("first"));

    }

    @Test
    public void testGetVisitsInvalidDataSource() throws Exception{
        Mockito.reset(mockDataSource);

        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
        List<ObstetricsOfficeVisit> list = null;
        try{
            list = mockOvsql.getObstetricsOfficeVisitsForPatient(1L);
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
        ObstetricsOfficeVisit ov = null;
        try{
            ov = mockOvsql.getByID(1L);
        }
        catch (Exception e){

        }
        assertNull(ov);
    }

    @Test
    public void testGetPatientInvalidDataSource() throws Exception{
        Mockito.reset(mockDataSource);

        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
        Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
        Patient ov = null;
        try{
            ov = mockOvsql.getPatient(1L);
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
        List<ObstetricsOfficeVisit> ov = null;
        try{
            ov = mockOvsql.getAll();
        }
        catch (Exception e){

        }
        assertNull(ov);
    }



}
