package cc.insidious.nexus.migration;

import cc.insidious.nexus.NexusApplication;
import cc.insidious.nexus.account.Account;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;

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
        Account account = new Account(
                "",
                "",
                "",
                "");


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

        handleStaySignedIn();

        instance.getEmailHandler().handleBackupEmail(account);
    }
    public void handlePopups(Account account){
        WebDriver driver = instance.getDriver();

        handleStaySignedIn();

        try {
            WebElement protectAccountElement = driver.findElement(By.ByCssSelector.cssSelector("#ModalFocusTrapZone24 > div.ms-Modal-scrollableContent.scrollableContent-170 > div.ms-Stack.dialogBody.css-190 > div > div > div > div.ms-Stack.css-247 > div.ms-Stack.css-249 > div.ms-Stack.css-250 > h1"));

            if(protectAccountElement != null){
                WebElement exitButton = driver.findElement(By.ByCssSelector.cssSelector("#acw\\.landing-page\\.dialog\\.close > span > i"));
                exitButton.click();
                System.out.println("help us protect your account popup found exiting.");
            }
        } catch (Exception e){
            System.out.println("No help us protect your account popup found skipping.");
        }

        try {
            WebElement breakFreePasswordsElement = driver.findElement(By.ByCssSelector.cssSelector("#setupAppTitle"));

            if(breakFreePasswordsElement != null){
                WebElement exitButton = driver.findElement(By.ByCssSelector.cssSelector("#iCancel"));
                exitButton.click();
                System.out.println("Break free from passwords element found and exited");

            }
        } catch (Exception e){
            System.out.println("No Break free from passwords element found skipping.");
        }

        try {
            Thread.sleep(5 * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Create xbox profile
        createXboxProfile(account);
    }

    private void handleStaySignedIn(){
        WebDriver driver = instance.getDriver();
        try {
            WebElement staySignedIn = driver.findElement(By.ByCssSelector.cssSelector("#kmsiTitle"));

            if(staySignedIn != null){
                WebElement yesButton = driver.findElement(By.ByCssSelector.cssSelector("#acceptButton"));
                yesButton.click();
                System.out.println("Found popup and clicked it!");
            }
        } catch (Exception e){
            System.out.println("No stay signed in popup found skipping!");
        }
    }

    private void createXboxProfile(Account account){
        WebDriver driver = instance.getDriver();
        String returnHandle = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to("https://www.minecraft.net/en-us/login");

        WebElement signInButton = driver.findElement(By.ByCssSelector.cssSelector("#LoginAnimation_Bee > div.container > div > div > section > div:nth-child(3) > div > div.login-form-view__first-container.p-4.d-flex.flex-column.justify-content-between > div:nth-child(1) > div > a"));
        signInButton.click();

        WebElement firstRandomName = driver.findElement(By.ByCssSelector.cssSelector("#create-account-gamertag-suggestion-1"));
        firstRandomName.click();

        WebElement continueButton = driver.findElement(By.ByCssSelector.cssSelector("#inline-continue-control"));
        continueButton.click();

        try {
            WebElement containsProfile = driver.findElement(By.ByCssSelector.cssSelector("#main-content > section > div > div.my-games-wrapper.mx-auto > div.my-games-container > div:nth-child(1) > div > div.games-card__text > div > div > a:nth-child(2) > div"));

            if(containsProfile != null){
                System.out.println("Profile found!");
            }
        } catch (Exception e){
            System.out.println("Profile not found");
        }

        driver.switchTo().window(driver.getWindowHandle()).close();
        driver.switchTo().window(returnHandle);
        changeEmailAndPassword(account);
    }

    private void changeEmailAndPassword(Account account){
        WebDriver driver = instance.getDriver();
        
    }

}
