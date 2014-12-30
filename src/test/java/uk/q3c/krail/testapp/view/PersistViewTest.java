package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import org.apache.commons.io.FileUtils;
import org.apache.onami.persist.EntityManagerProvider;
import org.apache.onami.persist.PersistenceService;
import org.apache.onami.persist.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.q3c.krail.testapp.persist.Jpa1;
import uk.q3c.krail.testapp.persist.PModule;
import uk.q3c.krail.testapp.persist.Todo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({PModule.class})
public class PersistViewTest {

    private static final String PERSISTENCE_UNIT_NAME = "todos";
    private static EntityManagerFactory factory;
    @Inject
    Injector injector;
    EntityManager em;
    @Inject
    //    @Jpa1
            PersistenceService persistService;
    @Inject
    //    @Jpa1
            EntityManagerProvider em2;

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

    @Transactional(onUnits = Jpa1.class)
    @Test
    public void injected() {
        //given
        persistService.start();
        //when

        //then
        assertThat(persistService.isRunning());
        assertThat(em2.get()).isNotNull();
        em = em2.get();
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

    @Test
    public void injected2() {
        //given
        persistService.start();
        JpaTestClass jpaTestClass = injector.getInstance(JpaTestClass.class);
        //when
        jpaTestClass.save();
        //then
        assertThat(jpaTestClass.readAll()).hasSize(1);
        System.out.println(jpaTestClass.readAll()
                                       .get(0));
    }

    @After
    public void teardown() {
        if (em != null) {
            em.close();
        }
        System.out.println("stopping service");
        persistService.stop();
    }

    public static class JpaTestClass {

        private EntityManagerProvider entityManagerProvider;


        @Inject
        public JpaTestClass(EntityManagerProvider entityManagerProvider) {
            this.entityManagerProvider = entityManagerProvider;
        }

        @Transactional
        public void save() {
            Todo todo = new Todo();
            todo.setSummary("To do summary");
            todo.setDescription("This is a wiggly test");

            EntityManager entityManager = entityManagerProvider.get();
            //            entityManager.getTransaction().begin();

            entityManager.persist(todo);

        }

        @Transactional
        public List<Todo> readAll() {
            EntityManager entityManager = entityManagerProvider.get();
            Query q = entityManager.createQuery("select t from Todo t");
            List<Todo> todoList = q.getResultList();
            return todoList;
        }

    }

}