package edu.ncsu.csc.itrust.model.ultrasounds;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface UltrasoundData extends DataBean<Ultrasound>{
    /**
     *
     * @param patientID
     * @return ultrasounds for given patient
     * @throws DBException
     */
    public List<Ultrasound> getUltrasoundsForPatient(Long patientID) throws DBException;

    /**
     *
     * @param patientID
     * @param obstetricOfficeVisitID
     * @return ultrasounds for the given obstetrics office visit
     * @throws DBException
     */
    public List<Ultrasound> getUltrasoundsForPatient(Long patientID, Long obstetricOfficeVisitID) throws DBException;

    /**
     *
     * @param ultrasoundID
     * @return encoded string of image for an ultrasound
     * @throws Exception
     */
    public String getUltrasoundImage(Long ultrasoundID) throws Exception;

    /**
     * Add ultrasound to db
     * @param ultrasound
     * @return return generated id of ultrasound
     * @throws DBException
     */
    public long addReturnGeneratedId(Ultrasound ultrasound) throws DBException;

    /**
     * Add encoded image string to ultrasound image table for the given ultrasound id
     * @param ultrasoundID
     * @param encodedImage
     * @throws SQLException
     */
    public void addUltrasoundImage(Long ultrasoundID, String encodedImage) throws SQLException;
}
