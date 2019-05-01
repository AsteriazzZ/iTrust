package edu.ncsu.csc.itrust.model.obstetricVisit;

import java.time.LocalDate;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface ObstetricVisitData extends DataBean<ObstetricVisit>{
    public List<ObstetricVisit> getObstetricVisitsForPatient(Long patientID) throws DBException;

    /**
     * Retrieves the patient's date of birth from database.
     *
     * @param patientID
     * 			MID of the patient
     * @return patient's date of birth
     */
    public LocalDate getPatientDOB(Long patientID);

    /**
     * Add obstetric visit to the database and return the generated ID.
     *
     * @param ov
     * 			obstetric visit to add to the database
     * @return ID generated from the database insertion, -1 if nothing was generated
     * @throws DBException if error occurred in inserting office visit
     */
    public long addReturnGeneratedId(ObstetricVisit ov) throws DBException;
}
