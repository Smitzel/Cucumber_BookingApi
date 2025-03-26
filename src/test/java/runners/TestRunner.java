package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/features",
        glue = {"steps", "hooks"},
        plugin = {
                "pretty",
//                "json:target/reports/cucumber.json",
//                "html:target/reports/cucumber-reports.html",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        tags = "@booking"
//        TODO refactor to JUNIT 5 and add parallel execution
)
public class TestRunner {
}