package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    private int amountValid = 500;
    private int amountInvalid = 30000;

    private DashboardPage shouldOpenDashboardPage() {
        open("http://localhost:9999");
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        return verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromCard2toCard1() {
        DashboardPage dashboardPage = shouldOpenDashboardPage();
        dashboardPage.dashboardPageVisible();
        int expected1 = dashboardPage.getBalanceCard1() + amountValid;
        int expected2 = dashboardPage.getBalanceCard2() - amountValid;
        val moneyTransfer = dashboardPage.card1();
        moneyTransfer.moneyTransferVisible();
        moneyTransfer.setTransferAmount(amountValid);
        moneyTransfer.setFrom(DataHelper.getCardNumber2());
        moneyTransfer.doTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }

    @Test
    void InvalidAmountFromCard2toCard1() {
        DashboardPage dashboardPage = shouldOpenDashboardPage();
        dashboardPage.dashboardPageVisible();
        int expected1 = dashboardPage.getBalanceCard1() + amountInvalid;
        int expected2 = dashboardPage.getBalanceCard2() - amountInvalid;
        val moneyTransfer = dashboardPage.card1();
        moneyTransfer.moneyTransferVisible();
        moneyTransfer.setTransferAmount(amountInvalid);
        moneyTransfer.setFrom(DataHelper.getCardNumber2());
        moneyTransfer.doTransfer();
        assertEquals(expected1, dashboardPage.getBalanceCard1());
        assertEquals(expected2, dashboardPage.getBalanceCard2());
    }
}
