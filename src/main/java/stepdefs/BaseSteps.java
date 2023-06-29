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
     */
    public BrowserUpProxyServer proxy;

    public BaseSteps() {
        this.testContext = TestContext.getInstance();
        // драйвер берется из хранилища в классе контекста
        this.driver = (WebDriver) this.testContext.get(Context.CHROMEDRIVER.toString());
        // прокси берётся из хранилища в классе контекста
        this.proxy = (BrowserUpProxyServer) this.testContext.get(Context.PROXY.toString());
    }

}
