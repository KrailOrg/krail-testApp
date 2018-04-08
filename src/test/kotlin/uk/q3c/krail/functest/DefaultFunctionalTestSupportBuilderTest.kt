package uk.q3c.krail.functest

import com.google.common.collect.ImmutableList
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provider
import com.nhaarman.mockito_kotlin.whenever
import com.vaadin.server.ErrorHandler
import com.vaadin.server.UICreateEvent
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinService
import com.vaadin.ui.AbstractOrderedLayout
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBeNull
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.core.i18n.I18NProcessor
import uk.q3c.krail.core.i18n.LabelKey
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.navigate.StrictURIFragmentHandler
import uk.q3c.krail.core.navigate.sitemap.DefaultMasterSitemap
import uk.q3c.krail.core.navigate.sitemap.MasterSitemap
import uk.q3c.krail.core.navigate.sitemap.MasterSitemapNode
import uk.q3c.krail.core.navigate.sitemap.SitemapService
import uk.q3c.krail.core.push.Broadcaster
import uk.q3c.krail.core.push.PushMessageRouter
import uk.q3c.krail.core.shiro.PageAccessControl
import uk.q3c.krail.core.ui.ApplicationTitle
import uk.q3c.krail.core.ui.DefaultUIModule
import uk.q3c.krail.core.ui.ScopedUI
import uk.q3c.krail.core.ui.ScopedUIProvider
import uk.q3c.krail.core.user.LoginLabelKey
import uk.q3c.krail.core.view.DefaultViewFactory
import uk.q3c.krail.core.view.ViewBase
import uk.q3c.krail.core.view.ViewFactory
import uk.q3c.krail.core.view.component.ComponentIdEntry
import uk.q3c.krail.core.view.component.ComponentIdGenerator
import uk.q3c.krail.core.view.component.DefaultComponentIdGenerator
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.i18n.CurrentLocale
import uk.q3c.krail.i18n.Translate
import uk.q3c.krail.option.Option
import uk.q3c.krail.testutil.guice.uiscope.TestUIScopeModule
import uk.q3c.util.UtilModule
import uk.q3c.util.guice.SerializationSupport
import uk.q3c.util.testutil.FileTestUtil
import uk.q3c.util.testutil.TestResource
import java.io.File


/**
 * Created by David Sowerby on 03 Feb 2018
 */
class DefaultFunctionalTestSupportBuilderTest : Spek({
    lateinit var injector: Injector
    lateinit var functionalTestSupportBuilder: FunctionalTestSupportBuilder
    lateinit var masterSitemap: MasterSitemap
    lateinit var uiProvider: ScopedUIProvider
    val mockRequest: VaadinRequest = mock()
    val mockService: VaadinService = mock()
    whenever(mockRequest.service).thenReturn(mockService)
    lateinit var fts: FunctionalTestSupport

    context("building a model of routes to views and UIs, and their components") {
        beforeGroup {
            injector = Guice.createInjector(IdGeneratorModule(), UtilModule(), TestUIScopeModule(), DefaultUIModule().uiClass(TestUI::class.java))
            functionalTestSupportBuilder = injector.getInstance(FunctionalTestSupportBuilder::class.java)
            masterSitemap = injector.getInstance(MasterSitemap::class.java)
            uiProvider = injector.getInstance(ScopedUIProvider::class.java)
            uiProvider.createInstance(UICreateEvent(mockRequest, TestUI::class.java))
            masterSitemap.addNode(MasterSitemapNode(1, "simple", SimpleView::class.java, LoginLabelKey.Log_In, -1, PageAccessControl.PUBLIC, ImmutableList.of()))
            masterSitemap.addNode(MasterSitemapNode(2, "simple/another", AnotherSimpleView::class.java, LabelKey.Active_Source, -1, PageAccessControl.PUBLIC, ImmutableList.of()))

        }

        on("extract") {
            fts = functionalTestSupportBuilder.generate()

            it("should have entries for 'simple' in the routeMap") {
                fts.routeMap.map["simple"].shouldNotBeNull()
                fts.routeMap.map["simple"]?.route?.shouldBeEqualTo("simple")
            }

            it("should have entries for 'another/simple' in the routeMap") {
                fts.routeMap.map["simple/another"].shouldNotBeNull()
                fts.routeMap.map["simple/another"]?.route?.shouldBeEqualTo("simple/another")
            }

            it("should have one entry for the UI") {
                fts.uis.keys.size.shouldBe(1)
                fts.uis.keys.first().uiId.name.shouldEqual("TestUI")
            }

            it("should contains ids for the view and its component") {
                fts.uis.values.forEach({ v ->
                    v.nodes()
                            .shouldContain(ComponentIdEntry(name = "TestUI", id = "TestUI", type = "TestUI", baseComponent = false))
                            .shouldContain(ComponentIdEntry(name = "label", id = "TestUI-label", type = "Label", baseComponent = true))
                })
            }

            it("should return the correct view and UI for a given route") {
                fts.routeMap.viewFor("simple").viewId.id.shouldBeEqualTo("SimpleView")
                fts.routeMap.viewFor("simple/another").viewId.id.shouldBeEqualTo("AnotherSimpleView")
                fts.routeMap.uiFor("simple").uiId.id.shouldBeEqualTo("TestUI")
                fts.routeMap.uiFor("simple/another").uiId.id.shouldBeEqualTo("TestUI")
            }

        }

        on("generating view objects") {
            val pageObjectGenerator = KotlinPageObjectGenerator()
            val actual = File("/tmp/PageObjects.kt")
            pageObjectGenerator.generate(fts, actual, "uk.q3c.krail.functest")

            it("looks like") {
                val expected = TestResource.resource(functionalTestSupportBuilder, "PageObjects.ref")
                val result = FileTestUtil.compare(actual, expected)
                result.ifPresent({
                    println(result.get())
                    throw AssertionError(result.get())
                })
            }
        }

        on("exporting Json") {
            fts.routeMap.toJson(File("/tmp/routeMap.json"))

            it("successfully round-trips") {
                val routeMap2 = routeMapFromJson(File("/tmp/routeMap.json"))
                assertThat(fts.routeMap).isEqualTo(routeMap2)

            }
        }
    }
})


