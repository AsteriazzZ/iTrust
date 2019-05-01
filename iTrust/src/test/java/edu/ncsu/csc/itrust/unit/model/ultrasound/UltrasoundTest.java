package edu.ncsu.csc.itrust.unit.model.ultrasound;

import edu.ncsu.csc.itrust.model.ultrasounds.Ultrasound;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class UltrasoundTest extends TestCase {
    
    @Test
    public void testNumberInputs(){
        Ultrasound p = new Ultrasound();

        p.setPatientMID((long) 999);
        p.setUltrasoundID((long) 9);
        p.setObstetricOfficeVisitID(1);
        p.setAc(2);
        p.setBpd(3);
        p.setCrl(4);
        p.setEfw(5);
        p.setFl(6);
        p.setHc(7);
        p.setHl(8);
        p.setOfd(9);

        Assert.assertEquals(true, p.getPatientMID() == (long) 999);
        Assert.assertEquals(true, p.getUltrasoundID() == (long) 9);
        Assert.assertEquals(true, p.getObstetricOfficeVisitID() == 1);
        Assert.assertEquals(true, p.getAc() == 2);
        Assert.assertEquals(true, p.getBpd() == 3);
        Assert.assertEquals(true, p.getCrl() == 4);
        Assert.assertEquals(true, p.getEfw() == 5);
        Assert.assertEquals(true, p.getFl() == 6);
        Assert.assertEquals(true, p.getHc() == 7);
        Assert.assertEquals(true, p.getHl() == 8);
        Assert.assertEquals(true, p.getOfd() == 9);

    }

}
