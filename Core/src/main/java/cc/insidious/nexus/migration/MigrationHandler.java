package cc.insidious.nexus.migration;

import cc.insidious.nexus.NexusApplication;
import cc.insidious.nexus.account.Account;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MigrationHandler {

    private final NexusApplication instance;


    public MigrationHandler(NexusApplication instance) {
        this.instance = instance;
        startMigrating();
    }
    public void startMigrating(){
//        Optional<Account> optional = instance.getAccountHandler().getNextAccount();
//
//        if (optional.isEmpty()) {
//            return;
//        }
        Account account = new Account("nosalcibwzjx@hotmail.com", "SJFvLzRjZ", "migratedalts22@alts.mcsniper.net", "pdqHLNKzSadImJNRc@580");

        String email = account.getEmail();
        String password = account.getPassword();

        WebDriver driver = instance.getDriver();
        driver.get("https://account.microsoft.com/account/manage-my-account");

        //Find and click initial sign-in button
        WebElement signInButton = driver.findElement(By.cssSelector("button[data-bi-id='signedout.hero.signIn']"));
        signInButton.click();

        //Find email and type it in
        WebElement emailField = driver.findElement(By.cssSelector("input[type='email'][name='loginfmt']"));
        emailField.sendKeys(email);

        //Click next button
        driver.findElement(By.cssSelector("input[type='submit'][id='idSIButton9']")).click();

        try {
            if(driver.findElement(By.cssSelector("div#usernameError")) != null){
                String text = driver.findElement(By.cssSelector("div#usernameError")).getText();
                if(text.contains("We couldn't find an account with that username")){
                    //Handle next account
                    System.out.println("Account with that username not found");
                }
            }
        } catch (Exception e){
            System.out.println("Successfully found account");
        }


        //Find email and type it in
        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password'][name='passwd']"));
        passwordField.sendKeys(password);

        //Click login button
        driver.findElement(By.cssSelector("input[type='submit'][id='idSIButton9']")).click();
        instance.getEmailHandler().handleBackupEmail(account);
    }

}
