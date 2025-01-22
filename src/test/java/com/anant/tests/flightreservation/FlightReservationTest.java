package com.anant.tests.flightreservation;

import com.anant.pages.flightreservation.*;
import com.anant.tests.AbstractTest;
import com.anant.tests.flightreservation.model.FlightReservationTestData;
import com.anant.tests.vendorportal.model.VendorPortalTestData;
import com.anant.util.JsonUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FlightReservationTest extends AbstractTest {

    private RegistrationPage registrationPage;
    private RegistrationConfirmationPage registrationConfirmationPage;
    private FlightsSearchPage flightsSearchPage;
    private FlightsSelectionPage flightsSelectionPage;
    private FlightConfirmationPage flightConfirmationPage;
    private FlightReservationTestData testData;


    @BeforeTest
    @Parameters("testDataPath")
    public void setPageObjects(String testDataPath){
        this.testData = JsonUtil.getTestData(testDataPath, FlightReservationTestData.class);
        this.registrationPage = new RegistrationPage(driver);
        registrationConfirmationPage = new RegistrationConfirmationPage(driver);
        flightsSearchPage = new FlightsSearchPage(driver);
        flightsSelectionPage = new FlightsSelectionPage(driver);
        flightConfirmationPage = new FlightConfirmationPage(driver);

    }

    @Test
    public void userRegistrationTest(){
        registrationPage.goTo("https://d1uh9e7cu07ukd.cloudfront.net/selenium-docker/reservation-app/index.html");
        driver.manage().window().maximize();
        Assert.assertTrue(registrationPage.isAt());

        registrationPage.enterUserDetails(testData.firstName(), testData.lastName());
        registrationPage.enterUserCredentials(testData.email(), testData.password());
        registrationPage.enterAddress(testData.street(), testData.city(), testData.zip());
        registrationPage.register();
    }

    @Test(dependsOnMethods = "userRegistrationTest")
    public void registrationConfirmationTest(){
        Assert.assertTrue(registrationConfirmationPage.isAt());
        registrationConfirmationPage.goToFlightsSearch();
    }

    @Test(dependsOnMethods = "registrationConfirmationTest")
    public void flightsSearchTest(){
        Assert.assertTrue(flightsSearchPage.isAt());
        flightsSearchPage.selectPassengers(testData.passengersCount());
        flightsSearchPage.searchFlights();
    }

    @Test(dependsOnMethods = "flightsSearchTest")
    public void flightsSelectionTest(){
        Assert.assertTrue(flightsSelectionPage.isAt());
        flightsSelectionPage.selectFlights();
        flightsSelectionPage.confirmFlights();
    }

    @Test(dependsOnMethods = "flightsSelectionTest")
    public void flightReservationConfirmationTest(){
        Assert.assertTrue(flightConfirmationPage.isAt());
        Assert.assertEquals(flightConfirmationPage.getPrice(), testData.expectedPrice());
    }

}
