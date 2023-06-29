import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Класс запуска тестов Cucumber
 * @author Кирилл Желтышев
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepdefs", "hooks"},
        plugin = {"pretty", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm", "json:target/cucumber-report/report.json"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTest {
}