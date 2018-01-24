package uk.q3c.krail.testapp.selenide

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Created by David Sowerby on 23 Jan 2018
 */
object LoginSpek : Spek({
    context("Logging in and Logging out") {
        System.setProperty("selenide.browser", "chrome")
        System.setProperty("selenide.baseUrl", baseUrl)

        given("user is logged out") {

            on("user logs in with valid username and password") {
                val loginPage = LoginPage()
                loginPage.open()
                loginPage.username.value = "ds"
                loginPage.password.setValue("password").pressEnter()

                it("changes page to 'private home'") {
                    PrivateHomePage().shouldBeOpen()
                }
            }

            on("user logs out") {
                PrivateHomePage().logoutIfLoggedIn()

                it("should be at logout page") {
                    LogoutPage().shouldBeOpen()
                }
            }

            on("user tries to navigate back to private page, while still logged out") {

                browser.back()

                it("displays a notification that the page is not valid") {

                }

                it("does not open the target page, although browser url is directed at target") {
                    LogoutPage().shouldBeOpenWithUrl("private/home")
                }
            }

            on("user tries to log in with invalid credentials") {
                val loginPage = LoginPage()
                loginPage.open()
                loginPage.username.value = "ds"
                loginPage.password.setValue("rubbish").pressEnter()

                it("Should still be at login page") {
                    loginPage.shouldBeOpen()
                }

                it("login form should display 'login failed' banner'") {
                    loginPage.failedLoginBannerShouldBeVisible()
                }
            }


        }


    }
})


val baseUrl = "http://localhost:8080/krail-testapp/#"