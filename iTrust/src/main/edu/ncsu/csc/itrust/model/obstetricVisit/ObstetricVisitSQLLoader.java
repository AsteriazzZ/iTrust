package edu.ncsu.csc.itrust.model.obstetricVisit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;

public class ObstetricVisitSQLLoader implements SQLLoader<ObstetricVisit>{
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObstetricVisit> loadList(ResultSet rs) throws SQLException {
        ArrayList<ObstetricVisit> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObstetricVisit loadSingle(ResultSet rs) throws SQLException {
        ObstetricVisit retVisit = new ObstetricVisit();
        retVisit.setVisitID(Long.parseLong(rs.getString("visitID")));
        retVisit.setPatientMID(Long.parseLong(rs.getString("patientMID")));
        retVisit.setLocationID(rs.getString("locationID"));
        retVisit.setDate(rs.getTimestamp("visitDate").toLocalDateTime());
        retVisit.setApptTypeID(rs.getLong("apptTypeID"));
        retVisit.setNotes(rs.getString("notes"));
        retVisit.setLMP(rs.getTimestamp("lmp").toLocalDateTime());
        retVisit.setEDD();
        retVisit.setWeeksPregnant();
        return retVisit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricVisit ov, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO obstetricsVisits(patientMID, visitDate, locationID, apptTypeID, notes, lmp)"
                    + "VALUES (?, ?, ?, ?, ?, ?);";

        } else {
            long id = ov.getVisitID();
            stmt = "UPDATE obstetricsVisits SET patientMID=?, "
                    + "visitDate=?, "
                    + "locationID=?, "
                    + "apptTypeID=?, "
                    + "notes=?, "
                    + "lmp=? "
                    + "WHERE visitID=" + id + ";";
        }
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, ov.getPatientMID());
        Timestamp ts = Timestamp.valueOf(ov.getDate());

        ps.setTimestamp(2, ts);

        ps.setString(3, ov.getLocationID());
        ps.setLong(4, ov.getApptTypeID());
        String noteText = "";
        if (ov.getNotes() != (null)){
            noteText = ov.getNotes();
        }
        ps.setString(5, noteText);
        ps.setTimestamp(6, Timestamp.valueOf(ov.getLMP()));

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
