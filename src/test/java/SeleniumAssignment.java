import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SeleniumAssignment {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void Setup() {
        System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
        FirefoxOptions ops = new FirefoxOptions();
        ops.addArguments("--headed");
        //ops.addArguments("--headless");//for not opening browser
        driver = new FirefoxDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void GetTitle() {
        driver.get("https://demoqa.com");
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertTrue(title.contains("ToolsQA"));
    }

    @Test
    public void CheckIfElementExists() throws InterruptedException {
        driver.get("https://demoqa.com");
        //Boolean status=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='/images/Toolsqa.jpg']"))).isDisplayed();
        Boolean status = driver.findElement(By.xpath("//img[@src='/images/Toolsqa.jpg']")).isDisplayed();
        Assert.assertEquals(status, true);
    }

    @Test
    public void WriteOnTextBox() {
        driver.get("https://demoqa.com/elements");
        driver.findElement(By.xpath("//span[contains(text(),'Text Box')]")).click();
        driver.findElement(By.xpath("//input[@id='userName']")).click();
        driver.findElement(By.xpath("//input[@id='userName']")).sendKeys("Pollab Ahmed");
        driver.findElement(By.xpath("//button[@id='submit']")).click();
        String status = driver.findElement(By.cssSelector("[id=name]")).getText();
        Assert.assertTrue(status.contains("Pollab Ahmed"));
    }

    @Test
    public void ClickOnButtonsFromMultipleElements() {
        driver.get("https://demoqa.com/buttons");
        Actions action = new Actions(driver);
        List<WebElement> list = driver.findElements(By.cssSelector("button"));
        action.doubleClick(list.get(1)).perform();
        String text = driver.findElement(By.id("doubleClickMessage")).getText();
        Assert.assertTrue(text.contains("You have done a double click"));

        action.contextClick(list.get(2)).perform();
        String text2 = driver.findElement(By.id("rightClickMessage")).getText();
        Assert.assertTrue(text2.contains("You have done a right click"));

        list.get(3).click();
        String text3 = driver.findElement(By.id("dynamicClickMessage")).getText();
        Assert.assertTrue(text3.contains("You have done a dynamic click"));
    }

    @Test
    public void DropdownSelection() {
        driver.get("https://demoqa.com/select-menu");
        Select color = new Select(driver.findElement(By.id("oldSelectMenu")));
        color.selectByValue("1");
        Select cars = new Select(driver.findElement(By.id("cars")));
        if (cars.isMultiple()) {
            cars.selectByValue("opel");
            cars.selectByValue("saab");
        }
    }

    @Test
    public void DatePicker() {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("16/08/1995");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);
    }

    @Test
    public void HandleAlert() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
//        driver.findElement(By.id("alertButton")).click();
//        driver.switchTo().alert().accept();
//        driver.findElement(By.id("confirmButton")).click();
//        driver.switchTo().alert().accept();
        driver.findElement(By.id("promtButton")).click();
        driver.switchTo().alert().sendKeys("Ahmed");
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
        String text = driver.findElement(By.id("promptResult")).getText();
        Assert.assertTrue(text.contains("Ahmed"));
    }

    @Test
    public void HandleTabs() throws InterruptedException {
        driver.get("https://demoqa.com/links");
        driver.findElement(By.id("simpleLink")).click();
        Thread.sleep(5000);
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        System.out.println("New tab title: " + driver.getTitle());
//        for(String childTab:driver.getWindowHandles()){
//            driver.switchTo().window(childTab);
//        }
//        System.out.printf(driver.getTitle());
        Boolean status = driver.findElement(By.xpath("//img[@src='/images/Toolsqa.jpg']")).isDisplayed();
        Assert.assertEquals(true, status);
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }

    @Test
    public void HandleMultipleWindow() {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("windowButton")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("windowButton")));
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandle = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandle.iterator();

        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(childWindow)) {
                driver.switchTo().window(childWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }
        }
    }

    @Test
    public void ModalDialog() {
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        driver.findElement(By.id("closeSmallModal")).click();
    }

    @Test
    public void WebTables() {
        driver.get("https://demoqa.com/webtables");
        driver.findElement(By.id("edit-record-1")).click();
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("Pollab");
        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).sendKeys("Ahmed");
        driver.findElement(By.id("submit")).click();
    }

    @Test
    public void ScrapData() {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i = 0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num[" + i + "] " + cell.getText());

            }
        }
    }

    @Test
    public void UploadImage() {
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("D:\\Torrent\\Wallpaperswide.com\\4K\\3d_sun_system_2-wallpaper-3840x2160.jpg");
        String text = driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(text.contains("3d_sun_system_2-wallpaper-3840x2160.jpg"));
    }

    @Test
    public void HandleIFrame() {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(text.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @Test
    public void MouseHover() throws InterruptedException {
        driver.get("https://green.edu.bd/");
        WebElement mainMenu = driver.findElement(By.xpath("//a[@class='dropdown-toggle'][contains(text(),'About Us')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(mainMenu).perform();
        Thread.sleep(3000);
        WebElement subMenu = driver.findElement(By.xpath("//li[@id='menu-item-325']//a[contains(text(),'History')]"));
        actions.moveToElement(subMenu);
        actions.click().build().perform();
    }

    @Test
    public void KeyboardEvent() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement webElement = driver.findElement(By.name("q"));
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement);
        actions.keyDown(Keys.SHIFT)
                .sendKeys("Selenium Webdriver")
                .keyUp(Keys.SHIFT)
                .sendKeys(Keys.ENTER)
                .perform();
        Thread.sleep(5000);
    }

    @Test
    public void TakingScreenshot() throws IOException {
        driver.get("https://demoqa.com/");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File destFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, destFile);
    }

    @Test
    public void ReadExcelFile() throws IOException {
        String filePath = ".\\src\\test\\resources";
        Utils.readFromExcel(filePath, "DemoFile.xls", "Sheet1");
    }

    @After
    public void FinishTest() {
        driver.close();
    }
}