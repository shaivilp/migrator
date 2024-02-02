package cc.insidious.nexus;


import cc.insidious.nexus.account.AccountHandler;
import cc.insidious.nexus.email.EmailHandler;
import cc.insidious.nexus.migration.MigrationHandler;
import cc.insidious.nexus.security.SecurityHandler;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

@Getter
public class NexusApplication {

    private final WebDriver driver;
    private final AccountHandler accountHandler;
    private final MigrationHandler migrationHandler;
    private final EmailHandler emailHandler;
    private final SecurityHandler securityHandler;
    public NexusApplication() {
        this.driver = new ChromeDriver();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        this.accountHandler = new AccountHandler(this);
        this.emailHandler = new EmailHandler(this);
        this.securityHandler = new SecurityHandler(this);
        this.migrationHandler = new MigrationHandler(this);

    }

    public static void main(String[] args) {
        new NexusApplication();
    }


}
