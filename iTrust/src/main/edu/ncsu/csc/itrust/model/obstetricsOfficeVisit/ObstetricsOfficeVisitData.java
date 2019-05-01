package edu.ncsu.csc.itrust.model.obstetricsOfficeVisit;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.user.patient.Patient;

public interface ObstetricsOfficeVisitData extends DataBean<ObstetricsOfficeVisit>{
    /**
     *
     * @param patientID
     * @return obstetrics office visits for given patient
     * @throws DBException
     */
    public List<ObstetricsOfficeVisit> getObstetricsOfficeVisitsForPatient(Long patientID) throws DBException;

    /**
     * Add obstetrics office visit to the database and return the generated VisitID.
     *
     * @param ov
     * 			Office visit to add to the database
     * @return VisitID generated from the database insertion, -1 if nothing was generated
     * @throws DBException if error occurred in inserting office visit
     */
    public long addReturnGeneratedId(ObstetricsOfficeVisit ov) throws DBException;

    /**
     *
     * @param patientMID
     * @return patient object for given id
     * @throws DBException
     */
    public Patient getPatient(Long patientMID) throws DBException;
}
