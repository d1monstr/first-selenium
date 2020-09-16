import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class FirstTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "http://www.rgs.ru";
        driver.get(baseUrl);
    }

    @Test
    public void exampleScenario() {
        // развернуть "Меню"
        String menuButtonXPath = "//div[@class = 'navbar-collapse collapse']//a[contains(text(), 'Меню')]";
        WebElement menuButton = driver.findElement(By.xpath(menuButtonXPath));
        menuButton.click();

        // выбрать пункт подменю - "ДМС"
        String dmsButtonXPath = "//a[contains(text(), 'ДМС')]";
        WebElement dmsButton = driver.findElement(By.xpath(dmsButtonXPath));
        dmsButton.click();

        // проверка открытия страницы "Добровольное медицинское страхование"
        String pageTitleXPath = "//h1[contains(text(), 'добровольное медицинское страхование')]";
        WebElement pageTitle = driver.findElement(By.xpath(pageTitleXPath));
        waitUtilElementToBeVisible(pageTitle);
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "ДМС — добровольное медицинское страхование", pageTitle.getText());

        // нажать кнопку "Отправить заявку"
        String requestXPath = "//a[contains(text(), 'Отправить заявку')]";
        WebElement requestButton = driver.findElement(By.xpath(requestXPath));
        waitUtilElementToBeClickable(requestButton);
        requestButton.click();

        // проверка открытия страницы "Заявка на добровольное медицинское страхование"
        String pageTitle1XPath = "//b[contains(text(), 'Заявка на добровольное медицинское страхование')]";
        WebElement pageTitle1 = driver.findElement(By.xpath(pageTitle1XPath));
        waitUtilElementToBeVisible(pageTitle1);
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Заявка на добровольное медицинское страхование", pageTitle1.getText());

        // заполнить поля данными
        fillInputField(driver.findElement(By.xpath("//input[@name = 'LastName']")), "Иванов");
        fillInputField(driver.findElement(By.xpath("//input[@name = 'FirstName']")), "Иван");
        fillInputField(driver.findElement(By.xpath("//input[@name = 'MiddleName']")), "Иванович");

        Select dropdown = new Select(driver.findElement(By.xpath("//select[@name = 'Region']")));
        dropdown.selectByVisibleText("Москва");

        WebElement phoneField = driver.findElement(By.xpath("//input[contains(@data-bind, 'phone')]"));
        waitUtilElementToBeClickable(phoneField);
        phoneField.click();
        phoneField.sendKeys("9999999999");
        Assert.assertEquals("Поле было заполнено некорректно",
                "+7 (999) 999-99-99", phoneField.getAttribute("value"));


        fillInputField(driver.findElement(By.xpath("//input[@name = 'Email']")), "qwertyqwerty");
        fillInputField(driver.findElement(By.xpath("//textarea[@name = 'Comment']")), "Комментарий");

        // Согласие на обработку данных
        WebElement checkboxAgree = driver.findElement(By.xpath("//input[@class = 'checkbox']"));
        if (!checkboxAgree.isSelected()){
            checkboxAgree.click();
        }

        // кликнуть по кнопке "Отправить"
        WebElement sendButton = driver.findElement(By.xpath("//button[contains(text(), 'Отправить')]"));
        sendButton.click();

        // проверить сообщение об ошибке
        String errorAlertXPath = "//input[@name = 'Email']/..//span[@class = 'validation-error-text']";
        WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
        waitUtilElementToBeVisible(errorAlert);
        Assert.assertEquals("Проверка ошибки у Email на странице не была пройдено",
                "Введите адрес электронной почты", errorAlert.getText());
    }

    @After
    public void after(){
        driver.quit();
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputField(WebElement element, String value) {
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assert.assertEquals("Поле было заполнено некорректно",
                value, element.getAttribute("value"));

    }
}
