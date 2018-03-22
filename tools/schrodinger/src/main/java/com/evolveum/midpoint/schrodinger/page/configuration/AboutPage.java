package com.evolveum.midpoint.schrodinger.page.configuration;

import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.FeedbackBox;
import com.evolveum.midpoint.schrodinger.page.BasicPage;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * Created by Viliam Repan (lazyman).
 */
public class AboutPage extends BasicPage {

    public AboutPage repositorySelfTest() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.testRepository")).click();
        return this;
    }

    public AboutPage checkAndRepairOrgClosureConsistency() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.testRepositoryCheckOrgClosure")).click();
        return this;
    }

    public AboutPage reindexRepositoryObjects() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.reindexRepositoryObjects")).click();
        return this;
    }

    public AboutPage provisioningSelfTest() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.testProvisioning")).click();
        return this;
    }

    public AboutPage cleanupActivitiProcesses() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.cleanupActivitiProcesses")).click();
        return this;
    }

    public AboutPage clearCssJsCache() {
        $(Schrodinger.byDataResourceKey("PageAbout.button.clearCssJsCache")).click();
        return this;
    }

    public String version() {
        return $(Schrodinger.bySchrodingerDataId("wicket_message-1130625241")).parent().getText();
    }

    public String gitDescribe() {
        return $(Schrodinger.bySchrodingerDataResourceKey("PageAbout.midPointRevision")).parent().getText();
    }

    public String buildAt() {
        return $(Schrodinger.bySchrodingerDataId("build")).parent().getText();
    }

    public String hibernateDialect() {
        SelenideElement aditionalDetails = $(Schrodinger.bySchrodingerDataId("wicket_message-1130625231"));
        return $(Schrodinger.bySchrodingerDataId("detailValue")).parent().getText();
    }

    public String connIdFrameworkVersion() {
        return $(Schrodinger.bySchrodingerDataId("provisioningDetailValue")).parent().getText();
    }

    public FeedbackBox<AboutPage> feedback() {

        SelenideElement feedback = $(By.cssSelector("div.feedbackContainer"));
        return new FeedbackBox<>(this, feedback);
    }
}

