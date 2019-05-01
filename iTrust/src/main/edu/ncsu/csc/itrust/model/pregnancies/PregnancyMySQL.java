package edu.ncsu.csc.itrust.model.pregnancies;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitSQLLoader;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitValidator;


@ManagedBean
public class PregnancyMySQL implements Serializable, PregnancyData
{
    @Resource(name = "jdbc/itrust2")
    private PregnancySQLLoader pregLoader;
    private static final long serialVersionUID = -8231210448583854595L;
    private DataSource ds;

    /**
     * Default constructor for PregnancyMySQL.
     *
     * @throws DBException if there is a context lookup naming exception
     */
    public PregnancyMySQL() throws DBException {
        pregLoader = new PregnancySQLLoader();
        try { Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) { throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage())); } }

    /**
     * Constructor for testing.
     *
     * @param ds
     */
    public PregnancyMySQL(DataSource ds) {
        pregLoader = new PregnancySQLLoader();
        this.ds = ds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pregnancy> getPregnanciesForPatient(Long patientID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM pregnancies WHERE patientMID=?");

                pstring.setLong(1, patientID);

                results = pstring.executeQuery();

                final List<Pregnancy> priorPregnancies = pregLoader.loadList(results);
                return priorPregnancies;
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
        } else {
            return null;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Pregnancy p) throws DBException { return addReturnGeneratedId(p) >= 0; }

    /**
     * {@inheritDoc}
     */
    @Override
    public long addReturnGeneratedId(Pregnancy p) throws DBException {
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
            pstring = pregLoader.loadParameters(conn, pstring, p, true);
            int results = pstring.executeUpdate();
            if (results != 0) {
                ResultSet generatedKeys = pstring.getGeneratedKeys();
                if(generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) { throw new DBException(e); }
        finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return generatedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pregnancy> getAll() throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM pregnancies");
            results = pstring.executeQuery();
            final List<Pregnancy> pregnancyList = pregLoader.loadList(results);
            return pregnancyList;
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
    public Pregnancy getByID(long id) throws DBException {
        Pregnancy ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<Pregnancy> pregnancyList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM pregnancies WHERE pregnancyID=?");

            pstring.setLong(1, id);

            results = pstring.executeQuery();

            /* May update with loader instead */
            pregnancyList = pregLoader.loadList(results);
            if (pregnancyList.size() > 0) {
                ret = pregnancyList.get(0);
            }
        } catch (SQLException e) { throw new DBException(e); }
        finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) { throw new DBException(e); }
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
    public boolean update(Pregnancy p) throws DBException {
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
            pstring = pregLoader.loadParameters(conn, pstring, p, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        } catch (SQLException e) { throw new DBException(e); }
        finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }

}
