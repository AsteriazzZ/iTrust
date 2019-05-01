package edu.ncsu.csc.itrust.model.childbirthVisit;

//import java.time.LocalDate;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface ChildbirthVisitData extends DataBean<ChildbirthVisit>{
    public List<ChildbirthVisit> getChildbirthVisitsForPatient(Long patientID) throws DBException;



    /**
     * Add childbirth visit to the database and return the generated VisitID.
     *
     * @param cv
     * 			childbirth visit to add to the database
     * @return VisitID generated from the database insertion, -1 if nothing was generated
     * @throws DBException if error occurred in inserting office visit
     */
    public long addReturnGeneratedId(ChildbirthVisit cv) throws DBException;
    public boolean delete(Long id) throws DBException;
}