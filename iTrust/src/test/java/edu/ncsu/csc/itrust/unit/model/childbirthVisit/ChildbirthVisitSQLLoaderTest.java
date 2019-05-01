package edu.ncsu.csc.itrust.unit.model.childbirthVisit;


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
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisit;
import edu.ncsu.csc.itrust.model.childbirthVisit.ChildbirthVisitMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;

public class ChildbirthVisitSQLLoaderTest extends TestCase {

    private DataSource ds;
    private ChildbirthVisitMySQL ovsql;
    private ChildbirthVisitMySQL ovsql2;
    private TestDataGenerator gen;

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private ChildbirthVisitMySQL mockOvsql;

    @Override
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ovsql = new ChildbirthVisitMySQL(ds);
        try {
            ovsql2 = new ChildbirthVisitMySQL();
        } catch (Exception e) {}
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc51();

        mockDataSource = Mockito.mock(DataSource.class);
        mockOvsql = new ChildbirthVisitMySQL(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
    }

    @Test
    public void testAdd() throws Exception {
        ChildbirthVisit cv = new ChildbirthVisit();
        cv.setPatientMID(1L);
        cv.setVisitDate(LocalDateTime.now());
        cv.setLocationID("1");
        cv.setPreScheduled(true);
        cv.setDeliveryType("Vaginal Delivery");
        cv.setPitocin(1L);
        cv.setPethidine(1L);
        cv.setOxide(1L);
        cv.setEpidural(1L);
        cv.setMagnesium(1L);
        cv.setRh(1L);
        Long id = ovsql.addReturnGeneratedId(cv);
        assertTrue(id > 0);
        ChildbirthVisit cv2 = ovsql.getByID(id);
        assertEquals(cv2.getPatientMID(), cv.getPatientMID());

    }

}
