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
import uk.q3c.krail.testapp.persist.*;

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
    @Jpa1
    PersistenceService persistService1;

    @Inject
    @Jpa2
    PersistenceService persistService2;


    @Inject
    @Jpa1
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

    @Test
    public void injected() {
        //given
        persistService1.start();
        //when

        //then
        assertThat(persistService1.isRunning());
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
        persistService1.start();
        persistService2.start();
        JpaTestDao1 jpaTestDao1 = injector.getInstance(JpaTestDao1.class);
        JpaTestDao2 jpaTestDao2 = injector.getInstance(JpaTestDao2.class);
        //when
        jpaTestDao1.save();
        jpaTestDao2.save();
        //then
        assertThat(jpaTestDao1.readAll()).hasSize(2);
        assertThat(jpaTestDao2.readAll()).hasSize(2);
        System.out.println(jpaTestDao1.readAll()
                                       .get(0));
        System.out.println(jpaTestDao1.readAll()
                                      .get(1));
    }

    @After
    public void teardown() {
        if (em != null) {
            em.close();
        }
        System.out.println("stopping service");
        persistService1.stop();
        persistService2.stop();
    }

    public static class JpaTestDao1 {

        private EntityManagerProvider entityManagerProvider;


        @Inject
        public JpaTestDao1(@Jpa1 EntityManagerProvider entityManagerProvider) {
            this.entityManagerProvider = entityManagerProvider;
        }

        @Transactional
        public void save() {
            Todo todo1 = new Todo();
            todo1.setSummary("Jpa1");
            todo1.setDescription("pojo 1");

            Todo todo2 = new Todo();
            todo2.setSummary("Jpa1");
            todo2.setDescription("pojo 2");

            EntityManager entityManager = entityManagerProvider.get();


            entityManager.persist(todo1);
            entityManager.persist(todo2);
        }

        @Transactional
        public List<Todo> readAll() {
            EntityManager entityManager = entityManagerProvider.get();
            Query q = entityManager.createQuery("select t from Todo t");
            List<Todo> todoList = q.getResultList();
            return todoList;
        }

    }

    public static class JpaTestDao2 {

        private EntityManagerProvider entityManagerProvider;


        @Inject
        public JpaTestDao2(@Jpa2 EntityManagerProvider entityManagerProvider) {
            this.entityManagerProvider = entityManagerProvider;
        }

        @Transactional
        public void save() {
            Widget widget1 = new Widget();
            widget1.setName("Jpa2");
            widget1.setDescription("pojo 1");

            Widget widget2 = new Widget();
            widget2.setName("Jpa2");
            widget2.setDescription("pojo 2");

            EntityManager entityManager = entityManagerProvider.get();


            entityManager.persist(widget1);
            entityManager.persist(widget2);
        }

        @Transactional
        public List<Widget> readAll() {
            EntityManager entityManager = entityManagerProvider.get();
            Query q = entityManager.createQuery("select t from Widget t");
            List<Widget> widgetList = q.getResultList();
            return widgetList;
        }

    }

}