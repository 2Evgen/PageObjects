package ru.netology.test;

import org.junit.jupiter.api.Test;

import ru.netology.data.UserInfo;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.UserInfo.*;


public class MoneyTransferTest {


    @Test
    void shouldIdTransferFromFirstToSecond() {

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = UserInfo.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = UserInfo.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = UserInfo.getFirstCardInfo();
        var secondCardInfo = UserInfo.getSecondCardInfo();
        var firstCardBalance = dashboardPage.checkBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.checkBalance(secondCardInfo);
        var amount = UserInfo.generateRandomValueInBalanceRange(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCard(secondCardInfo);
        dashboardPage = transferPage.makeRecharge(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.checkBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.checkBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferFromSecondToFirst() {

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = UserInfo.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = UserInfo.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = UserInfo.getFirstCardInfo();
        var secondCardInfo = UserInfo.getSecondCardInfo();
        var firstCardBalance = dashboardPage.checkBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.checkBalance(secondCardInfo);
        var amount = UserInfo.generateRandomValueInBalanceRange(secondCardBalance);
        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCard(firstCardInfo);
        dashboardPage = transferPage.makeRecharge(String.valueOf(amount), secondCardInfo);
        var actualBalanceFirstCard = dashboardPage.checkBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.checkBalance(secondCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldShowErrorIfAmountOfChargeOverBalanceFirstToSecond() {

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = UserInfo.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = UserInfo.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = UserInfo.getFirstCardInfo();
        var secondCardInfo = UserInfo.getSecondCardInfo();
        var firstCardBalance = dashboardPage.checkBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.checkBalance(secondCardInfo);
        var amount = generateRandomValueOverBalanceRange(firstCardBalance);
        var transferPage = dashboardPage.selectCard(secondCardInfo);
        transferPage.rechargeCard(String.valueOf(amount), firstCardInfo);
        transferPage.findErrorMessage("Ошибка");
        var actualBalanceFirstCard = dashboardPage.checkBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.checkBalance(secondCardInfo);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }

    @Test
    void shouldShowErrorIfAmountOfChargeOverBalanceSecondToFirst() {

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = UserInfo.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = UserInfo.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = UserInfo.getFirstCardInfo();
        var secondCardInfo = UserInfo.getSecondCardInfo();
        var firstCardBalance = dashboardPage.checkBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.checkBalance(secondCardInfo);
        var amount = generateRandomValueOverBalanceRange(secondCardBalance);
        var transferPage = dashboardPage.selectCard(firstCardInfo);
        transferPage.rechargeCard(String.valueOf(amount), secondCardInfo);
        transferPage.findErrorMessage("Ошибка");
        var actualBalanceFirstCard = dashboardPage.checkBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.checkBalance(secondCardInfo);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }
}