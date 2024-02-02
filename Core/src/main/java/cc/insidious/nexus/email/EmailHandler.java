package cc.insidious.nexus.email;

import cc.insidious.nexus.NexusApplication;
import cc.insidious.nexus.account.Account;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

import java.time.Duration;

public class EmailHandler {
    private final NexusApplication instance;

    public EmailHandler(NexusApplication instance) {
        this.instance = instance;
    }

    public void handleBackupEmail(Account account) {
        WebDriver driver = instance.getDriver();
        String email = account.getMigrationEmail();
        String password = account.getMigrationPassword();

        WebElement emailField = driver.findElement(By.cssSelector("input[type='email'][id='EmailAddress']"));

        //Write email to field
        emailField.sendKeys(email);

        //Click next button
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit'][id='iNext']"));
        submitButton.click();

        String originalTab = driver.getWindowHandle();

        //Open new window to log in to email
        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to("https://mail.mcsniper.net/mail/");

        driver.findElement(By.cssSelector("input[name='_user']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[name='_pass']")).sendKeys(password);
        driver.findElement(By.cssSelector("button#rcmloginsubmit")).click();

        //Close current tab
        driver.switchTo().window(driver.getWindowHandle()).close();

        //Switch to original tab
        driver.switchTo().window(originalTab);

        String pageToReturn = driver.getWindowHandle();

        //Get the current code under the current driver.
        int code = instance.getSecurityHandler().getSecurityCode(1);

        if (code == -1) {
            System.out.println("Invalid security code.");
            return;
        }

        //Switch to the previous window, which we're passing in.
        driver.switchTo().window(pageToReturn);

        //Write code and click submit
        WebElement verifyCode = driver.findElement(By.cssSelector("input[type='tel'][id='iOttText']"));
        verifyCode.sendKeys(String.valueOf(code));

        try {
            Thread.sleep(5 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector("input[type='submit'][id='iNext']")).click();

        verifyBackupEmail(driver, account);

    }

    private void verifyBackupEmail(WebDriver driver, Account account){
        String email = account.getMigrationEmail();

        //TODO: Check if page even comes up
        WebElement radioButton = driver.findElement(By.cssSelector("input[type='radio'][name='proof'][id='iProof0']"));
        radioButton.click();

        WebElement emailField = driver.findElement(By.cssSelector("input[type='email'][id='iProofEmail']"));
        emailField.sendKeys(email);

        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit'][id='iSelectProofAction']"));
        submitButton.click();

        String returnPage = driver.getWindowHandle();

        int code = instance.getSecurityHandler().getSecurityCode(1);

        if (code == -1) {
            System.out.println("Invalid security code.");
            return;
        }

        driver.switchTo().window(returnPage);

        WebElement verifyCode = driver.findElement(By.cssSelector("input#iOttText[type='tel']"));
        verifyCode.sendKeys(String.valueOf(code));

        WebElement verifyCodeButton = driver.findElement(By.cssSelector("input#iVerifyCodeAction[type='submit']"));
        verifyCodeButton.click();
    }
}
