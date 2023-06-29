package stepdefs;

import com.browserup.bup.BrowserUpProxyServer;
import org.openqa.selenium.WebDriver;
import stash.Context;
import stash.TestContext;

/**
 * Базовый класс Steps
 */
public class BaseSteps {

    /**
     * Класс-хранилище тестовых данных
     */
    public TestContext testContext;

    /**
     * Хромдрайвер
     */
    public WebDriver driver;

    /**
     * Прокси
     * @author Кирилл Желтышев
     */
    public BrowserUpProxyServer proxy;

    /**
     * Конструктор класса
     * @author Кирилл Желтышев
     */
    public BaseSteps() {
        this.testContext = TestContext.getInstance();
        this.driver = (WebDriver) this.testContext.get(Context.CHROMEDRIVER.toString());
        this.proxy = (BrowserUpProxyServer) this.testContext.get(Context.PROXY.toString());
    }

}
