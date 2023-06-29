package hooks;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.CaptureType;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import stash.Context;
import stash.TestContext;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;

/**
 * Класс задает методы, которые могут выполняться в различных точках цикла выполнения Cucumber.
 * Обычно они используются для настройки и демонтажа среды до и после каждого сценария
 */
public class Hooks {

    /**
     * Инициализация и настройка драйвера
     * value указывает, для каких тегов выполнить метод
     */
    @Before()
    public void initializeWebDriver(){
        System.out.println("Инициализация веб-драйвера");

        BrowserUpProxyServer proxy = new BrowserUpProxyServer();
        proxy.start(0);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver(ChromeDriverService.createDefaultService(), getOptionsChrome(proxy));
        TestContext testContext = TestContext.getInstance();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        //заносим объект веб-драйвера в хранилище
        testContext.put(String.valueOf(Context.CHROMEDRIVER),driver);
        testContext.put(String.valueOf(Context.PROXY),proxy);
    }

    /**
     * Завершение работы драйвера
     */
    @After()
    public void quitWebDriver() {
        System.out.println("Завершение работы веб-драйвера");
        TestContext testContext = TestContext.getInstance();
        ChromeDriver chromeDriver = (ChromeDriver) testContext.get(String.valueOf(Context.CHROMEDRIVER));
        chromeDriver.quit();
        BrowserUpProxyServer proxy = (BrowserUpProxyServer) testContext.get(Context.PROXY.name());
        proxy.stop();
    }

    /**
     * Получение методанных сценария по завершении теста
     * @param scenario - объект метаданных сценария
     * отсутствие тега после @Before говорит о том, что метод применяется ко всем тестам
     */
    @Before
    public void getScenarioInfo(Scenario scenario) {
        System.out.println("____________________________");
        System.out.println(scenario.getId());
        System.out.println(scenario.getName());
        System.out.println(scenario.getStatus());
        System.out.println(scenario.isFailed());
        System.out.println(scenario.getSourceTagNames());
        System.out.println(scenario.getUri());
        System.out.println("____________________________");
    }

    /**
     * Метод настроек web-драйвера перед каждым тестом.
     * @author Кирилл Желтышев
     */
    private ChromeOptions getOptionsChrome(BrowserUpProxyServer proxy) {
        final Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        final String proxyAddress = resolveProxyAddress(proxy);

        seleniumProxy.setHttpProxy(proxyAddress);
        seleniumProxy.setSslProxy(proxyAddress);

        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.setAcceptInsecureCerts(true);

        return options;
    }

    /**
     * Метод возвращает адресс прокси
     * @param proxy прокси
     * @return адресс прокси в виде строки
     */
    private String resolveProxyAddress(BrowserUpProxy proxy) {
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            return hostIp + ":" + proxy.getPort();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Не удалось создать адресс прокси:", e);
        }
    }
}