class TestUI @Inject
protected constructor(navigator: Navigator,
                      errorHandler: ErrorHandler,
                      broadcaster: Broadcaster,
                      pushMessageRouter: PushMessageRouter,
                      applicationTitle: ApplicationTitle,
                      translate: Translate,
                      currentLocale: CurrentLocale,
                      translator: I18NProcessor,
                      serializationSupport: SerializationSupport) : ScopedUI(navigator, errorHandler, broadcaster, pushMessageRouter, applicationTitle, translate, currentLocale, translator, serializationSupport) {

    val label = Label("ui")

    override fun screenLayout(): AbstractOrderedLayout {
        return VerticalLayout(viewDisplayPanel)
    }


}

class IdGeneratorModule : AbstractModule() {
    val mockMasterSitemapProvider: Provider<MasterSitemap> = mock()
    val mockSitemapService: SitemapService = mock()
    val translate: Translate = mock()
    val mockCurrentLocale: CurrentLocale = mock()
    val masterSitemap: MasterSitemap = DefaultMasterSitemap(StrictURIFragmentHandler())
    val mockOption: Option = mock()
    val mockPushMessageRouter: PushMessageRouter = mock()
    val mockBroadcaster: Broadcaster = mock()
    val mockNavigator: Navigator = mock()
    val mockI18NProcessor: I18NProcessor = mock()
    val mockErrorHandler: ErrorHandler = mock()
    val mockSerializationSupport: SerializationSupport = mock()


    init {
        whenever(mockMasterSitemapProvider.get()).thenReturn(masterSitemap)
    }

    override fun configure() {
        bind(MasterSitemap::class.java).toInstance(masterSitemap)
        bind(ViewFactory::class.java).to(DefaultViewFactory::class.java)
        bind(Translate::class.java).toInstance(translate)
        bind(CurrentLocale::class.java).toInstance(mockCurrentLocale)
        bind(SitemapService::class.java).toInstance(mockSitemapService)
        bind(FunctionalTestSupportBuilder::class.java).to(DefaultFunctionalTestSupportBuilder::class.java)
        bind(ComponentIdGenerator::class.java).to(DefaultComponentIdGenerator::class.java)
        bind(Option::class.java).toInstance(mockOption)
        bind(PushMessageRouter::class.java).toInstance(mockPushMessageRouter)
        bind(Broadcaster::class.java).toInstance(mockBroadcaster)
        bind(Navigator::class.java).toInstance(mockNavigator)
        bind(I18NProcessor::class.java).toInstance(mockI18NProcessor)
        bind(ErrorHandler::class.java).toInstance(mockErrorHandler)
        bind(SerializationSupport::class.java).toInstance(mockSerializationSupport)

    }

}

class SimpleView @Inject constructor(translate: Translate, serializationSupport: SerializationSupport) : ViewBase(translate, serializationSupport) {
    lateinit var label: Label
    lateinit var custom: TestCustomComponent
    override fun doBuild(busMessage: ViewChangeBusMessage?) {
        label = Label("boo")
        custom = TestCustomComponent()
    }
}

class AnotherSimpleView @Inject constructor(translate: Translate, serializationSupport: SerializationSupport) : ViewBase(translate, serializationSupport) {
    lateinit var button: Button
    override fun doBuild(busMessage: ViewChangeBusMessage?) {
        button = Button("boo button")
    }
}

class TestCustomComponent : Panel() {
    var labelInCustom = Label()
    var butonnInCustom = Button()
}