package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class AddObstetricsPatientTest extends iTrustSeleniumTest{
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
        WebElement element = driver.findElement(By.linkText("View/Initialize Records"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        assertTrue(driver.findElement(By.xpath("//body")).getText()
                .contains("You are not an OB/GYN HCP and do not have the authority to approve them for obstetrics care"));
    }

    public void testPatientEligibility() throws Exception {
        // Login as OB/GYN HCP Kathryn Evans
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the Obstetrics View/Initialize Records form link
        WebElement element = driver.findElement(By.linkText("View/Initialize Records"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        assertEquals("iTrust - Obstetrics Patient Eligibility Check", driver.getTitle());
        assertTrue(driver.findElement(By.xpath("//body")).getText()
                .contains("The patient is not eligible for obstetric care."));
    }

    public void testBlankDate() throws Exception {
        // Login as OB/GYN HCP Kathryn Evans
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the Obstetrics View/Initialize Records form link
        WebElement element = driver.findElement(By.linkText("View/Initialize Records"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        assertEquals("iTrust - Obstetrics Patient Eligibility Check", driver.getTitle());
        driver.findElement(By.xpath("//input[@type='button']")).click();

        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml");
        driver.findElement(By.xpath("//input[@type='button']")).click();

        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsInitializationForm.xhtml");
        setValue("basic_ov_form:ovdate", "");
        setValue("basic_ov_form:ovLMP", "11/14/2018 11:47 PM");
        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();
        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("Date: Validation Error: Value is required."));
    }

    public void testBlankLMP() throws Exception {
        // Login as OB/GYN HCP Kathryn Evans
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the Obstetrics View/Initialize Records form link
        WebElement element = driver.findElement(By.linkText("View/Initialize Records"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        assertEquals("iTrust - Obstetrics Patient Eligibility Check", driver.getTitle());
        driver.findElement(By.xpath("//input[@type='button']")).click();

        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml");
        driver.findElement(By.xpath("//input[@type='button']")).click();

        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsInitializationForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovLMP", "");
        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();
        assertTrue(driver.findElement(By.className("iTrustError")).getText().contains("LMP: Validation Error: Value is required."));
    }

    public void testNormal() throws Exception {
        // Login as OB/GYN HCP Kathryn Evans
        driver = login("9000000012", "pw");
        assertEquals("iTrust - HCP Home", driver.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000012L, 0L, "");

        // Click the Obstetrics View/Initialize Records form link
        WebElement element = driver.findElement(By.linkText("View/Initialize Records"));
        element.click();

        // use the search to go to a patients page
        element = driver.findElement(By.name("UID_PATIENTID"));
        element.sendKeys("2");
        element = driver.findElement(By.id("mainForm"));
        element.submit();
        assertEquals("iTrust - Obstetrics Patient Eligibility Check", driver.getTitle());
        driver.findElement(By.xpath("//input[@type='button']")).click();

        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/viewObstetricsVisit.xhtml");
        driver.findElement(By.xpath("//input[@type='button']")).click();

        driver.get("http://localhost:8080/iTrust/auth/hcp-uap/obstetricsInitializationForm.xhtml");
        setValue("basic_ov_form:ovdate", "11/14/2018 11:47 PM");
        setValue("basic_ov_form:ovLMP", "10/14/2018 11:47 PM");
        driver.findElement(By.id("basic_ov_form:submitVisitButton")).click();
        assertTrue(driver.findElement(By.xpath("//body")).getText()
                .contains("Office Visit Successfully Created"));
    }

    private void setValue(String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(value);
    }
}

