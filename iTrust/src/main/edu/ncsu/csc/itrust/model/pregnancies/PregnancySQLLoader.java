package edu.ncsu.csc.itrust.model.pregnancies;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class PregnancySQLLoader implements SQLLoader<Pregnancy>{
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Pregnancy> loadList(ResultSet rs) throws SQLException {
        ArrayList<Pregnancy> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pregnancy loadSingle(ResultSet rs) throws SQLException {
        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setPregnancyID(Long.parseLong(rs.getString("pregnancyID")));
        pregnancy.setPatientMID(Long.parseLong(rs.getString("patientMID")));
        pregnancy.setConceptionYear(Integer.parseInt(rs.getString("conceptionYear")));
        pregnancy.setWeeksPregnant(Integer.parseInt(rs.getString("weeksPregnant")));
        pregnancy.setLaborHours(Integer.parseInt(rs.getString("laborHours")));
        pregnancy.setWeightGain(Integer.parseInt(rs.getString("weightGain")));
        pregnancy.setDeliveryType(rs.getString("deliveryType"));
        pregnancy.setNumberOfChildren(rs.getInt("numberOfChildren"));
        return pregnancy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Pregnancy pregnancy, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO pregnancies(patientMID, conceptionYear, weeksPregnant, laborHours, weightGain, deliveryType, numberOfChildren)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        } else {
            long id = pregnancy.getPregnancyID();
            stmt = "UPDATE pregnancies SET patientMID=?, "
                    + "conceptionYear=?, "
                    + "weeksPregnant=?, "
                    + "laborHours=?, "
                    + "weightGain=?, "
                    + "deliveryType=?, "
                    + "numberOfChildren=? "
                    + "WHERE pregnancyID=" + id + ";";
        }
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, pregnancy.getPatientMID());
        ps.setInt(2, pregnancy.getConceptionYear());

        ps.setInt(3, pregnancy.getWeeksPregnant());
        ps.setInt(4, pregnancy.getLaborHours());
        ps.setInt(5, pregnancy.getWeightGain());
        ps.setString(6, pregnancy.getDeliveryType());
        ps.setInt(7, pregnancy.getNumberOfChildren());

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
