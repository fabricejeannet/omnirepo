package pojos;

import org.bson.types.ObjectId;
import core.OmniRepoRoot;

import java.util.Objects;

public class Pojo implements OmniRepoRoot<ObjectId> {

    private ObjectId id;
    private String title;

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Pojo(String title) {
        this.title = title;
    }

    public Pojo() {
    }

    @Override
    public String toString() {
        return "pojos.Pojo{" +
                "title='" + title + "\'," +
                "id='"+ getId() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pojo pojo = (Pojo) o;
        return Objects.equals(title, pojo.title) && Objects.equals(getId(), pojo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
