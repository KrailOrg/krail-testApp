package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;
import org.apache.onami.persist.EntityManagerProvider;
import org.apache.onami.persist.PersistenceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.q3c.krail.testapp.persist.*;

import javax.persistence.EntityManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({PModule.class})
public class JpaViewTest {

    private static final String PERSISTENCE_UNIT_NAME = "todos";
    private static EntityManagerFactory factory;
    @Inject
    Injector injector;
    //    EntityManager em;
    @Inject
    @Jpa1
    PersistenceService persistService1;

    @Inject
    @Jpa2
    PersistenceService persistService2;

    @Inject
    @Jpa1
    EntityManagerProvider entityManagerProvider1;

    @Inject
    @Jpa2
    EntityManagerProvider entityManagerProvider2;


    @Before
    public void setup() {
        //        File f = new File("/home/david/temp/derbyDb");
        //        if (f.exists()) {
        //            FileUtils.deleteQuietly(f);
        //        }
        //        f = new File("/home/david/temp/derbyDb2");
        //        if (f.exists()) {
        //            FileUtils.deleteQuietly(f);
        //        }

    }



    @Test
    public void injectedOutsideContainer() {
        //given
        persistService1.start();
        persistService2.start();


        Todo todo1_1 = newTodo("jpa1", 1);
        Widget widget1_1 = newWidget("jpa1", 1);
        Widget widget1_2 = newWidget("jpa1", 2);

        Todo todo2_1 = newTodo("jpa2", 1);
        Todo todo2_2 = newTodo("jpa2", 2);
        Todo todo2_3 = newTodo("jpa2", 3);

        Widget widget2_1 = newWidget("jpa2", 1);
        Widget widget2_2 = newWidget("jpa2", 2);
        Widget widget2_3 = newWidget("jpa2", 3);
        Widget widget2_4 = newWidget("jpa2", 4);

        DefaultGenericJpaDao dao1 = injector.getInstance(DefaultGenericJpaDao.class);
        dao1.setEntityManagerProvider(entityManagerProvider1);
        DefaultGenericJpaDao dao2 = injector.getInstance(DefaultGenericJpaDao.class);
        dao2.setEntityManagerProvider(entityManagerProvider2);


        //when
        dao1.persist(todo1_1);
        dao1.persist(widget1_1);
        dao1.persist(widget1_2);

        dao2.persist(todo2_1);
        dao2.persist(todo2_2);
        dao2.persist(todo2_3);

        dao2.persist(widget2_1);
        dao2.persist(widget2_2);
        dao2.persist(widget2_3);
        dao2.persist(widget2_4);

        //then
        assertThat(dao1.loadAll(Todo.class)).hasSize(1);
        assertThat(dao1.loadAll(Widget.class)).hasSize(2);
        assertThat(dao2.loadAll(Todo.class)).hasSize(3);
        assertThat(dao2.loadAll(Widget.class)).hasSize(4);
    }

    private Widget newWidget(String channel, int i) {
        Widget w = new Widget();
        w.setName(channel);
        w.setDescription("Widget " + i);
        return w;
    }

    private Todo newTodo(String channel, int i) {
        Todo w = new Todo();
        w.setName(channel);
        w.setDescription("Todo " + i);
        return w;
    }

    @After
    public void teardown() {
        //        if (em != null) {
        //            em.close();
        //        }
        System.out.println("stopping services");
        persistService1.stop();
        persistService2.stop();
    }



}