package pojos;

import org.bson.types.ObjectId;
import core.OmniRepoRoot;

public class ObjectWithLogicInside implements OmniRepoRoot<ObjectId> {

    private ObjectId id;
    private int value;
    private DummyPerson person;

    public ObjectWithLogicInside(int value, DummyPerson person) {
        this.value = value;
        this.person = person;
    }

    public int getSomething() {
        return value * getPerson().getAge();
    }

    public int getValue() {
        return value;
    }

    public ObjectWithLogicInside setValue(int value) {
        this.value = value;
        return this;
    }

    public DummyPerson getPerson() {
        return person;
    }

    public ObjectWithLogicInside setPerson(DummyPerson person) {
        this.person = person;
        return this;
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }
}
