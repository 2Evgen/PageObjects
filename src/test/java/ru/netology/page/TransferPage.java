package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.UserInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;


public class TransferPage {
    private final SelenideElement amountInput = $x("//div[@data-test-id='amount']//input[@class='input__control']");
    private final SelenideElement fromInput = $x("//span[@data-test-id='from']//input[@class='input__control']");
    private final SelenideElement submitRecharge = $x("//button[@data-test-id='action-transfer']");
    private final SelenideElement errorMessage = $("[data-test-id='error-message']");


    public TransferPage() {
        SelenideElement rechargeHead = $(byText("Пополнение карты"));
        rechargeHead.should(visible);
    }

    public DashboardPage makeRecharge(String amountToTransfer, UserInfo.CardInfo cardInfo) {
        rechargeCard(amountToTransfer, cardInfo);
        return new DashboardPage();
    }

    public void rechargeCard(String amountToTransfer, UserInfo.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        fromInput.setValue(cardInfo.getNumber());
        submitRecharge.click();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
