package uk.q3c.krail.testapp.p

import org.junit.After
import org.junit.Before
import org.junit.Test
import uk.q3c.krail.functest.*


/**
 * Created by David Sowerby on 01 Feb 2018
 */
class LogInOutFunctionalTest {
//    //    given("we want to test logging in and out") {
//    companion object {
//
//        @BeforeClass
//        @JvmStatic
//        fun beforeClass() {
//
//            executionMode = ExecutionMode.SELENIDE
////        executionMode = ExecutionMode.CODED
//            createBrowser()
//
//        }
//    }

    @Before
    fun before() {
        executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
        createBrowser()
        browser.navigateTo("home")

    }

    @After
    fun after() {
//        println("Sleeping after")
//        Selenide.close()
    }


    @Test
    fun loginOut() {
//on("navigating to login page") {
        val page = TestAppUIObject()
        page.userStatus.login_logout_Button.click()


        //   it("should be at login page") {
        browser.fragmentShouldBe("login")

//    it("displays correct captions") {
        val view = DefaultLoginViewObject()
        view.password.captionShouldBe("Password")
        view.username.captionShouldBe("User Name")


        //  it("shows users status as guest, and Log In on button") {
        page.userStatus.usernameLabel.valueShouldBe("Guest")
        page.userStatus.login_logout_Button.captionShouldBe("log in")

// on("entering invalid credentials") {
//    val view = DefaultLoginViewObject()
        view.username.setValue("ds")
        view.password.setValue("rubbish")
        view.submit.click()

//    it("shows failure message") {
        view.statusMsgLabel.valueShouldBe("That username or password was not recognised")


//on("entering valid credentials") {
//    val view = DefaultLoginViewObject()
//    val page = TestAppUIObject()
        view.username.setValue("ds")
        view.password.setValue("password")
        view.submit.click()


//    it("navigates to private home page") {
        browser.fragmentShouldBe("home")

//    it("shows users status as user name, and 'log out' on button") {
        page.userStatus.usernameLabel.valueShouldBe("ds")
        page.userStatus.login_logout_Button.captionShouldBe("log out")

//  on("logout") {
        page.userStatus.login_logout_Button.click()


//    it("goes to logout page") {
        browser.fragmentShouldBe("logout")

//    it("changes labels in user status") {
        page.userStatus.usernameLabel.valueShouldBe("Guest")
        page.userStatus.login_logout_Button.captionShouldBe("log in")

    }
}