package edu.ncsu.csc.itrust.model.user.patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.old.enums.Role;

public class PatientSQLConvLoader implements SQLLoader<Patient> {


	
	
	@Override
	public List<Patient> loadList(ResultSet rs) throws SQLException {
		List<Patient> list  = new ArrayList<Patient>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	@Override
	public Patient loadSingle(ResultSet rs) throws SQLException {
		Patient ret = new Patient();
		long mid = rs.getLong("MID");
		String roleName = rs.getString("Role");
		Role userRole = Role.parse(roleName);
		String fn = rs.getString("firstName");
		String ln = rs.getString("lastName");
		ret.setFirstName(fn);
		ret.setLastName(ln);
		ret.setRole(userRole);
		try {
			ret.setMID(mid);
		} catch (ITrustException e) {
			throw new SQLException("Incorrect value for MID stored in MySQL database");
		}
		return ret;
	}


	public List<Patient> loadList2(ResultSet rs) throws SQLException {
		List<Patient> list  = new ArrayList<Patient>();
		while (rs.next())
			list.add(loadSingle2(rs));
		return list;
	}


	public Patient loadSingle2(ResultSet rs) throws SQLException {
		Patient ret = new Patient();
		long mid = rs.getLong("MID");
		String fn = rs.getString("firstName");
		String ln = rs.getString("lastName");
		ret.setFirstName(fn);
		ret.setLastName(ln);
		try {
			ret.setMID(mid);
		} catch (ITrustException e) {
			throw new SQLException("Incorrect value for MID stored in MySQL database");
		}
		return ret;
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Patient insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO patients(firstName, lastName)"
					+ "VALUES (?, ?);";

		} else {
			long id = insertObject.getMID();
//			stmt = "UPDATE childbirths SET visitID=?, "
//					+ "birthDateTime=?, "
//					+ "deliveryType=?, "
//					+ "sex=?, "
//					+ "birthName=? "
//					+ "WHERE birthID=" + id + ";";
		}
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);

		ps.setString(1, insertObject.getFirstName());
		ps.setString(2, insertObject.getLastName());

		return ps;
	}

}
