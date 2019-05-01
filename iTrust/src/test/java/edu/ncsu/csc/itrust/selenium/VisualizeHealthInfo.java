package edu.ncsu.csc.itrust.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizeHealthInfo extends iTrustSeleniumTest {
    private HtmlUnitDriver driver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Make sure all the numeric fields are an <a> element with the proper name
     * @throws Exception
     */
    @Test
    public void testClickableFields() throws Exception {
        driver = (HtmlUnitDriver) login("102", "pw");
        assertEquals("iTrust - Patient Home", driver.getTitle());
        driver.findElement(By.id("toggleMenu")).click();
        driver.findElement(By.xpath("//div[@id='iTrustMenu']/div/div[1]/div/h2")).click();
        driver.findElement(By.linkText("View My Records")).click();
        assertEquals("iTrust - View My Records", driver.getTitle());
        List<String> columns = driver.findElements(By.xpath("//div[@id='iTrustContent']/table/thead/tr/th/a")).stream().map(el -> el.getText()).collect(Collectors.toList());
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Length");
        expected.add("Weight");
        expected.add("Head Circumference");
        assertTrue(expected.containsAll(columns));
    }

    /**
     * Verify a clicked column field leads to the visualization page with the graph
     * @throws Exception
     */
    @Test
    public void testVisualization() throws Exception {
        driver = (HtmlUnitDriver) login("102", "pw");
        assertEquals("iTrust - Patient Home", driver.getTitle());
        driver.findElement(By.id("toggleMenu")).click();
        driver.findElement(By.xpath("//div[@id='iTrustMenu']/div/div[1]/div/h2")).click();
        driver.findElement(By.linkText("View My Records")).click();
        assertEquals("iTrust - View My Records", driver.getTitle());
        WebElement lengthCol = driver.findElements(By.xpath("//div[@id='iTrustContent']/table/thead/tr/th/a")).get(0);
        assertEquals(lengthCol.getText(), "Length");
        lengthCol.click();
        assertEquals(driver.getTitle(), "iTrust - Visualize My Records");
    }


}
