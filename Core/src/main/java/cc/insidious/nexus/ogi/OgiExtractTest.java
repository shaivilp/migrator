package cc.insidious.nexus.ogi;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OgiExtractTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        String email = "";
        String password = "";

        driver.get("https://outlook.com/login");

        WebElement emailField = driver.findElement(By.cssSelector("input[type='email'][name='loginfmt']"));
        emailField.sendKeys(email);

        WebElement nextButton = driver.findElement(By.cssSelector("input[type='submit'][id='idSIButton9']"));
        nextButton.click();

        try {
            if (driver.findElement(By.cssSelector("div#usernameError")) != null) {
                String text = driver.findElement(By.cssSelector("div#usernameError")).getText();
                if (text.contains("We couldn't find an account with that username")) {
                    //Handle next account
                    System.out.println("Account with that username not found");
                }
            }
        } catch (Exception e) {
            System.out.println("Successfully found account");
        }

        WebElement passwordField = driver.findElement(By.cssSelector("input[type='password'][name='passwd']"));
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit'][id='idSIButton9']"));
        loginButton.click();

        try {
            WebElement staySignedIn = driver.findElement(By.cssSelector("#kmsiTitle"));
            if (staySignedIn != null) {
                driver.findElement(By.cssSelector("#acceptButton")).click();
                System.out.println("Found stay signed in popup and accepted it!");
            }
        } catch (NoSuchElementException e) {
            System.out.println("No stay signed in popup found skipping!");
        }


        WebElement searchField = driver.findElement(By.cssSelector("#topSearchInput"));
        searchField.sendKeys("microsoft order");

        WebElement button = driver.findElement(By.cssSelector("#searchSuggestion-1 > div"));
        button.click();

        Thread.sleep(3 * 1000);

        String datePattern = "Date purchased: (.+)";
        String orderNumberPattern = "Order Number: (.+)";
        String productKeyPattern = "Product Key: (.+)";
        String pKey = "";

        try {
            WebElement productKey = driver.findElement(By.cssSelector("#UniqueMessageBody > div > div > div > div.R1UVb > table > tbody > tr > td > center > table > tbody > tr > td > table.x_container.x_template-container.x_section > tbody > tr > td > table > tbody > tr > td > center > table.x_row.x_divider-light.x_margin-bottom-4 > tbody > tr > th > table > tbody > tr > th:nth-child(1) > table.x_table-default.x_table-heading-columns.x_show-for-large > tbody > tr > td.x_product > p:nth-child(3)"));
            pKey = productKey.getText();
        } catch (Exception e) {
            System.out.println("Unable to find product key");
        }

        String source = driver.getPageSource();

        String datePurchased = extractInfo(source, datePattern);
        String orderNumber = extractInfo(source, orderNumberPattern);
        String tid = extractInfo(pKey, productKeyPattern);

        System.out.println(datePurchased);
        System.out.println(orderNumber);
        System.out.println(tid);

        try(FileWriter fileWriter = new FileWriter("data/%email%.txt".replace("%email%", email))){
            String line = "---------------------------------------";
            String msg = line  +
                    "\nEmail: " + email +
                    "\nPurchase Date: " + datePurchased +
                    "\nOrder Number: #" + orderNumber +
                    "\nTID: " + tid +
                    "\n" + line;
            fileWriter.write(msg);
        }
    }

    private static String extractInfo(String input, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            return matcher.group(1).split("<")[0];
        } else {
            return "Not found";
        }
    }
}
