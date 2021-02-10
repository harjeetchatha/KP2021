package com.kp.apps.web.library;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

/**
 * Takes care of common functions
 *
 * @author Harjeet Singh
 */
public class Functions {
  private WebDriver              driver;

  private long                   WAIT_DURATION_IN_SECONDS = 60;

  private long                   NINTY_SECOND_IN_MILLIS   = 90;

  private Logger                 logger                   = Logger.getLogger(Functions.class);

  static UIMapParser             uimapParser              = new UIMapParser();

  private static StringEncrypter encrypter                = StringEncrypter.createNewEncrypter();

  public long                    launchTime               = 0;
  public static String           startTime                = "0";
  public static String           endTime                  = "0";


  public WebDriverWait           wait;

  private InputStream getResourceFromClasspath(String resourceName) throws IOException {
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
    if (inputStream == null) {
      throw new FileNotFoundException("resource '" + resourceName + "' not found in the classpath");
    }
    return inputStream;
  }

  /**
   *
   * @author SreeMoore
   *
   */
//Function to get time stamp in YYYY-MM-DD format
  public String dateYearStamp() {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }

  /**
   *
   * @author SreeMoore
   *
   */
  // Function to get Last day in Next Month
  public String getNextDate() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, 1);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    Date nextDate = cal.getTime();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String reportDate = df.format(nextDate);
    return reportDate;
  }


  // Function to get time stamp in MMMdd format
  public String dateStamp() {

    DateFormat df = new SimpleDateFormat("MMMdd");
    java.util.Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }

  // Function to get year in yyyy format
  public String yearStamp() {

    DateFormat df = new SimpleDateFormat("yyyy");
    java.util.Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }

  // Function to get random string number
  public String RandomStringNumber() throws SQLException {
    // note a single Random object is reused here
    Random randomGenerator = new Random();
    int randomInt = 0;
    for (int idx = 1; idx <= 10; ++idx) {
      randomInt = randomGenerator.nextInt(10000);
    }
    String randomStr = String.valueOf(randomInt);

    System.out.println(randomStr);
    return randomStr;
  }

  // Function to change time from days,hrs into seconds
  public String ChangeTimeStamp(int days, int hrs) throws Exception {
    long time = (System.currentTimeMillis() / 1000);
    time = time - (24 * 60 * 60 * days + hrs * 60 * 60);
    String changedTime = (String.valueOf(time));
    return changedTime;
  }

  // Function to generate random string
  public String randomstring(int length) {
    UUID uuid = UUID.randomUUID();
    String myRandom = uuid.toString();
    return myRandom.substring(length);
  }

  // Function to get the year in format yyyy
  public String timeinyear() {

    DateFormat df = new SimpleDateFormat("yyyy");
    java.util.Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }

  // Function to get the time stamp in MMddyyhhmmss format
  public String timeStamp() {

    DateFormat df = new SimpleDateFormat("MMddyyhhmmss");
    java.util.Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }

  // Connects to config property and read the confidential details
  public static String getConfDetails(String confValueName) throws Exception {

    BufferedReader in = new BufferedReader(new FileReader("Conf.txt"));
    String line;
    int lineCount = 1;
    String confValue = "";
    while ((line = in.readLine()) != null) {
      if (line.indexOf(confValueName) != -1) {
        confValue = line.split("=")[1];

      }
      lineCount++;
    }
    in.close();

    return confValue;
  }

  public static String getXpath(String pageName, String objectname) throws Exception {
    ExcelReader e = new ExcelReader();
    return e.getdata("Xpath", "xpaths",
        "objectName='" + objectname + "' and PageName='" + pageName + "'");

  }


  public void CompareXML(String SourceFile, String DownloadedFile) throws Exception {

    try {

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setCoalescing(true);
      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setIgnoringComments(true);
      DocumentBuilder db = dbf.newDocumentBuilder();

      org.w3c.dom.Document doc1 = db.parse(new File(SourceFile));
      doc1.normalizeDocument();

      org.w3c.dom.Document doc2 = db.parse(new File(DownloadedFile));

      doc2.normalizeDocument();
      Assert.assertTrue(doc1.isEqualNode(doc2));
      logger.info("XML comparison Passed");

    } catch (Exception e) {
      e.printStackTrace();
      logger.info("XML comparison failed");
    }

  }


  /**
   * Function to to Wait for Presence Of Element Located
   *
   * @throws Exception
   */
  public static void WaitforPresenceOfElementLocated(WebDriver driver, By by) throws Exception {

    WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(ExpectedConditions.presenceOfElementLocated(by));
  }

  /**
   * Function to to FindHeaderColumn in table grid
   *
   * @throws Exception
   */
  public int FindHeaderColumn(WebDriver driver, String Col_Header) throws Exception {
    int FoundInColumn = 0;
    logger.info("Searching table for string: " + Col_Header);

    // get the header and size
    int Cols = driver.findElements(By.xpath("//div[1]/table/tbody/tr[2]/td")).size();

    // System.out.println(Cols);
    // verify header for "search" in header columns
    for (int x = 2; x < Cols; x++) {
      String ColumnTxt =
          driver.findElement(By.xpath("//div[1]/table/tbody/tr[2]/td[" + x + "]")).getText();
      if (ColumnTxt.contains(Col_Header)) {
        FoundInColumn = x;
        logger.info("Grid header with '" + Col_Header + "'found in column" + FoundInColumn);
        break;
      }
    }
    return FoundInColumn;
  }

  // GEt Portal displayed time and return in epoc
  public static long GetPortalTime(WebDriver driver) throws Exception {

    WebElement sPortalDt = driver.findElement(By.xpath("//div/div[2]/div[2]"));
    String sPortalDate = sPortalDt.getText();
    System.out.println(sPortalDate);
    String[] sPortalDate1 = sPortalDate.split("day, ");

    sPortalDate = sPortalDate1[1].substring(0, sPortalDate1[1].length() - 4);
    // System.out.println("\n*****sPortalDate: "+sPortalDate+ " *****\n");
    SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMM dd, yyyy hh:mm aa");
    Date parsedDate = sdf.parse(sPortalDate);
    long launchTime = parsedDate.getTime();
    System.out.println("\n*****launchTime: " + launchTime + " *****\n");
    return launchTime;
  }

  public void callingvbs(String fileName) throws IOException {
    try {
      logger.info("TestData/UploadBatchData/" + fileName + "_xml.vbs file");

      final URL fileURL = Functions.class.getClassLoader().getResource("TestData/UploadBatchData");
      final String folderPath = fileURL.getPath().substring(1);

      logger.info("VBS file path = " + folderPath + "/" + fileName + "_xml.vbs");
      String cmd = "wscript " + folderPath + "/" + fileName + "_xml.vbs  \"" + folderPath + "\" ";
      Process p = Runtime.getRuntime().exec(cmd);
      // int exitValue =p.exitValue();
      // if (exitValue==1){
      // cmd= "wscript "+"src/test/resources/TestData/UploadBatchDatan/"+fileName+"_xml.vbs";
      // Runtime.getRuntime().exec(cmd);
      // }
    } catch (IOException e) {
      System.exit(0);
    }

  }

  public void CreateBatchfile_XML(String path) throws IOException {
    try {
      logger.info(path);
      String cmd = "wscript " + path;
      // String cmd= "wscript "+"TestData/UploadBatchData/Inline-Minutes_xml.vbs";
      Runtime.getRuntime().exec(cmd);
    } catch (IOException e) {
      System.exit(0);
    }

  }

  public static void Handle_onPresent(WebDriver driver, String xpath) throws Exception {
    try {
      WebElement elemnt = driver.findElement(By.xpath(xpath));
      elemnt.click();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getTestData(String TestID, String valueName) {
    final Sheet sheet;
    String value = "";
    try {

      // sheet = Workbook.getWorkbook(getResourceFromClasspath("TestData.xls")).getSheet("Data");
      // sheet = Workbook.getWorkbook(new
      // File("./src/test/resources/TestData.xls")).getSheet("Data");

      sheet = Workbook.getWorkbook(new File(GlobalVariables.sTestDataFile)).getSheet("Data");

      Cell t1 = sheet.findCell(TestID);
      Cell c1 = sheet.findCell(valueName);

      if (t1 == null)
        return "";
      else {
        int fndRow = t1.getRow();
        value = sheet.getCell(c1.getColumn(), fndRow).getContents();
      }

    } catch (BiffException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return value;
  }
  /*
  * Created by Harjeet
   */


  // send keys and get visibilityOf of Element
  public void type(WebDriver driver, By locator, String value, String steplog) {
    WebElement element =
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(locator));
    element.clear();
    element.sendKeys(value);
    logger.info(steplog);
  }

  // click function that will wait for visibilityOf element
  public void clickOnElement(WebDriver driver, WebElement element, int timeout) {
    new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
    element.click();
  }

  /*
   * idea to click/ and enabled.
   */
  public boolean verifyEditable(WebDriver driver, By locater) {
    boolean elementIsClickable = false;
    try {
      elementIsClickable = driver.findElement(locater).isEnabled();
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("verifyEditable");
    }
    return elementIsClickable;
  }
  // click and wait

  public void click(WebDriver driver, By by) throws InterruptedException {
    WebElement ele =
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
    highlighElement(driver, by);
    try {
      ele.click();
    } catch (ElementClickInterceptedException e) {
      int i = 0;
      while (i < 5) {
        Thread.sleep(i*1000);
        try {
          ele.click();
          logger.info("Cliked for ElementClickInterceptedException");
          break;
        } catch (Exception e1) {

        }
      }
    }
  }

  public void waitElementToLoad(WebDriver driver, By locator, int time)
      throws InterruptedException {
    int visible = 0;
    while (driver.findElements(locator).size() == 0 && visible < time) {
      logger.info("Waiting for element " + locator + " to visible");
      visible++;
    }
    logger.info("Element" + locator + "is visible");
    int enable = 0;
    while (enable < time) {
      logger.info("Waiting for element " + locator + " to enable");
     // Thread.sleep(enable*50);
        Thread.sleep(2000);
      try {
        if (driver.findElement(locator).isEnabled()) {
          logger.info("Element" + locator + "is enable");
          break;
        }

      //  enable++;
      } catch (Exception e) {
        logger.info("Checking for Element" + locator + "is enable for " + enable + " Time(s)");
      }
      enable++;
     
    }

  }

  // click and wait and log
  public void click(WebDriver driver, By by, String steplog) {
    WebElement ele =
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
    highlighElement(driver, by);
    ele.click();
    logger.info(steplog);
  }

  /**
   * Wait for an element to be displayed in the DOM
   *
   * @param elementLocator - CSS of the element
   * @return - WebElement
   */

  public WebElement waitForElement(WebDriver driver, By elementLocator) {
    WebElement webElement = null;
    int timeout = 20; // in seconds
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    try {
      System.err.println(elementLocator);
      webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
    } catch (WebDriverException e) {
      // do nothing, don't want to log this
    }

    if (webElement == null) {
      assertFalse(true, "WebElement not found within " + timeout + " seconds. Failing test!"
          + " of element: " + elementLocator + "\nCurrent page: " + driver.getCurrentUrl());
    }
    return webElement;
  }


  public WebElement waitForElementPrescence(WebDriver driver, By elementLocator) {
    WebElement webElement = null;
    int timeout = 10; // in seconds
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    try {
      System.out.println(elementLocator);
      webElement = wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));

    } catch (StaleElementReferenceException e) {
      // do nothing, don't want to log this
    }
    // if element wasn't found -> fail test
    if (webElement == null) {
      assertFalse(true, "WebElement not found within " + timeout + " seconds. Failing test!"
          + " of element: " + elementLocator + "\nCurrent page: " + driver.getCurrentUrl());
    }
    return webElement;
  }

  public boolean selectDropDown(WebDriver driver, By locator, String val)
      throws InterruptedException, ClassNotFoundException, IOException {
    boolean result = false;
    int attempts = 0;
    Select select;
    while (attempts < 5) {
      try {
        select = new Select(driver.findElement(locator));
        select.selectByVisibleText(val);

        result = true;
        break;
      } catch (StaleElementReferenceException e) {
      }
      attempts++;
      assertFalse(true, "WebElement not found within " + attempts + " Failing test!"
          + " of element: " + locator + "\nCurrent page: " + driver.getCurrentUrl());
    }
    return result;
  }


  /**
   * Method to wait for page to update following an action that changes the pages state.
   * This is needed to avoid any race conditions between page updates and Selenium, which can
   * sometimes occur.
   */

  public void refreshPage(WebDriver driver) {

    String currentURL = getCurrentURL(driver);
    driver.navigate().to(currentURL);
  }

  public String getCurrentURL(WebDriver driver) {
    return driver.getCurrentUrl();
  }

