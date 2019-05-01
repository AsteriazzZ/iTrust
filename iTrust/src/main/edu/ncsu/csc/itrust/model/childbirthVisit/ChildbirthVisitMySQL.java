package edu.ncsu.csc.itrust.model.childbirthVisit;

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
 * class to execute childbirth visit record queries
 */
@ManagedBean
public class ChildbirthVisitMySQL implements Serializable, ChildbirthVisitData {
    @Resource(name = "jdbc/itrust2")
    private ChildbirthVisitSQLLoader cvLoader;
    private static final long serialVersionUID = -8231210448583854595L;
    private DataSource ds;

    public ChildbirthVisitMySQL() throws DBException {
        cvLoader = new ChildbirthVisitSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
    }

    public ChildbirthVisitMySQL(DataSource ds) {
        cvLoader = new ChildbirthVisitSQLLoader();
        this.ds = ds;
    }

    /**
     * gets all childbirth records for a certain patientID
     * @param patientID
     * @return
     * @throws DBException
     */
    @Override
    public List<ChildbirthVisit> getChildbirthVisitsForPatient(Long patientID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM childbirthVisits WHERE patientMID=?");

                pstring.setLong(1, patientID);

                results = pstring.executeQuery();

                final List<ChildbirthVisit> visitList = cvLoader.loadList(results);
                return visitList;
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (SQLException e) { throw new DBException(e);
                } finally {
                    DBUtil.closeConnection(conn, pstring);

                }
            }
        } else {
            return null;
        }

    }

    /**
     * adds a childbirth visit record to db
     * @param cv
     * @return
     * @throws DBException
     */
    @Override
    public boolean add(ChildbirthVisit cv) throws DBException {
        return addReturnGeneratedId(cv) >= 0;
    }

    /**
     * adds a childbirth visit record to db and returns generated visitID
     */
    @Override
    public long addReturnGeneratedId(ChildbirthVisit cv) throws DBException {
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
            pstring = cvLoader.loadParameters(conn, pstring, cv, true);

            int results = pstring.executeUpdate();
            if (results != 0) {
                ResultSet generatedKeys = pstring.getGeneratedKeys();
                if(generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) { throw new DBException(e); } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return generatedId;
    }

    /**
     * gets all childbirth visits in db
     * @return
     * @throws DBException
     */
    @Override
    public List<ChildbirthVisit> getAll() throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM childbirthVisits");
            results = pstring.executeQuery();
            final List<ChildbirthVisit> visitList = cvLoader.loadList(results);
            return visitList;
        } catch (SQLException e) { throw new DBException(e); } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) { throw new DBException(e); } finally {
                DBUtil.closeConnection(conn, pstring);
            }
        }
    }

    /**
     * gets childbirth visit record based off visitID
     * @param id
     * @return
     * @throws DBException
     */
    @Override
    public ChildbirthVisit getByID(long id) throws DBException {
        ChildbirthVisit ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<ChildbirthVisit> visitList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM childbirthVisits WHERE visitID=?");

            pstring.setLong(1, id);

            results = pstring.executeQuery();

            /* May update with loader instead */
            visitList = cvLoader.loadList(results);
            if (visitList.size() > 0) {
                ret = visitList.get(0);
            }
        } catch (SQLException e) { throw new DBException(e); } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException e) { throw new DBException(e); } finally {

                DBUtil.closeConnection(conn, pstring);
            }
        }
        return ret;
    }

    /**
     * updates childbirth visit record corresponding to ov.visitID
     * @param ov
     * @return
     * @throws DBException
     */
    @Override
    public boolean update(ChildbirthVisit ov) throws DBException {
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
            pstring = cvLoader.loadParameters(conn, pstring, ov, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        } catch (SQLException e) { throw new DBException(e); } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }

    /**
     * removes childbirth visit from db corresponding to visitID
     * @param visitID
     * @return
     * @throws DBException
     */
    @Override
    public boolean delete(Long visitID) throws DBException {
        boolean retval = false;
        Connection conn = null;
        PreparedStatement pstring = null;
//        try {
//            validator.validate(ov);
//        } catch (FormValidationException e1) {
//            throw new DBException(new SQLException(e1.getMessage()));
//        }
        boolean results;
        try {
            conn = ds.getConnection();
            pstring = cvLoader.loadDeleteParameters(conn, pstring, visitID);
            results = pstring.execute();

        } catch (SQLException e) { throw new DBException(e); } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return true;
    }
}