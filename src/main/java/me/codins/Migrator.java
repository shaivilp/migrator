package me.codins;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.regex.Pattern;

public class Migrator {
    public static void main( String[] args ) {
        String email_password = "dhoopfjkebetw@hotmail.com:ShMnaIMNj";
        String migrate_email_password = "migratedalts20@alts.mcsniper.net:iLbEpxSUJASADLGVX$921";
        startMigrating(email_password, migrate_email_password);
    }

    public static void startMigrating(String accountCombo, String migrateCombo){
        WebDriver driver = new ChromeDriver();
        driver.get("https://account.microsoft.com/account/manage-my-account");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        WebElement signInButton = driver.findElement(By.cssSelector("button[data-bi-id='signedout.hero.signIn']"));
        signInButton.click();

        //Find email and type it in
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email'][name='loginfmt']"));
        emailField.sendKeys(accountCombo.split(":")[0]);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        //Click next button
        driver.findElement(By.cssSelector("input[type='submit'][id='idSIButton9']")).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        try {

            if(driver.findElement(By.cssSelector("div#usernameError")) != null){
                String text = driver.findElement(By.cssSelector("div#usernameError")).getText();
                if(text.contains("We couldn't find an account with that username")){
                    //Handle next account
                    System.out.println("Account with that username not found");
                }
            }
        } catch (NoSuchElementException e){
            System.out.println("Successfully found account");
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        //Find email and type it in
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password'][name='passwd']"));
        passwordField.sendKeys(accountCombo.split(":")[1]);

        //Click login button
        driver.findElement(By.cssSelector("input[type='submit'][id='idSIButton9']")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        String webHandle = driver.getWindowHandle();

        helpProtectYourAccount(driver, webHandle,  migrateCombo);
    }

    public static void helpProtectYourAccount(WebDriver driver, String handleToReturn, String migrateCombo){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email'][id='EmailAddress']"));
        emailField.sendKeys(migrateCombo.split(":")[0]);

        //Click next button
        driver.findElement(By.cssSelector("input[type='submit'][id='iNext']")).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to("https://mail.mcsniper.net/mail/");
        driver.findElement(By.cssSelector("input[name='_user']")).sendKeys(migrateCombo.split(":")[0]);
        driver.findElement(By.cssSelector("input[name='_pass']")).sendKeys(migrateCombo.split(":")[1]);
        driver.findElement(By.cssSelector("button#rcmloginsubmit")).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.switchTo().window(driver.getWindowHandle()).close();
        driver.switchTo().window(handleToReturn);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        String pageToReturn = driver.getWindowHandle();
        int code = getSecurity(driver);

        driver.switchTo().window(pageToReturn);

        driver.findElement(By.cssSelector("input[type='tel'][id='iOttText']")).sendKeys(String.valueOf(code));
        driver.findElement(By.cssSelector("input[type='submit'][id='iNext']")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    public static int getSecurity(WebDriver driver){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to("https://mail.mcsniper.net/mail/?_task=mail&_uid=1&_mbox=INBOX&_action=viewsource&_extwin=1");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        String source = driver.getPageSource();
        System.out.println(source);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        Pattern pattern = Pattern.compile("Security code: (\\d{6})");

        int code = Integer.parseInt(pattern.matcher(source).group(1));

        return code;
    }
}
