package edu.ncsu.csc.itrust.unit.model.obstetricsOfficeVisit;

import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisitSQLLoader;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ObstetricsOfficeVisitSQLLoaderTest extends TestCase {
    private static String MOCK_COLUMN_NAME = "mock_column";
    private static Integer MOCK_COLUMN_INDEX = 0;
    private static Integer MOCK_INT_VALUE = 1031;
    private static Float MOCK_FLOAT_VALUE = 1031.1f;

    private ObstetricsOfficeVisitSQLLoader ovloader;

    @Mock
    private ResultSet mockIntValResultSet;
    @Mock
    private ResultSet mockIntNullResultSet;
    @Mock
    private ResultSet mockFloatValResultSet;
    @Mock
    private ResultSet mockFloatNullResultSet;
    @Mock
    private PreparedStatement mockPreparedStatement;

    @SuppressWarnings("null")
    @Override
    public void setUp() throws Exception {
        ovloader = new ObstetricsOfficeVisitSQLLoader();

        // Mock a ResultSet containing int value
        mockIntValResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockIntValResultSet.getInt(MOCK_COLUMN_NAME)).thenReturn(MOCK_INT_VALUE);
        Mockito.when(mockIntValResultSet.wasNull()).thenReturn(false);

        // Mock a ResultSet containing null int value
        mockIntNullResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockIntNullResultSet.getInt(MOCK_COLUMN_NAME)).thenReturn(0);
        Mockito.when(mockIntNullResultSet.wasNull()).thenReturn(true);

        // Mock a ResultSet containing int value
        mockFloatValResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockFloatValResultSet.getFloat(MOCK_COLUMN_NAME)).thenReturn(MOCK_FLOAT_VALUE);
        Mockito.when(mockFloatValResultSet.wasNull()).thenReturn(false);

        // Mock a ResultSet containing null int value
        mockFloatNullResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockFloatNullResultSet.getFloat(MOCK_COLUMN_NAME)).thenReturn(0.0f);
        Mockito.when(mockFloatNullResultSet.wasNull()).thenReturn(true);

        // Mock a PreparedStatement with a placeholder
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
    }

    @Test
    public void testGetIntOrNull() throws Exception {
        Assert.assertEquals(MOCK_INT_VALUE, ovloader.getIntOrNull(mockIntValResultSet, MOCK_COLUMN_NAME));
        Assert.assertEquals(null, ovloader.getIntOrNull(mockIntNullResultSet, MOCK_COLUMN_NAME));
    }

    @Test
    public void testGetFloatOrNull() throws Exception {
        Assert.assertEquals(MOCK_FLOAT_VALUE, ovloader.getFloatOrNull(mockFloatValResultSet, MOCK_COLUMN_NAME));
        Assert.assertEquals(null, ovloader.getFloatOrNull(mockFloatNullResultSet, MOCK_COLUMN_NAME));
    }

    @Test
    public void testSetIntOrNull() throws Exception {
        try {
            ovloader.setIntOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, MOCK_INT_VALUE);
        } catch (NullPointerException e) {
            fail();
        }

        try {
            ovloader.setIntOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, null);
        } catch (NullPointerException e) {
            fail();
        }
    }

    @Test
    public void testSetFloatOrNull() throws Exception {
        try {
            ovloader.setFloatOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, MOCK_FLOAT_VALUE);
        } catch (NullPointerException e) {
            fail();
        }

        try {
            ovloader.setFloatOrNull(mockPreparedStatement, MOCK_COLUMN_INDEX, null);
        } catch (NullPointerException e) {
            fail();
        }
    }

    @Test
    public void testLoadList() throws Exception {
        try {
            ovloader.loadList(mockIntValResultSet);
        } catch (NullPointerException e) {
            fail();
        }
    }

//    @Test
//    public void testLoadSingle() throws Exception {
//        mockFloatNullResultSet.s
//        Mockito.doReturn("1").when(Mockito.mock(ResultSet.class)).getString(Mockito.anyString());
//        ovloader.loadSingle(mockIntValResultSet);
//    }

}