//  public String getElementText(WebDriver driver, String elementLocator) {
//
//    WebElement webElement = waitForElement(driver, elementLocator);
//    return webElement.getText();
//  }

  public void waitForFrameAndSwitch(WebDriver driver, By elementLocator) {

    int timeout = 10; // in seconds
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    try {
      // System.out.println(elementLocator);
      driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
      
      while (driver.findElements(By.xpath("//iframe[@aria-label='Main content']")).size() == 0) {
     logger.info("waiting for page to load");
      }
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(elementLocator));
      
      logger.info("Switched To Frame :" + elementLocator.toString());
    } catch (NoSuchFrameException e) {
      // do nothing, don't want to log this
      e.printStackTrace();
      Assert.fail("Failed : waitForFrameAndSwitch" + e.toString());
    }
  }

  public WebElement highlighElement(WebDriver driver, By by) {
    WebElement element = driver.findElement(by);
    // draw a border around the found element
    try {
      if (driver instanceof JavascriptExecutor) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='2px solid yellow'",
            element);

        SeleniumWait.hold(GlobalVariables.ShortSleep);

        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border=''", element);

      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Failed : highlighElement" + e.toString());
    }
    return element;
  }

  public WebElement waitForElementToBeClickable(WebDriver driver, By elementLocator, int timeout) {
    WebElement webElement = null;
    // int timeout = 10; // in seconds
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    try {
      System.out.println(elementLocator);
      webElement = wait.until(ExpectedConditions.elementToBeClickable(elementLocator));

    } catch (StaleElementReferenceException e) {
      // do nothing, don't want to log this
    }

    // if element wasn't found -> fail test
    if (webElement == null) {
      assertFalse(true, "WebElement not found within " + timeout + " seconds. Failing test!"
          + " of element: " + elementLocator + "\nCurrent page: " + driver.getCurrentUrl());
    }
    return webElement;
  }

  public String getCurrentTimeStamp(String dateFormat) {
    return new SimpleDateFormat(dateFormat).format(new Date());
  }

