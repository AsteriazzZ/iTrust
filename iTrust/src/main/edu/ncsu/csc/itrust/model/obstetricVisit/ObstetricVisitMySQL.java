package edu.ncsu.csc.itrust.model.obstetricVisit;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;

/**
 * @author T819
 *
 */
@ManagedBean
public class ObstetricVisitMySQL implements Serializable, ObstetricVisitData {
    @Resource(name = "jdbc/itrust2")
    private ObstetricVisitSQLLoader ovLoader;
    private static final long serialVersionUID = -8231210448583854595L;
    private DataSource ds;
//    private OfficeVisitValidator validator;

    /**
     * Default constructor for ObstetricVisitMySQL.
     *
     * @throws DBException if there is a context lookup naming exception
     */
    public ObstetricVisitMySQL() throws DBException {
        ovLoader = new ObstetricVisitSQLLoader();
        try { Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) { throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage())); } }

    /**
     * Constructor for testing.
     *
     * @param ds
     */
    public ObstetricVisitMySQL(DataSource ds) {
        ovLoader = new ObstetricVisitSQLLoader();
        this.ds = ds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObstetricVisit> getObstetricVisitsForPatient(Long patientID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM obstetricsVisits WHERE patientMID=?");

                pstring.setLong(1, patientID);

                results = pstring.executeQuery();

                final List<ObstetricVisit> visitList = ovLoader.loadList(results);
                return visitList;
            } catch (SQLException e) { throw new DBException(e); }
            finally {
                try {
                    if (results != null) { results.close();
                    } }
                    catch (SQLException e) {
                    throw new DBException(e); }
                    finally {
                    DBUtil.closeConnection(conn, pstring);
                } }
        } else {
            return null;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(ObstetricVisit ov) throws DBException { return addReturnGeneratedId(ov) >= 0; }

    /**
     * {@inheritDoc}
     */
    @Override
    public long addReturnGeneratedId(ObstetricVisit ov) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
//        try {
//            validator.validate(ov);
//        } catch (FormValidationException e1) {
//            throw new DBException(new SQLException(e1.getMessage()));
//        }
        long generatedId = -1;
        try {
            conn = ds.getConnection();
            pstring = ovLoader.loadParameters(conn, pstring, ov, true);
            int results = pstring.executeUpdate();
            if (results != 0) {
                ResultSet generatedKeys = pstring.getGeneratedKeys();
                if(generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                }
            }
        }
        catch (SQLException e) { throw new DBException(e); }
        finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return generatedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObstetricVisit> getAll() throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM obstetricsVisits");
            results = pstring.executeQuery();
            final List<ObstetricVisit> visitList = ovLoader.loadList(results);
            return visitList;
        } catch (SQLException e) { throw new DBException(e); }
        finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) { throw new DBException(e); }
            finally {
                DBUtil.closeConnection(conn, pstring);
            } }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObstetricVisit getByID(long id) throws DBException {
        ObstetricVisit ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<ObstetricVisit> visitList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM obstetricsVisits WHERE visitID=?");

            pstring.setLong(1, id);

            results = pstring.executeQuery();

            /* May update with loader instead */
            visitList = ovLoader.loadList(results);
            if (visitList.size() > 0) {
                ret = visitList.get(0);
            }
        } catch (SQLException e) { throw new DBException(e); }
        finally {
            try {
                if (results != null) {
                    results.close();
                } }
                catch (SQLException e) { throw new DBException(e); }
                finally {
                DBUtil.closeConnection(conn, pstring);
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(ObstetricVisit ov) throws DBException {
        boolean retval = false;
        Connection conn = null;
        PreparedStatement pstring = null;

        int results;

        try {
            conn = ds.getConnection();
            pstring = ovLoader.loadParameters(conn, pstring, ov, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        } catch (SQLException e) { throw new DBException(e); }
        finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }

    /**
     * {@inheritDoc}
     */
    public LocalDate getPatientDOB(final Long patientMID) {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        java.sql.Date patientDOB = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT DateOfBirth FROM patients WHERE MID=?");
            pstring.setLong(1, patientMID);
            results = pstring.executeQuery();
            if (!results.next()) {
                return null;
            }
            patientDOB = results.getDate("DateOfBirth");
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                return null;
            } finally {
                DBUtil.closeConnection(conn, pstring);
            }
        }

        if (patientDOB == null) {
            return null;
        }

        return patientDOB.toLocalDate();
    }
}
