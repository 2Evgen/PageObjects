package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.UserInfo;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    private final ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        SelenideElement heading = $("[data-test-id=dashboard]");
        heading.shouldBe(visible);
    }

    public int checkBalance(UserInfo.CardInfo cardInfo) {
        String text = cards.findBy(text(cardInfo.getNumber().substring(12, 16))).getText();
        return extractBalance(text);
    }

    public TransferPage selectCard(UserInfo.CardInfo cardInfo) {
        cards.findBy(attribute("data-test-id", cardInfo.getTestId())).$("button").click();
        return new TransferPage();
    }
    private int extractBalance(String text) {
        String balanceStart = "баланс: ";
        var start = text.indexOf(balanceStart);
        String balanceFinish = " р.";
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

}