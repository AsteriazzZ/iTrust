package edu.ncsu.csc.itrust.unit.model.childbirth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.childbirth.Childbirth;
import edu.ncsu.csc.itrust.model.childbirth.ChildbirthMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class ChildbirthSQLLoaderTest extends TestCase {
    private DataSource ds;
    private ChildbirthMySQL ovsql;
    private TestDataGenerator gen;

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private ChildbirthMySQL mockOvsql;

    @Override
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ovsql = new ChildbirthMySQL(ds);

        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc51();

        mockDataSource = Mockito.mock(DataSource.class);
        mockOvsql = new ChildbirthMySQL(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
    }

    @Test
    public void testAdd() throws Exception {
        Childbirth cv = new Childbirth();
        cv.setVisitID(1L);
        cv.setDate(LocalDateTime.now());
        cv.setSex("Male");
        cv.setName("Brian Roper");
        cv.setDeliveryType("Vaginal Delivery");
        Long id = ovsql.addReturnGeneratedId(cv);
        assertTrue(id > 0);
        Childbirth cv2 = ovsql.getByID(id);
        assertEquals(cv2.getVisitID(), cv.getVisitID());

    }

    @Test
    public void testUpdate() throws Exception {
        Childbirth cv = new Childbirth();
        cv.setVisitID(1L);
        cv.setDate(LocalDateTime.now());
        cv.setSex("Male");
        cv.setName("Brian Roper");
        cv.setDeliveryType("Vaginal Delivery");
        Long id = ovsql.addReturnGeneratedId(cv);
        cv.setBirthID(id);
        cv.setVisitID(2L);
        assertTrue(ovsql.update(cv));
        assertTrue(ovsql.delete(id));

    }

}
