package ru.runner.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features",
        glue = {"stepdefs", "hooks"},
        tags = "@all",
        dryRun = false,
        strict = false,
        snippets = CucumberOptions.SnippetType.CAMELCASE
//        name = "^Успешное|Успешная.*"
)
public class RunnerTest {
}