package uk.q3c.krail.app

import com.google.inject.AbstractModule
import com.google.inject.Inject
import org.slf4j.LoggerFactory
import uk.q3c.krail.core.form.FormDaoFactory
import uk.q3c.krail.startup.DefaultServletApplicationStartup
import uk.q3c.krail.startup.DefaultVertxApplicationStartup
import uk.q3c.krail.startup.ServletApplicationStartup
import uk.q3c.krail.startup.VertxApplicationStartup
import uk.q3c.krail.testapp.view.Person
import java.io.File

/**
 * Created by David Sowerby on 04 Aug 2018
 */
class TestAppVertxApplicationStartup @Inject constructor(val daoFactory: FormDaoFactory) : DefaultVertxApplicationStartup() {
    private val log = LoggerFactory.getLogger(this.javaClass.name)
    override fun executeCode() {
        val dbFile = File("/tmp/mapdb.db")
        if (dbFile.exists()) {
            dbFile.delete()
            log.debug("database file deleted")
        }
        val dao = daoFactory.getDao(Person::class)
        val p1 = Person(id = "1", name = "Monty", age = 42)
        val p2 = Person(id = "2", name = "Python", age = 43)
        val p3 = Person(id = "3", name = "Munchkin", age = 44)
        dao.insert(p1, p2, p3)
    }
}


class TestAppStartupModule : AbstractModule() {
    override fun configure() {
        bind(ServletApplicationStartup::class.java).to(DefaultServletApplicationStartup::class.java)
        bind(VertxApplicationStartup::class.java).to(TestAppVertxApplicationStartup::class.java)
    }

}