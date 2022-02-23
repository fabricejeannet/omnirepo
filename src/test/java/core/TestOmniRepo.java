package core;

import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import core.OmniRepo;
import pojos.DummyPerson;
import pojos.ObjectWithLogicInside;
import pojos.OtherPojo;
import pojos.Pojo;

import java.util.List;

import static org.junit.Assert.*;

public class TestOmniRepo {

    @Before
    public void before(){
        if (omniRepo == null) {
            omniRepo = new OmniRepo("mongodb://localhost:8080", "test_db");
        }
        omniRepo.getMongoDatabase().drop();
    }

    private void mapTestClasses() {
        omniRepo.map(Pojo.class);
        omniRepo.map(OtherPojo.class);
        omniRepo.map(DummyPerson.class);
    }

    @Test
    public void testCanSaveObject() {
        mapTestClasses();

        Pojo pojo = new Pojo();
        pojo.setTitle("My pojo");
        assertEquals(0, omniRepo.getAll(Pojo.class).size());
        ObjectId id = (ObjectId) omniRepo.save(pojo);
        assertNotNull(id);
        System.out.println(pojo);
        assertEquals(1, omniRepo.getAll(Pojo.class).size());

    }

    @Test
    public void testRetrieveAnObjectWithItsId() {
        mapTestClasses();
        Pojo pojo = new Pojo();
        pojo.setTitle("My super pojo");
        assertEquals(0, omniRepo.getAll(Pojo.class).size());
        ObjectId id = (ObjectId)  omniRepo.save(pojo);
        assertNotNull(id);
        Pojo retrievedPojo = omniRepo.get(Pojo.class, id);
        assertEquals(pojo, retrievedPojo);
    }


    @Test
    public void testCanDeleteObject() {
        mapTestClasses();
        Pojo pojo = new Pojo();
        pojo.setTitle("My super pojo");
        ObjectId id = (ObjectId)  omniRepo.save(pojo);

        assertNotNull(id);
        omniRepo.delete(Pojo.class, id);

        Pojo retrievedPojo = omniRepo.get(Pojo.class, id);
        assertNull(retrievedPojo);
    }

    @Test
    public void canRetrieveAllObjectsOfAGivenType() {
        omniRepo.map(Pojo.class, "pojos");

        for (int i = 0; i < 10; i++) {
            omniRepo.save(new Pojo("pojos.Pojo #" + i));
        }
        List<Pojo> pojos =  omniRepo.getAll(Pojo.class);
        assertEquals(10, pojos.size());
    }


    @Test
    public void testCanSaveDifferentTypesOfObjects() {
        mapTestClasses();

        Pojo pojo = new Pojo();
        pojo.setTitle("My pojo");
        OtherPojo otherPojo = new OtherPojo(12);

        assertEquals(0, omniRepo.getAll(Pojo.class).size());
        assertEquals(0, omniRepo.getAll(OtherPojo.class).size());

        omniRepo.save(pojo);
        omniRepo.save(otherPojo);

        assertEquals(1, omniRepo.getAll(Pojo.class).size());
        assertEquals(1, omniRepo.getAll(OtherPojo.class).size());

    }


    @Test
    public void canRetrieveObjectsViaOneFilters(){
        omniRepo.map(OtherPojo.class, "other_pojos");

        for (int i = 0; i < 10; i++) {
            omniRepo.save(new OtherPojo(i));
        }
        List<Pojo> pojos = omniRepo.filter(OtherPojo.class, Filters.gt("value", 4));
        assertEquals(5, pojos.size());
    }

    @Test
    public void canRetrieveObjectsViaMultipleFilters(){
        omniRepo.map(DummyPerson.class, "persons");

        for (int i = 0; i < 10; i++) {
            omniRepo.save(new DummyPerson(i % 2 == 0 ? "Steven":"Todd", "lastName" + i, i ));
        }
        List<DummyPerson> pojos = omniRepo.filter(DummyPerson.class, Filters.and(
                Filters.eq("firstName", "Steven"),
                Filters.gt("age", 3)
        ));
        assertEquals(3, pojos.size());
    }

    @Test
    public void canGetCollectionNameFromClassName() {
        assertEquals("pojo", omniRepo.getCollectionNameFromClass(Pojo.class));
    }

    @Test
    public void canAutoMapAGivenClass() {
        omniRepo.map(this.getClass());
        assertNotNull(omniRepo.getCollectionMappedToClass(this.getClass()));
    }

    @Test
    public void testCanSaveNestedObjectWithLogic() {
        omniRepo.map(ObjectWithLogicInside.class);
        DummyPerson dummyPerson = new DummyPerson();
        dummyPerson.setAge(10);
        dummyPerson.setFirstName("toto");
        dummyPerson.setLastName("titi");

        ObjectWithLogicInside object = new ObjectWithLogicInside(2, dummyPerson);
        ObjectId id = (ObjectId) omniRepo.save(object);

        assertNotNull(id);


    }

    /*
    @Test
    public void canMapFromPackage() throws IOException {
        helper.mapFromPackage("pojos");

    }
*/

    private OmniRepo omniRepo;
}
