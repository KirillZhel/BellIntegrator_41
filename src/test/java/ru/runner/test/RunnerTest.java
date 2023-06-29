package ru.runner.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepdefs", "hooks"},
        plugin = {"pretty", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm", "json:target/cucumber-report/report.json"},
        //plugin = {"pretty", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm"},
        //tags = "@all",
        //dryRun = false,
        //strict = true,
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTest {
}