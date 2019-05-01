package edu.ncsu.csc.itrust.model.childbirth;

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
 * Executes SQL queries for childbirth records
 */
@ManagedBean
public class ChildbirthMySQL implements Serializable, ChildbirthData {
    @Resource(name = "jdbc/itrust2")
    private ChildbirthSQLLoader cvLoader;
    private static final long serialVersionUID = -8231210448583854595L;
    private DataSource ds;

    public ChildbirthMySQL() throws DBException {
        cvLoader = new ChildbirthSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
    }


    public ChildbirthMySQL(DataSource ds) {
        cvLoader = new ChildbirthSQLLoader();
        this.ds = ds;
    }

    /**
     * Returns list of childbirths corresponding to a visitID
     * @param visitID
     * @return
     * @throws DBException
     */
    @Override
    public List<Childbirth> getChildbirthsForVisit(Long visitID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(visitID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM childbirths WHERE visitID=?");

                pstring.setLong(1, visitID);

                results = pstring.executeQuery();

                final List<Childbirth> visitList = cvLoader.loadList(results);
                return visitList;
            } catch (SQLException e) { throw new DBException(e);
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
     * adds a childbirth record to db
     * @param cv
     * @return
     * @throws DBException
     */
    @Override
    public boolean add(Childbirth cv) throws DBException {
        return addReturnGeneratedId(cv) >= 0;
    }

    /**
     * adds a childbirth record to database and returns birthID of record
     * @param cv
     * @return
     * @throws DBException
     */
    @Override
    public long addReturnGeneratedId(Childbirth cv) throws DBException {
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
        } catch (SQLException e) { throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return generatedId;
    }

    /**
     * deletes childbirth record from db corresponding to birthID
     * @param birthID
     * @return
     * @throws DBException
     */
    @Override
    public boolean delete(Long birthID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;

        long generatedId = -1;
        try {
            conn = ds.getConnection();
            pstring = cvLoader.loadDeleteParameters(conn, pstring, birthID);

            boolean results = pstring.execute();
        } catch (SQLException e) { throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return true;
    }

    /**
     * returns childbirth record corresponding to birthID id
     * @param id
     * @return
     * @throws DBException
     */
    @Override
    public Childbirth getByID(long id) throws DBException {
        Childbirth ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<Childbirth> visitList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM childbirths WHERE birthID=?");

            pstring.setLong(1, id);

            results = pstring.executeQuery();

            /* May update with loader instead */
            visitList = cvLoader.loadList(results);
            if (visitList.size() > 0) {
                ret = visitList.get(0);
            }
        } catch (SQLException e) { throw new DBException(e);
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
        return ret;
    }

    /**
     * updates childbirth record corresponding to ov.birthID
     * @param ov
     * @return
     * @throws DBException
     */
    @Override
    public boolean update(Childbirth ov) throws DBException {
        boolean retval = false;
        Connection conn = null;
        PreparedStatement pstring = null;
        int results;

        try {
            conn = ds.getConnection();
            pstring = cvLoader.loadParameters(conn, pstring, ov, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        } catch (SQLException e) { throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }

    /**
     * gets all childbirth records in db
     * @return
     * @throws DBException
     */
    @Override
    public List<Childbirth> getAll() throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM childbirths");
            results = pstring.executeQuery();
            final List<Childbirth> childbirthList = cvLoader.loadList(results);
            return childbirthList;
        } catch (SQLException e) { throw new DBException(e);
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
    }

}