//  public boolean isreadOnly(element) {
//    Boolean readOnly = false;
//    readOnly = ((element.getAttribute("disabled") != null) || (element.getAttribute("readonly") != null));
//    return readOnly;
//}
  public void clickAt(WebDriver driver, By locator) {
    WebElement ele =
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(locator));
    Actions builder = new Actions(driver);
    builder.moveToElement(ele).click().build().perform();
  }


  public String getVisibleText(WebDriver driver, By locator) {
    WebElement ele =
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(locator));
    logger.info("Text captured :" + ele.getText().trim());
    return ele.getText().trim();

  }

  public void clickAt(WebDriver driver, By locator, String steplog) {
    WebElement ele =
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(locator));
    Actions builder = new Actions(driver);
    builder.moveToElement(ele).click().build().perform();
    logger.info(steplog);
  }

  public void moveTo(WebDriver driver, By locator) {
    Actions builder = new Actions(driver);
    builder.moveToElement(driver.findElement(locator)).build().perform();
  }

//  public void waitForText(String text, By by, String timeoutMsg) throws Exception {
//    for (int second = 0;; second++) {
//           if (second >= 30)
//                  fail(timeoutMsg);
//           try {
//                  if (text.equals(driver.findElement(by).getText()))
//                        break;
//           } catch (Exception e) {
//           }
//
//    }

  public static String getTimeStamp() {
    Date d = Calendar.getInstance().getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_YYYY_HH_MM_SS");
    String CurrentDate = sdf.format(d);
    return CurrentDate;
  }


  public String switchToWindow(WebDriver driver, String getActiveWindowHandle) {

    Set<String> windowHandles = driver.getWindowHandles();
    for (String windowHandle : windowHandles) {
      if (getActiveWindowHandle != windowHandle) {

        return windowHandle;

      }

    }
    return getActiveWindowHandle;


  }



  public void waitForWebElementPresence(WebDriver driver, WebElement webElement) {
    boolean found = false;
    wait = new WebDriverWait(driver, 60);


    final long startTime = System.currentTimeMillis();
    while ((System.currentTimeMillis() - startTime) < NINTY_SECOND_IN_MILLIS) {
      try {
        wait.until(ExpectedConditions.visibilityOf(webElement));

        found = webElement.isDisplayed();
        if (found) {
          break;
        }
      } catch (final Exception e) {
        // logWarning(webElement.getText(), e);

      }
      final long endTime = System.currentTimeMillis();
      final long totalTime = endTime - startTime;
      if (found) {
        logger.info(String.format("Found element[%s]TimeTaken[%s ms]", webElement.getText(),
            String.valueOf(totalTime)));
      }
      else {
        logger.error(String.format("Cannot find element[%s]Spent[%s ms] to find",
            webElement.getText(), String.valueOf(totalTime)));
      }


    }
  }


  public void waitForWebElementInvisibility(WebDriver driver, WebElement webElement) {
    boolean found = false;
    wait = new WebDriverWait(driver, 120);
    try {
      wait.until(ExpectedConditions.invisibilityOf(webElement));
    } catch (final Exception e) {
      // logWarning(webElement.getText(), e);

    }
  }


  public String randomNumber() {
    int min = 10000;
    int max = 99999;
    int random_int = (int) (Math.random() * (max - min + 1) + min);

    return Integer.toString(random_int);
  }


  /**
   * Mapping the column header with which column it belongs
   * This will allow header columns moved around, still we can re-map correctly
   * Also, allows to construct Xpath locators dynamically
   */
  public WebElement getCellByTableAndHeader(WebDriver driver, String tableName, String headerName) {
    String data = null;
    WebElement ele = null;
    for (int i = 0; i < driver
        .findElements(By.xpath(
            "//table[contains(@id,'" + tableName + "')]//th[contains(@class,'list_header')]"))
        .size(); i++) {
      data = driver
          .findElements(By.xpath(
              "//table[contains(@id,'" + tableName + "')]//th[contains(@class,'list_header')]"))
          .get(i).getAttribute("glide_label").trim();
      if (data.equalsIgnoreCase(headerName)

          && data != null) {
        // System.err.println(data + " found" + i);
        ele = driver.findElement(By.xpath("(//table[contains(@id,'" + tableName
            + "')]//tr[contains(@id,'" + tableName + "')])[2]/td[" + (i + 3) + "]"));
      }

    }
    return ele;
  }

  public WebElement highlighElement(WebDriver driver, WebElement ele) {

    // draw a border around the found element
    try {
      if (driver instanceof JavascriptExecutor) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='2px solid yellow'",
            ele);

        SeleniumWait.hold(GlobalVariables.ShortSleep);

        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border=''", ele);

      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Failed : highlighElement" + e.toString());
    }
    return ele;
  }

  // Function to scroll the page vertically:
  public void scrollPage(WebElement element) {
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].scrollIntoView(true);", element);

  }

