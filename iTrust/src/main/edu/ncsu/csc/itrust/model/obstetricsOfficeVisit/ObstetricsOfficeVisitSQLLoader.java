package edu.ncsu.csc.itrust.model.obstetricsOfficeVisit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.obstetricsOfficeVisit.ObstetricsOfficeVisit;
import edu.ncsu.csc.itrust.model.user.patient.Patient;

public class ObstetricsOfficeVisitSQLLoader implements SQLLoader<ObstetricsOfficeVisit>{
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ObstetricsOfficeVisit> loadList(ResultSet rs) throws SQLException {
        ArrayList<ObstetricsOfficeVisit> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;

    }

    public Patient loadPatient(ResultSet rs) throws SQLException {
        Patient ret = new Patient();
        long mid = rs.getLong("MID");
        String fn = rs.getString("firstName");
        String ln = rs.getString("lastName");
        String bloodType = rs.getString("BloodType");
        ret.setFirstName(fn);
        ret.setLastName(ln);
        ret.setBloodType(bloodType);
        try {
            ret.setMID(mid);
        } catch (Exception e) {
            throw new SQLException("Incorrect value for MID stored in MySQL database");
        }
        return ret;
    }

    public List<Patient> loadPatientList(ResultSet rs) throws SQLException{
        ArrayList<Patient> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadPatient(rs));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObstetricsOfficeVisit loadSingle(ResultSet rs) throws SQLException {
        ObstetricsOfficeVisit retVisit = new ObstetricsOfficeVisit();
        retVisit.setVisitID(Long.parseLong(rs.getString("visitID")));
        retVisit.setPatientMID(Long.parseLong(rs.getString("patientMID")));
        retVisit.setDate(rs.getTimestamp("visitDate").toLocalDateTime());

        retVisit.setNumOfWeeksPregnant(getIntOrNull(rs, "pregnantWeeks"));
        retVisit.setWeight(getFloatOrNull(rs, "weight"));
        retVisit.setBloodPressure(rs.getString("blood_pressure"));
        retVisit.setFetalHeartRate(getIntOrNull(rs, "FHR"));
        retVisit.setMultiplePregnancy(getIntOrNull(rs, "multiPregnancy"));
        retVisit.setLowLyingPlacenta(rs.getString("lowLyingPlacenta"));


        return retVisit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ObstetricsOfficeVisit ov, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO obstetricsOfficeVisit(patientMID, visitDate, pregnantWeeks, weight, "
                    + "blood_pressure, FHR, multiPregnancy, lowLyingPlacenta) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        } else {
            long id = ov.getVisitID();
            stmt = "UPDATE obstetricsOfficeVisit SET patientMID=?, "
                    + "visitDate=?, "
                    + "pregnantWeeks=?, "
                    + "weight=?, "
                    + "blood_pressure=?, "
                    + "FHR=?, "
                    + "multiPregnancy=?, "
                    + "lowLyingPlacenta=? "
                    + "WHERE visitID=" + id + ";";
        }
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, ov.getPatientMID());
        Timestamp ts = Timestamp.valueOf(ov.getDate());

        ps.setTimestamp(2, ts);

        // Patient health metrics
        setIntOrNull(ps, 3, ov.getNumOfWeeksPregnant());
        setFloatOrNull(ps, 4, ov.getWeight());
        ps.setString(5, ov.getBloodPressure());
        setIntOrNull(ps, 6, ov.getFetalHeartRate());
        setIntOrNull(ps, 7, ov.getMultiplePregnancy());
//        System.out.println(ov.getLowLyingPlacenta());
        ps.setString(8, ov.getLowLyingPlacenta());

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
