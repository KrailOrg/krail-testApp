package uk.q3c.krail.testapp.view;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.q3c.krail.testapp.persist.Todo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({})
public class PersistViewTest {

    private static final String PERSISTENCE_UNIT_NAME = "todos";
    private static EntityManagerFactory factory;
    EntityManager em;

    @Before
    public void setup() {
        File f = new File("/home/david/temp/simpleDb");
        if (f.exists()) {
            FileUtils.deleteQuietly(f);
        }

    }

    @Test
    public void basic() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
        // read the existing entries and write to console
        // create new todo
        em.getTransaction()
          .begin();
        Todo todo = new Todo();
        todo.setSummary("To do summary");
        todo.setDescription("This is a test");
        em.persist(todo);
        em.getTransaction()
          .commit();

        em.getTransaction()
          .begin();
        Todo todo2 = new Todo();
        todo2.setSummary("To do summary");
        todo2.setDescription("This is a test 2");
        em.persist(todo2);
        em.getTransaction()
          .commit();


        Query q = em.createQuery("select t from Todo t");
        List<Todo> todoList = q.getResultList();
        for (Todo tdo : todoList) {
            System.out.println(tdo);
        }

        assertThat(todoList).hasSize(2);

    }

    @After
    public void teardown() {
        em.close();
    }
}