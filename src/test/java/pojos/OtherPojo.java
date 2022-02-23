package pojos;

import org.bson.types.ObjectId;
import core.OmniRepoRoot;

public class OtherPojo implements OmniRepoRoot<ObjectId> {

    private ObjectId id;
    private int value;

    public OtherPojo() {
    }

    public OtherPojo(int value) {
        this.value = value;
    }



    public int getValue() {
        return value;
    }

    public OtherPojo setValue(int value) {
        this.value = value;
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
