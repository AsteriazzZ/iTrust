package edu.ncsu.csc.itrust.unit.model.Pregnancy;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;
import edu.ncsu.csc.itrust.model.pregnancies.PregnancyMySQL;
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

public class PregnancyMySQLTest extends TestCase {
	
	private DataSource ds;
	private PregnancyMySQL ovsql;
	private TestDataGenerator gen;
    private Pregnancy test;

	@Mock
	private DataSource mockDataSource;
	@Mock
	private Connection mockConnection;
	@Mock
	private PreparedStatement mockPreparedStatement;
	@Mock
	private ResultSet mockResultSet;
	private PregnancyMySQL mockOvsql;
	
	@Override
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		ovsql = new PregnancyMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.uc51();

		mockDataSource = Mockito.mock(DataSource.class);
		mockOvsql = new PregnancyMySQL(mockDataSource);
		
		mockConnection = Mockito.mock(Connection.class);
		mockResultSet = Mockito.mock(ResultSet.class);
		mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        test = new Pregnancy();
        test.setPatientMID(1L);
        test.setPregnancyID(1L);
        test.setConceptionYear(2005);
        test.setLaborHours(8);
        test.setWeeksPregnant(5);
        test.setWeightGain(50);
        test.setNumberOfChildren(1);
        test.setDeliveryType("g");

    }
	
	@Test
	public void testGetVisitsForPatient() throws Exception {
		List<Pregnancy> list101 = ovsql.getPregnanciesForPatient(101L);
		assertEquals(0, list101.size());
	}

    @Test
    public void testGetVisitsForPatientNegativeZero() throws Exception {
        List<Pregnancy> list101 = ovsql.getPregnanciesForPatient(101L);
        assertEquals(0, list101.size());
        List<Pregnancy> list102 = ovsql.getPregnanciesForPatient(0L);
        List<Pregnancy> list103 = ovsql.getPregnanciesForPatient(-1L);
    }
	
//	@Test
//	public void testGetPatientDOB() throws Exception {
//		LocalDate patient101DOB = ovsql.getPatientDOB(101L);
//		// patient with MID 101 is initialized with birthday as '2013-05-01'
//		LocalDate expectedPatientDOB = LocalDate.of(2013, 5, 1);
//		assertEquals(expectedPatientDOB, patient101DOB);
//
//		LocalDate invalidPatient106DOB = ovsql.getPatientDOB(106L);
//		assertNull(invalidPatient106DOB);
//
//		LocalDate invalidPatient107DOB = ovsql.getPatientDOB(107L);
//		assertNull(invalidPatient107DOB);
//	}

	@Test
	public void testAddReturnGeneratedId() throws Exception {
		Long res = ovsql.addReturnGeneratedId(test);
		assertTrue(res > 0);
	}

	@Test
	public void testGetAll() throws Exception {
		List<Pregnancy> list101 = ovsql.getAll();
		assertTrue(list101.size() > 0);
	}

    @Test
    public void testGetByID() throws Exception {
		Pregnancy ob = ovsql.getByID(1L);
        assertNotNull(ob);
    }

	@Test
	public void testUpdate() throws Exception {
		boolean check = ovsql.update(test);
	}

    @Test
    public void testGetByIDNegativeZero() throws Exception {
        Pregnancy ob = ovsql.getByID(-1L);
        assertNull(ob);
		Pregnancy ob1 = ovsql.getByID(0L);
        assertNull(ob1);
    }

//	@Test
//	public void testGetPatientDOBWithInvalidDataSource() throws Exception {
//		Mockito.doThrow(new SQLException("mock exception")).when(mockDataSource).getConnection();
//		LocalDate patient101DOB = mockOvsql.getPatientDOB(101L);
//		assertNull(patient101DOB);
//
//		Mockito.reset(mockDataSource);
//
//		Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
//		Mockito.when(mockConnection.prepareStatement(Matchers.any(String.class))).thenReturn(mockPreparedStatement);
//		Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//		Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).next();
//		Mockito.doThrow(new SQLException("mock exception")).when(mockResultSet).close();
//		LocalDate patient102DOB = mockOvsql.getPatientDOB(102L);
//		assertNull(patient102DOB);
//	}
}
