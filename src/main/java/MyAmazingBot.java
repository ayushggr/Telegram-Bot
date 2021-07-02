import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class MyAmazingBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "animetimer_bot";
    }

    @Override
    public String getBotToken() {
        return "1538228592:AAEG74UruOBLgywjjJK2GKrY7faB0eDYKkc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String query = update.getMessage().getText().trim().replace(" ", "-");
            String url = "https://www.google.com/search?q=" + query;
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
                String element = document.select("a[href]").get(10).attr("href");
                System.out.println(element);
                System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
                WebDriver driver = new ChromeDriver();
                driver.get("https://ytmp3.cc/en13/");
                driver.findElement(By.id("input")).sendKeys(element);
                driver.findElement(By.id("submit")).click();
                WebElement ayush;
                WebDriverWait wait = new WebDriverWait(driver, 20);
                ayush = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"buttons\"]/a[1]")));
                String link = driver.findElement(By.xpath("//*[@id=\"buttons\"]/a[1]")).getAttribute("href");
                driver.close();
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(update.getMessage().getChatId()));
                if (link.length()==0) {
                    message.setText("Try again with other input");
                } else
                    message.setText(link+"                          Made by : Ayush");
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
