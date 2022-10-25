package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.UserInfo.*;


public class MoneyTransferTest {


    @Test
    void shouldIdTransferFromFirstToSecond() {
        var loginPage = new LoginPage();
        var LoginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateRandomValueInBalanceRange(firstCardBalance);
        var moneyTransferPage = dashboardPage.selectCard(secondCardInfo);
        moneyTransferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        assertEquals(firstCardBalance - amount, dashboardPage.getCardBalance(firstCardInfo));
        assertEquals(secondCardBalance + amount, dashboardPage.getCardBalance(secondCardInfo));
    }


    @Test
    void shouldTransferFromSecondToFirst() {
        var loginPage = new LoginPage();
        var LoginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateRandomValueInBalanceRange(secondCardBalance);
        var moneyTransferPage = dashboardPage.selectCard(firstCardInfo);
        moneyTransferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        assertEquals(secondCardBalance - amount, dashboardPage.getCardBalance(secondCardInfo));
        assertEquals(firstCardBalance + amount, dashboardPage.getCardBalance(firstCardInfo));
    }

    @Test
    void shouldShowErrorIfAmountOfChargeOverBalanceFirstToSecond() {
        var loginPage = new LoginPage();
        var LoginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateRandomValueOverBalanceRange(firstCardBalance);
        var moneyTransferPage = dashboardPage.selectCard(secondCardInfo);
        moneyTransferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        moneyTransferPage.findErrorMessage(
                "На картe " + firstCardInfo.getNumber().substring(12, 16) + " недостаточно средств");
        assertEquals(firstCardBalance - amount, dashboardPage.getCardBalance(firstCardInfo));
        assertEquals(secondCardBalance + amount, dashboardPage.getCardBalance(secondCardInfo));
    }

    @Test
    void shouldShowErrorIfAmountOfChargeOverBalanceSecondToFirst() {
        var loginPage = new LoginPage();
        var LoginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateRandomValueOverBalanceRange(secondCardBalance);
        var moneyTransferPage = dashboardPage.selectCard(firstCardInfo);
        moneyTransferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        moneyTransferPage.findErrorMessage(
                "На картe " + secondCardInfo.getNumber().substring(12, 16) + " недостаточно средств");
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo));
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo));
    }
}