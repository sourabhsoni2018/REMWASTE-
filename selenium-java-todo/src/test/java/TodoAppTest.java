
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TodoAppTest {
    private static WebDriver driver;

    @BeforeClass
      public void setup() {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\srbro\\unified-todo-app\\selenium-java-todo\\server\\geckodriver.exe"); // Adjust path if needed
        driver = new FirefoxDriver();
        driver.get("http://localhost:3000");
    }

    void login(String username, String password) {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();
    }

    @Test(priority = 1)
    public void testValidLogin() {
        login("admin", "password");
        Assert.assertTrue(driver.getPageSource().contains("Welcome!"));
    }

    @Test(priority = 2)
    
    public void testInvalidLogin() {
        driver.get("http://localhost:3000");
        login("wrong", "wrong");
        Assert.assertTrue(driver.getPageSource().contains("Invalid credentials"));
    }

    @Test(priority = 3)
    public void testCreateTodo() throws InterruptedException {
        driver.get("http://localhost:3000");
        login("admin", "password");
        Thread.sleep(1000);
        WebElement input = driver.findElement(By.id("new-todo"));
        input.sendKeys("Buy bread");
        driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
        Thread.sleep(1000);
        Assert.assertTrue(driver.getPageSource().contains("Buy bread"));
    }

    @Test(priority = 4)
    public void testEditTodo() throws InterruptedException {
        WebElement editInput = driver.findElement(By.id("edit-1"));
        editInput.clear();
        editInput.sendKeys("Buy bread and butter");
        driver.findElement(By.xpath("//button[contains(text(),'Save')]")).click();
        Thread.sleep(1000);
        Assert.assertTrue(driver.getPageSource().contains("Buy bread and butter"));
    }

    @Test(priority = 5)
    public void testDeleteTodo() throws InterruptedException {
        driver.findElement(By.xpath("//button[contains(text(),'Delete')]")).click();
        Thread.sleep(1000);
        Assert.assertFalse(driver.getPageSource().contains("Buy bread and butter"));
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
