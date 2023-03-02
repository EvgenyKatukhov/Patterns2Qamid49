package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.*;
import static ru.netology.testmode.data.DataGenerator.Registration.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class AuthTest {

    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        RegistrationDto registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $$("button").find(Condition.text("Продолжить")).click();
        $(byText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $$("button").find(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(visible).shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $$("button").find(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(visible).shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $$("button").find(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(visible).shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $$("button").find(Condition.text("Продолжить")).click();
        $("[data-test-id=error-notification]").shouldBe(visible).shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
