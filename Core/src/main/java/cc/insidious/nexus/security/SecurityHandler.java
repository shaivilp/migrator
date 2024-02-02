package cc.insidious.nexus.security;

import cc.insidious.nexus.NexusApplication;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityHandler {
    private final NexusApplication instance;

    public SecurityHandler(NexusApplication instance) {
        this.instance = instance;
    }

    public int getSecurityCode(int emailId){
        WebDriver driver = instance.getDriver();
        //Create new tab
        driver.switchTo().newWindow(WindowType.TAB);

        try {
            Thread.sleep(30 * 1000L);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        //Load latest email
        driver.navigate().to("https://mail.mcsniper.net/mail/?_task=mail&_uid=" + emailId + "&_mbox=INBOX&_action=viewsource&_extwin=1");

        //Parse the email source for 6 digit code
        String source = driver.getPageSource();
        Pattern pattern = Pattern.compile("Security code: (\\d{6})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);

        //Close current tab
        driver.switchTo().window(driver.getWindowHandle()).close();

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return -1;
        }
    }
}
