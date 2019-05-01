package edu.ncsu.csc.itrust.model.obstetricsOfficeVisit;


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
import edu.ncsu.csc.itrust.model.user.patient.Patient;
import edu.ncsu.csc.itrust.model.user.patient.PatientMySQLConverter;
import edu.ncsu.csc.itrust.model.user.patient.PatientSQLConvLoader;

/**
 * @author seelder
 *
 */
@ManagedBean
public class ObstetricsOfficeVisitMySQL implements Serializable, ObstetricsOfficeVisitData {
    @Resource(name = "jdbc/itrust2")
    private ObstetricsOfficeVisitSQLLoader ovLoader;
    private static final long serialVersionUID = -8231210448583854595L;
    private DataSource ds;
    //private OfficeVisitValidator validator;

    /**
     * Default constructor for ObstetricsOfficeVisitMySQL.
     *
     * @throws DBException if there is a context lookup naming exception
     */
    public ObstetricsOfficeVisitMySQL() throws DBException {
        ovLoader = new ObstetricsOfficeVisitSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        //validator = new OfficeVisitValidator(this.ds);
    }

    /**
     * Constructor for testing.
     *
     * @param ds
     */
    public ObstetricsOfficeVisitMySQL(DataSource ds) throws Exception{
        ovLoader = new ObstetricsOfficeVisitSQLLoader();
        this.ds = ds;
//        validator = new ObstetricsOfficeVisitValidator(this.ds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObstetricsOfficeVisit> getObstetricsOfficeVisitsForPatient(Long patientID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM obstetricsOfficeVisit WHERE patientMID=?");

                pstring.setLong(1, patientID);

                results = pstring.executeQuery();

                final List<ObstetricsOfficeVisit> visitList = ovLoader.loadList(results);
                return visitList;
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (SQLException e) {
                    throw new DBException(e);
                } finally {
                    DBUtil.closeConnection(conn, pstring);

                }
            }
        } else {
            return null;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(ObstetricsOfficeVisit ov) throws DBException {
        return addReturnGeneratedId(ov) >= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long addReturnGeneratedId(ObstetricsOfficeVisit ov) throws DBException {
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
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return generatedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObstetricsOfficeVisit> getAll() throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM obstetricsOfficeVisit");
            results = pstring.executeQuery();
            final List<ObstetricsOfficeVisit> visitList = ovLoader.loadList(results);
            return visitList;
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {
                DBUtil.closeConnection(conn, pstring);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObstetricsOfficeVisit getByID(long id) throws DBException {
        ObstetricsOfficeVisit ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<ObstetricsOfficeVisit> visitList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM obstetricsOfficeVisit WHERE visitID=?");

            pstring.setLong(1, id);

            results = pstring.executeQuery();

            /* May update with loader instead */
            visitList = ovLoader.loadList(results);
            if (visitList.size() > 0) {
                ret = visitList.get(0);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {

                DBUtil.closeConnection(conn, pstring);
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(ObstetricsOfficeVisit ov) throws DBException {
        boolean retval = false;
        Connection conn = null;
        PreparedStatement pstring = null;
//        try {
//            validator.validate(ov);
//        } catch (FormValidationException e1) {
//            throw new DBException(new SQLException(e1.getMessage()));
//        }
        int results;

        try {
            conn = ds.getConnection();
            pstring = ovLoader.loadParameters(conn, pstring, ov, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        } catch (SQLException e) {
            throw new DBException(e);
        } catch (Exception e){

        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }


    public Patient getPatient(Long patientMID) throws DBException {
        Patient ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<Patient> patientList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM patients WHERE MID=?");

            pstring.setLong(1, patientMID);

            results = pstring.executeQuery();

            /* May update with loader instead */
            patientList = ovLoader.loadPatientList(results);
            if (patientList.size() > 0) {
                ret = patientList.get(0);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {

                DBUtil.closeConnection(conn, pstring);
            }
        }
        return ret;
    }

}
