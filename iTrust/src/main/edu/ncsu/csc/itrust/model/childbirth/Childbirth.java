package edu.ncsu.csc.itrust.model.childbirth;

import java.time.LocalDateTime;
import java.time.Duration;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="childbirth")
/**
 * Class to store data of childbirth record
 */
public class Childbirth {
    private Long birthID;
    private Long visitID;
    private LocalDateTime date;
    private String deliveryType;
    private String sex;
    private String name;

    public Childbirth() {}

    /**
     *
     * @return birthID
     */
    public Long getBirthID() {
        return birthID;
    }

    /**
     * sets birthID
     * @param birthID_
     */
    public void setBirthID(Long birthID_) {
        this.birthID = birthID_;
    }
    /**
     *
     * @return visitID
     */
    public Long getVisitID() {
        return visitID;
    }

    /**
     * sets visitID
     * @param visitID_
     */
    public void setVisitID(Long visitID_) {
        this.visitID = visitID_;
    }
    /**
     *
     * @return Date
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * sets Date
     * @param date_
     */
    public void setDate(LocalDateTime date_) {
        this.date = date_;
    }
    /**
     *
     * @return DeliveryType
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * sets deliveryType
     * @param delivery
     */
    public void setDeliveryType(String delivery) {
        this.deliveryType = delivery;
    }
    /**
     *
     * @return Sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * sets Sex
     * @param sex_
     */
    public void setSex(String sex_) {
        this.sex = sex_;
    }
    /**
     *
     * @return Name
     */
    public String getName() {return name; }

    /**
     * sets Name
     * @param name_
     */
    public void setName(String name_) { this.name = name_; }
}
