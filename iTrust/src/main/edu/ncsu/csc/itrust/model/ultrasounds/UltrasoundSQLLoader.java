package edu.ncsu.csc.itrust.model.ultrasounds;

import com.mysql.jdbc.Statement;
import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.pregnancies.Pregnancy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UltrasoundSQLLoader implements SQLLoader<Ultrasound> {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ultrasound> loadList(ResultSet rs) throws SQLException {
        ArrayList<Ultrasound> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ultrasound loadSingle(ResultSet rs) throws SQLException {
        Ultrasound ultrasound = new Ultrasound();
        ultrasound.setUltrasoundID(Long.parseLong(rs.getString("ultrasoundID")));
        ultrasound.setObstetricOfficeVisitID(Integer.parseInt(rs.getString("obstetricOfficeVisitID")));
        ultrasound.setPatientMID(Long.parseLong(rs.getString("patientMID")));
        ultrasound.setCrl(Integer.parseInt(rs.getString("crl")));
        ultrasound.setBpd(Integer.parseInt(rs.getString("bpd")));
        ultrasound.setHc(Integer.parseInt(rs.getString("hc")));
        ultrasound.setFl(Integer.parseInt(rs.getString("fl")));
        ultrasound.setOfd(Integer.parseInt(rs.getString("ofd")));
        ultrasound.setAc(Integer.parseInt(rs.getString("ac")));
        ultrasound.setHl(Integer.parseInt(rs.getString("hl")));
        ultrasound.setEfw(Integer.parseInt(rs.getString("efw")));

        return ultrasound;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Ultrasound ultrasound, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO ultrasounds(obstetricOfficeVisitID, patientMID, crl, bpd, hc, fl, ofd, ac, hl, efw)"
                    + "VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?);";
            ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, ultrasound.getObstetricOfficeVisitID());
            ps.setLong(2, ultrasound.getPatientMID());

            ps.setInt(3, ultrasound.getCrl());
            ps.setInt(4, ultrasound.getBpd());
            ps.setInt(5, ultrasound.getHc());
            ps.setInt(6, ultrasound.getFl());
            ps.setInt(7, ultrasound.getOfd());
            ps.setInt(8, ultrasound.getAc());
            ps.setInt(9, ultrasound.getHl());
            ps.setInt(10, ultrasound.getEfw());

        } else {
            long ultrasoundID = ultrasound.getUltrasoundID();
            int obstetricOfficeVisitID = ultrasound.getObstetricOfficeVisitID();
            stmt = "UPDATE ultrasounds SET patientMID=?, "
                    + "crl=?, "
                    + "bpd=?, "
                    + "hc=?, "
                    + "fl=?, "
                    + "ofd=?, "
                    + "ac=?, "
                    + "hl=?, "
                    + "efw=? "
                    + "WHERE ultrasoundID=" + ultrasoundID + " and obstetricOfficeVisitID=" + obstetricOfficeVisitID + ";";
            ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, ultrasound.getPatientMID());

            ps.setInt(2, ultrasound.getCrl());
            ps.setInt(3, ultrasound.getBpd());
            ps.setInt(4, ultrasound.getHc());
            ps.setInt(5, ultrasound.getFl());
            ps.setInt(6, ultrasound.getOfd());
            ps.setInt(7, ultrasound.getAc());
            ps.setInt(8, ultrasound.getHl());
            ps.setInt(9, ultrasound.getEfw());

        }


        return ps;
    }

    /**
     * Get the integer value if initialized in DB, otherwise get null.
     *
     * @param rs
     * 		ResultSet object
     * @param field
     * 		name of DB attribute
     * @return Integer value or null
     * @throws SQLException when field doesn't exist in the result set
     */
    public Integer getIntOrNull(ResultSet rs, String field) throws SQLException {
        Integer ret = rs.getInt(field);
        if (rs.wasNull()) {
            ret = null;
        }
        return ret;
    }

    /**
     * Get the float value if initialized in DB, otherwise get null.
     *
     * @param rs
     * 		ResultSet object
     * @param field
     * 		name of DB attribute
     * @return Float value or null
     * @throws SQLException when field doesn't exist in the result set
     */
    public Float getFloatOrNull(ResultSet rs, String field) throws SQLException {
        Float ret = rs.getFloat(field);
        if (rs.wasNull()) {
            ret = null;
        }
        return ret;
    }

    /**
     * Set integer placeholder in statement to a value or null
     *
     * @param ps
     * 		PreparedStatement object
     * @param index
     * 		Index of placeholder in the prepared statement
     * @param value
     * 		Value to set to placeholder, the value may be null
     * @throws SQLException
     * 		When placeholder is invalid
     */
    public void setIntOrNull(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    /**
     * Set float placeholder in statement to a value or null
     *
     * @param ps
     * 		PreparedStatement object
     * @param index
     * 		Index of placeholder in the prepared statement
     * @param value
     * 		Value to set to placeholder, the value may be null
     * @throws SQLException
     * 		When placeholder is invalid
     */
    public void setFloatOrNull(PreparedStatement ps, int index, Float value) throws SQLException {
        if (value == null) {
            ps.setNull(index, java.sql.Types.FLOAT);
        } else {
            ps.setFloat(index, value);
        }
    }



}
