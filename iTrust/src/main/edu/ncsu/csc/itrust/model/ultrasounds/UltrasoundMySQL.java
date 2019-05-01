package edu.ncsu.csc.itrust.model.ultrasounds;

import com.mysql.jdbc.Statement;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ValidationFormat;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@ManagedBean
public class UltrasoundMySQL implements Serializable, UltrasoundData
{
    @Resource(name = "jdbc/itrust2")
    private UltrasoundSQLLoader ultrasoundLoader;
    private static final long serialVersionUID = -8231210448583854595L;
    private DataSource ds;

    /**
     * Default constructor for UltrasoundMySQL.
     *
     * @throws DBException if there is a context lookup naming exception
     */
    public UltrasoundMySQL() throws DBException {
        ultrasoundLoader = new UltrasoundSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
//        validator = new OfficeVisitValidator(this.ds);
    }

    public UltrasoundMySQL(DataSource ds) {
        ultrasoundLoader = new UltrasoundSQLLoader();
        this.ds = ds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ultrasound> getUltrasoundsForPatient(Long patientID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM ultrasounds WHERE patientMID=?");

                pstring.setLong(1, patientID);

                results = pstring.executeQuery();

                final List<Ultrasound> ultrasounds = ultrasoundLoader.loadList(results);
                return ultrasounds;
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

    @Override
    public List<Ultrasound> getUltrasoundsForPatient(Long patientID, Long obstetricOfficeVisitID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM ultrasounds WHERE patientMID=? and obstetricOfficeVisitID=?");

                pstring.setLong(1, patientID);
                pstring.setLong(2, obstetricOfficeVisitID);

                results = pstring.executeQuery();

                final List<Ultrasound> ultrasounds = ultrasoundLoader.loadList(results);
                return ultrasounds;
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

    public String getUltrasoundImage(Long ultrasoundID) throws Exception{
        String ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM ultrasoundImages WHERE ultrasoundID=?");

            pstring.setLong(1, ultrasoundID);

            results = pstring.executeQuery();
            if (results.first()) {
                ret = results.getString("image");
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
    public boolean add(Ultrasound u) throws DBException {
        return addReturnGeneratedId(u) >= 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long addReturnGeneratedId(Ultrasound u) throws DBException {
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
            pstring = ultrasoundLoader.loadParameters(conn, pstring, u, true);
            int results = pstring.executeUpdate();
            if (results != 0) {
                ResultSet generatedKeys = pstring.getGeneratedKeys();
                if(generatedKeys.next()) {
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
    public List<Ultrasound> getAll() throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM ultrasounds");
            results = pstring.executeQuery();
            final List<Ultrasound> ultrasoundList = ultrasoundLoader.loadList(results);
            return ultrasoundList;
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
    public Ultrasound getByID(long id) throws DBException {
        Ultrasound ret = null;
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        List<Ultrasound> ultrasoundList = null;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("SELECT * FROM ultrasounds WHERE ultrasoundID=?");

            pstring.setLong(1, id);

            results = pstring.executeQuery();

            /* May update with loader instead */
            ultrasoundList = ultrasoundLoader.loadList(results);
            if (ultrasoundList.size() > 0) {
                ret = ultrasoundList.get(0);
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
    public boolean update(Ultrasound p) throws DBException {
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
            pstring = ultrasoundLoader.loadParameters(conn, pstring, p, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }

    public void addUltrasoundImage(Long ultrasoundID, String encodedImage) throws SQLException{
        Connection conn = null;
        PreparedStatement ps = null;
        conn = ds.getConnection();
        String stmt = "";
        stmt = "INSERT INTO ultrasoundImages(ultrasoundID, image)"
                    + "VALUES (?, ?);";

        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, ultrasoundID);
        ps.setString(2, encodedImage);

        ps.executeUpdate();
    }
}
