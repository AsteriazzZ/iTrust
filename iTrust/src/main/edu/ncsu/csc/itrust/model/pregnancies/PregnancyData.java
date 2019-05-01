package edu.ncsu.csc.itrust.model.pregnancies;

//import java.time.LocalDate;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface PregnancyData extends DataBean<Pregnancy>{
    public List<Pregnancy> getPregnanciesForPatient(Long patientID) throws DBException;


    public long addReturnGeneratedId(Pregnancy pregnancy) throws DBException;
}
