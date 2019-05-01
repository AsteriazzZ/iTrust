
package edu.ncsu.csc.itrust.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;



public class AddObstetricsOfficeVisitTest extends iTrustSeleniumTest {
    protected WebDriver driver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    public void testHCPEligibility() throws Exception {
        // Login as ordinary HCP Kelly Doctor
        driver = login("9000000000", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

        // Click the Obstetrics View/Initialize Records form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsPatientEligibilityCheck.jsp");
        assertTrue(driver.findElement(By.xpath("//body")).getText()
                .contains("You are not an OB/GYN HCP and do not have the authority to approve them for obstetrics care"));
    }

    public void testPatientEligibility() throws Exception {
        // Login as OB/GYN HCP Kathryn Evans
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the Obstetrics View/Initialize Records form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsPatientEligibilityCheck.jsp");
        assertTrue(driver.findElement(By.xpath("//body")).getText()
                .contains("The patient is not eligible for obstetric care."));
    }

    public void testBlankDate() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsOfficeVisitForm.xhtml");
        setValue("basic_ov_form:ovdate", "");
        setValue("basic_ov_form:ovWeeksPreg", "5");
        setValue("basic_ov_form:ovWeight", "100");
        setValue("basic_ov_form:ovBloodPressure", "110");
        setValue("basic_ov_form:ovFHR", "120");
        setValue("basic_ov_form:ovMultiPreg", "1");
        Select select;
        select = new Select(driver.findElement(By.id("basic_ov_form:ovLyingPlacenta")));
        select.selectByIndex(0);

        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();

        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("Date: Validation Error: Value is required."));
    }



    public void testBlankWeek() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsOfficeVisitForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovWeeksPreg", "");
        setValue("basic_ov_form:ovWeight", "100");
        setValue("basic_ov_form:ovBloodPressure", "110");
        setValue("basic_ov_form:ovFHR", "120");
        setValue("basic_ov_form:ovMultiPreg", "1");
        Select select;
        select = new Select(driver.findElement(By.id("basic_ov_form:ovLyingPlacenta")));
        select.selectByIndex(0);

        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();

        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("numOfWeeksPregnant: Validation Error: Value is required."));
    }




    public void testBlankWeight() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        //assertEquals("Obstetric Office Visit", driver.getTitle());

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsOfficeVisitForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovWeeksPreg", "5");
        setValue("basic_ov_form:ovWeight", "");
        setValue("basic_ov_form:ovBloodPressure", "110");
        setValue("basic_ov_form:ovFHR", "120");
        setValue("basic_ov_form:ovMultiPreg", "1");
        Select select;
        select = new Select(driver.findElement(By.id("basic_ov_form:ovLyingPlacenta")));
        select.selectByIndex(0);

        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();

        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("weight: Validation Error: Value is required."));
    }

    public void testBlankBloodPressure() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        //assertEquals("Obstetric Office Visit", driver.getTitle());

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsOfficeVisitForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovWeeksPreg", "5");
        setValue("basic_ov_form:ovWeight", "100");
        setValue("basic_ov_form:ovBloodPressure", "");
        setValue("basic_ov_form:ovFHR", "120");
        setValue("basic_ov_form:ovMultiPreg", "1");
        Select select;
        select = new Select(driver.findElement(By.id("basic_ov_form:ovLyingPlacenta")));
        select.selectByIndex(0);

        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();

        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("bloodPressure: Validation Error: Value is required."));
    }


    public void testBlankFHR() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        //assertEquals("Obstetric Office Visit", driver.getTitle());

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsOfficeVisitForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovWeeksPreg", "5");
        setValue("basic_ov_form:ovWeight", "100");
        setValue("basic_ov_form:ovBloodPressure", "110");
        setValue("basic_ov_form:ovFHR", "");
        setValue("basic_ov_form:ovMultiPreg", "1");
        Select select;
        select = new Select(driver.findElement(By.id("basic_ov_form:ovLyingPlacenta")));
        select.selectByIndex(0);

        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();

        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("fetalHeartRate: Validation Error: Value is required."));
    }


    public void testBlankMultiPreg() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        //assertEquals("Obstetric Office Visit", driver.getTitle());

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsOfficeVisitForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovWeeksPreg", "5");
        setValue("basic_ov_form:ovWeight", "100");
        setValue("basic_ov_form:ovBloodPressure", "110");
        setValue("basic_ov_form:ovFHR", "120");
        setValue("basic_ov_form:ovMultiPreg", "");
        Select select;
        select = new Select(driver.findElement(By.id("basic_ov_form:ovLyingPlacenta")));
        select.selectByIndex(0);

        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();

        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("multiplePregnancy: Validation Error: Value is required."));
    }



    public void testUltrasound() throws Exception {
        // Login
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the obstetric office visit form link
        WebElement element = driver.findElement(By.linkText("View/Add Office Visits"));
        element.click();
        // use the old search to go to the patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        //assertEquals("Obstetric Office Visit", driver.getTitle());

        // Enter in information but blank date
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/ultrasounds/ultrasoundForm.xhtml");
        setValue("basic_ov_form:CRL", "4");
        setValue("basic_ov_form:BPD", "5");
        setValue("basic_ov_form:HC", "100");
        setValue("basic_ov_form:FL", "110");
        setValue("basic_ov_form:OFD", "120");
        setValue("basic_ov_form:AC", "1");
        setValue("basic_ov_form:HL", "2");
        setValue("basic_ov_form:EFW", "3");


        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();
        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/ultrasounds/viewUltrasounds.xhtml");
        assertEquals("Document Ultrasound", driver.getTitle());
    }

    private void setValue(String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(value);
    }
}
