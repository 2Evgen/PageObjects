package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.UserInfo;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {

    private int begBalance1;
    private int begBalance2;
    private int endBalance1;
    private int endBalance2;
    private int sum;
    DashboardPage dashboardPage;

    @BeforeEach
    void SetUp() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = UserInfo.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = UserInfo.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        begBalance1 = dashboardPage.getBalance(dashboardPage.card1);
        begBalance2 = dashboardPage.getBalance(dashboardPage.card2);
    }

    @Test
    @DisplayName("Перевод денег сo второй карты на первую")
    void shouldTransferMoneyFromSecondToFirstCard() throws InterruptedException {
        sum = 100;
        val topUpPage = dashboardPage.clickTopUp(dashboardPage.card1);
        val cardNum = UserInfo.getSecondCard().getNumber();
        val dashboardPage2 = topUpPage.successfulTransfer(Integer.toString(sum), cardNum);
        endBalance1 = dashboardPage2.getBalance(dashboardPage2.card1);
        endBalance2 = dashboardPage2.getBalance(dashboardPage2.card2);
        assertEquals(begBalance1 + sum, endBalance1);
        assertEquals(begBalance2 - sum, endBalance2);
    }

    @Test
    @DisplayName("Перевод денег с первой карты на вторую")
    void shouldTransferMoneyFromFirstToSecondCard() throws InterruptedException {
        sum = 100;
        val topUpPage = dashboardPage.clickTopUp(dashboardPage.card2);
        val cardNum = UserInfo.getFirstCard().getNumber();
        val dashboardPage2 = topUpPage.successfulTransfer(Integer.toString(sum), cardNum);
        endBalance1 = dashboardPage2.getBalance(dashboardPage2.card1);
        endBalance2 = dashboardPage2.getBalance(dashboardPage2.card2);
        assertEquals(begBalance1 - sum, endBalance1);
        assertEquals(begBalance2 + sum, endBalance2);
    }

    @Test
    @DisplayName("Не должен переводить больше, чем есть на карте")
    void shouldNotTransferMoreThanAvailable() {
        sum = begBalance1 + 100;
        val topUpPage = dashboardPage.clickTopUp(dashboardPage.card2);
        val cardNum = UserInfo.getFirstCard().getNumber();
        topUpPage.unsuccessfulTransfer(Integer.toString(sum), cardNum);
    }

}
