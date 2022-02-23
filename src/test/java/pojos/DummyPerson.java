package pojos;

import org.bson.types.ObjectId;
import core.OmniRepoRoot;

public class DummyPerson implements OmniRepoRoot<ObjectId> {

    private String firstName;
    private String lastName;
    private int age;
    private ObjectId id;

    public DummyPerson() {
    }

    public DummyPerson(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public DummyPerson setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public DummyPerson setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public DummyPerson setAge(int age) {
        this.age = age;
        return this;
    }

}
