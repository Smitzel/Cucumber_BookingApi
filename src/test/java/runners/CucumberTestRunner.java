package runners;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features") // Let op: geen "src/" prefix!
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "steps")
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@booking")
@ConfigurationParameter(key = Constants.EXECUTION_DRY_RUN_PROPERTY_NAME, value = "false")
//@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,value = "pretty, html:target/cucumber-report/cucumber.html")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:target/extent-report"
)
public class CucumberTestRunner {


}