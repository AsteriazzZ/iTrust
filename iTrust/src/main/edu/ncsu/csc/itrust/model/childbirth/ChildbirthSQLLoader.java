package edu.ncsu.csc.itrust.model.childbirth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;

/**
 * creates sql statements to be executed
 */
public class ChildbirthSQLLoader implements SQLLoader<Childbirth> {
    /**
     * loads a list of childbirth records
     * @param rs The java.sql.ResultSet we are extracting.
     * @return
     * @throws SQLException
     */
    @Override
    public List<Childbirth> loadList(ResultSet rs) throws SQLException {
        ArrayList<Childbirth> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;

    }

    /**
     * loads a single childbirth record
     * @param rs The java.sql.ResultSet to be loaded.
     * @return
     * @throws SQLException
     */
    @Override
    public Childbirth loadSingle(ResultSet rs) throws SQLException {
        Childbirth retVisit = new Childbirth();
        retVisit.setBirthID(Long.parseLong(rs.getString("birthID")));
        retVisit.setVisitID(Long.parseLong(rs.getString("visitID")));
        retVisit.setDeliveryType(rs.getString("deliveryType"));
        retVisit.setSex(rs.getString("sex"));
        retVisit.setDate(rs.getTimestamp("birthDateTime").toLocalDateTime());
        retVisit.setName(rs.getString("birthName"));
        return retVisit;
    }

    /**
     * loads parameters into sql statement for insert and update
     * @param conn The connection to the database
     * @param ps The prepared statement to be loaded.
     * @param ov
     * @param newInstance True if a new instance of the object should be created, false if the prepared statement should update an existing instance of the object
     * @return
     * @throws SQLException
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Childbirth ov, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO childbirths(visitID, birthDateTime, deliveryType, sex, birthName)"
                    + "VALUES (?, ?, ?, ?, ?);";

        } else {
            long id = ov.getBirthID();
            stmt = "UPDATE childbirths SET visitID=?, "
                    + "birthDateTime=?, "
                    + "deliveryType=?, "
                    + "sex=?, "
                    + "birthName=? "
                    + "WHERE birthID=" + id + ";";
        }
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, ov.getVisitID());
        Timestamp ts = Timestamp.valueOf(ov.getDate());

        ps.setTimestamp(2, ts);

        ps.setString(3, ov.getDeliveryType());
        ps.setString(4, ov.getSex());
        ps.setString(5, ov.getName());

        return ps;
    }

    /**
     * loads parameters into delete sql statement
     * @param conn
     * @param ps
     * @param birthID
     * @return
     * @throws SQLException
     */
    public PreparedStatement loadDeleteParameters(Connection conn, PreparedStatement ps, Long birthID)
            throws SQLException {
        String stmt = "";
        stmt = "DELETE FROM childbirths WHERE birthID=" + birthID + ";";
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        return ps;
    }
}