//Function to scroll the page vertically:
 public void scrollPage(By locator,WebDriver driver) {
   JavascriptExecutor executor = (JavascriptExecutor) driver;
   executor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(locator));

 }
  
  public String waitforTitlepresent(String titlevalue, int timeOut) {
    WebDriverWait wait = new WebDriverWait(driver, timeOut);
    wait.until(ExpectedConditions.titleContains(titlevalue));
    return driver.getTitle();
  }
 
  public void switchToWindow(WebDriver driver, int index) {

    ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
    driver.switchTo().window(tabs2.get(index));
    logger.info("Switched to window " + tabs2.get(index));
  }

//wait for Left Nav loader to disappear:
  public void waitForLeftNavToLoad() {
    try {
    WebDriverWait wait = new WebDriverWait(driver, 5000L);
    wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//div/Span[@class='icon-loading']")))); 
    }catch(Exception e) {
      logger.info("element not available"+e.getMessage());
    }

  }
  
  
  /**
   * @author SreeMoore
   * Function to get time stamp in yyyy-MM-dd hh:mm:ss format
   */
  public String dateYearWithTimeStamp() {
  //date format 2020-09-25 09:53:04 PM
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    java.util.Date today = Calendar.getInstance().getTime();
    String reportDate = df.format(today);
    return reportDate;
  }
  
  
  
  /**
   * @author SreeMoore
   * Function to get time stamp + N days in yyyy-MM-dd hh:mm:ss format
   */
  public String dateYearWithTimeStamp(int n) {
   Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, n);
    Date nextDate = cal.getTime();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    String reportDate = df.format(nextDate);
    return reportDate;
  }
  
  
  

}

