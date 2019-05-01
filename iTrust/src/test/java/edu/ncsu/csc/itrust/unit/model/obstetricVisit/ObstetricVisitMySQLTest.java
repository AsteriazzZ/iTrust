package edu.ncsu.csc.itrust.unit.model.obstetricVisit;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisit;
import edu.ncsu.csc.itrust.model.obstetricVisit.ObstetricVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
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

public class ObstetricVisitMySQLTest extends TestCase {
	
	private DataSource ds;
	private ObstetricVisitMySQL ovsql;
	private TestDataGenerator gen;
    private ObstetricVisit test;

	@Mock
	private DataSource mockDataSource;
	@Mock
	private Connection mockConnection;
	@Mock
	private PreparedStatement mockPreparedStatement;
	@Mock
	private ResultSet mockResultSet;
	private ObstetricVisitMySQL mockOvsql;
	
	@Override
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		ovsql = new ObstetricVisitMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.uc51();

		mockDataSource = Mockito.mock(DataSource.class);
		mockOvsql = new ObstetricVisitMySQL(mockDataSource);
		
		mockConnection = Mockito.mock(Connection.class);
		mockResultSet = Mockito.mock(ResultSet.class);
		mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        test = new ObstetricVisit();
        test.setPatientMID(1L);
        test.setLMP(LocalDateTime.now().minusDays(70));
        test.setDate(LocalDateTime.now().minusDays(7));
        test.setVisitID(100L);
        test.setApptTypeID(10L);
        test.setLocationID("a");

    }
	
	@Test
	public void testGetVisitsForPatient() throws Exception {
		List<ObstetricVisit> list101 = ovsql.getObstetricVisitsForPatient(101L);
		assertEquals(0, list101.size());
	}

    @Test
    public void testGetVisitsForPatientNegativeZero() throws Exception {
        List<ObstetricVisit> list101 = ovsql.getObstetricVisitsForPatient(101L);
        assertEquals(0, list101.size());
        List<ObstetricVisit> list102 = ovsql.getObstetricVisitsForPatient(0L);
        List<ObstetricVisit> list103 = ovsql.getObstetricVisitsForPatient(-1L);
    }
	
	@Test
	public void testGetPatientDOB() throws Exception {
		LocalDate patient101DOB = ovsql.getPatientDOB(101L);
		// patient with MID 101 is initialized with birthday as '2013-05-01'
		LocalDate expectedPatientDOB = LocalDate.of(2013, 5, 1);
		assertEquals(expectedPatientDOB, patient101DOB);
		
		LocalDate invalidPatient106DOB = ovsql.getPatientDOB(106L);
		assertNull(invalidPatient106DOB);
		
		LocalDate invalidPatient107DOB = ovsql.getPatientDOB(107L);
		assertNull(invalidPatient107DOB);
	}

	@Test
	public void testAddReturnGeneratedId() throws Exception {
		Long res = ovsql.addReturnGeneratedId(test);
		assertTrue(res > 0);
	}

	@Test
	public void testGetAll() throws Exception {
		List<ObstetricVisit> list101 = ovsql.getAll();
//		assertTrue(list101.size() > 0);
	}

    @Test
    public void testGetByID() throws Exception {
        ObstetricVisit ob = ovsql.getByID(1L);
//        assertNotNull(ob);
    }

    @Test
    public void testGetByIDNegativeZero() throws Exception {
        ObstetricVisit ob = ovsql.getByID(-1L);
        assertNull(ob);
        ObstetricVisit ob1 = ovsql.getByID(0L);
        assertNull(ob1);
    }

    @Test
    public void testUpdate() throws Exception {
        boolean check = ovsql.update(test);
        assertFalse(check);
    }

	@Test
	public void testGetPatientDOBWithInvalidDataSource() throws Exception {
		Mockito.doThrow(new SQLException("mock exception")).when(mockDataSource).getConnection();
		LocalDate patient101DOB = mockOvsql.getPatientDOB(101L);
		assertNull(patient101DOB);
		
		Mockito.reset(mockDataSource);
		
		Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
		Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
		Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
		Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
		Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
		LocalDate patient102DOB = mockOvsql.getPatientDOB(102L);
		assertNull(patient102DOB);
	}
}
