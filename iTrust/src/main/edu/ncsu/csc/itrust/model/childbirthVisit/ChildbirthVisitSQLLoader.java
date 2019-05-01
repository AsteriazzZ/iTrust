package edu.ncsu.csc.itrust.model.childbirthVisit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class ChildbirthVisitSQLLoader implements SQLLoader<ChildbirthVisit> {
    /**
     * loads list of childbirth visit records
     * @param rs The java.sql.ResultSet we are extracting.
     * @return
     * @throws SQLException
     */
    @Override
    public List<ChildbirthVisit> loadList(ResultSet rs) throws SQLException {
        ArrayList<ChildbirthVisit> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;

    }

    /**
     * loads a single childbirth visit record
     * @param rs The java.sql.ResultSet to be loaded.
     * @return
     * @throws SQLException
     */
    @Override
    public ChildbirthVisit loadSingle(ResultSet rs) throws SQLException {
        ChildbirthVisit retVisit = new ChildbirthVisit();
        retVisit.setVisitID(Long.parseLong(rs.getString("visitID")));
        retVisit.setPatientMID(Long.parseLong(rs.getString("patientMID")));
        retVisit.setLocationID(rs.getString("locationID"));
        retVisit.setVisitDate(rs.getTimestamp("visitDate").toLocalDateTime());
        retVisit.setPreScheduled(rs.getBoolean("preScheduled"));
        retVisit.setDeliveryType(rs.getString("deliveryType"));
        retVisit.setPitocin(rs.getLong("pitocin"));
        retVisit.setPethidine(rs.getLong("pethidine"));
        retVisit.setMagnesium(rs.getLong("magnesiumSulfate"));
        retVisit.setEpidural(rs.getLong("epiduralAnaesthesia"));
        retVisit.setRh(rs.getLong("rhImmuneGlobulin"));
        retVisit.setOxide(rs.getLong("nitrousOxide"));
        return retVisit;
    }

    /**
     * loads parameters into either insert or update sql statement for the childbirth visit db
     * @param conn The connection to the database
     * @param ps The prepared statement to be loaded.
     * @param ov
     * @param newInstance True if a new instance of the object should be created, false if the prepared statement should update an existing instance of the object
     * @return
     * @throws SQLException
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ChildbirthVisit ov, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO childbirthVisits(patientMID, visitDate, locationID, apptType, preScheduled, deliveryType, pitocin, nitrousOxide, pethidine, epiduralAnaesthesia, magnesiumSulfate, rhImmuneGlobulin)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        } else {
            long id = ov.getVisitID();
            stmt = "UPDATE childbirthVisits SET patientMID=?, "
                    + "visitDate=?, "
                    + "locationID=?, "
                    + "apptType=?, "
                    + "preScheduled=?, "
                    + "deliveryType=?, "
                    + "pitocin=?, "
                    + "nitrousOxide=?, "
                    + "pethidine=?, "
                    + "epiduralAnaesthesia=?, "
                    + "magnesiumSulfate=?, "
                    + "rhImmuneGlobulin=? "
                    + "WHERE visitID=" + id + ";";
        }
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, ov.getPatientMID());
        Timestamp ts = Timestamp.valueOf(ov.getVisitDate());

        ps.setTimestamp(2, ts);

        ps.setString(3, ov.getLocationID());
        ps.setString(4, ov.getApptType());
        ps.setBoolean(5, ov.getPreScheduled());
        ps.setString(6, ov.getDeliveryType());
        ps.setLong(7, ov.getPitocin());
        ps.setLong(8, ov.getOxide());
        ps.setLong(9, ov.getPethidine());
        ps.setLong(10, ov.getEpidural());
        ps.setLong(11, ov.getMagnesium());
        ps.setLong(12, ov.getRh());

        return ps;
    }

    /**
     * loads delete sql statement for childbirth visit database
     * @param conn
     * @param ps
     * @param visitID
     * @return
     * @throws SQLException
     */
    public PreparedStatement loadDeleteParameters(Connection conn, PreparedStatement ps, Long visitID)
            throws SQLException {
        String stmt = "";
        stmt = "DELETE FROM childbirthVisits WHERE visitID=" + visitID + ";";
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        return ps;
    }
}