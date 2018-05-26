package uk.q3c.krail.testapp.view;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * Created by David Sowerby on 25 May 2018
 */
public class Person implements Serializable {

    private String title;
    private String name;
    @Max(12)
    private int age;

    public Person(String title, String name, int age) {
        this.title = title;
        this.name = name;
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
