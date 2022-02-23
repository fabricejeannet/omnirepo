package core;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class OmniRepo {

    public OmniRepo(String serverURI, String dataBaseName) {
        this.serverURI = serverURI;
        this.dataBaseName = dataBaseName;
        initMongo();
    }

    public void initMongo() {
        ConnectionString connectionString = new ConnectionString(serverURI);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        try {
            mongoClient = MongoClients.create(clientSettings);
            mongoDatabase = mongoClient.getDatabase(dataBaseName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void map (Class objClass) {
        String collectionName = getCollectionNameFromClass(objClass);
        map(objClass, collectionName);
    }


    public void map (Class objClass, String collection) {
        classCollectionMap.put(objClass, mongoDatabase.getCollection(collection, objClass));
    }

/*

    public void mapFromPackage(String packageName) throws IOException {
        ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName()
                        .equalsIgnoreCase(packageName))
                .map(clazz -> classCollectionMap.put(clazz, mongoDatabase.getCollection(getCollectionNameFromClass(clazz), clazz)));
    }

 */


    public String getCollectionNameFromClass(Class objClass) {
        return objClass.getSimpleName().toLowerCase();
    }

    public MongoCollection getCollectionMappedToClass(Class objClass) {
        return classCollectionMap.get(objClass);
    }

    public <T extends OmniRepoRoot> Object save (T object) {
        Class objClass = object.getClass();
        MongoCollection<T> collection = classCollectionMap.get(objClass);
        collection.insertOne(object);
        return (ObjectId) object.getId();
    }

    public <T> List<T> getAll(Class<T> objClass) {
        MongoCollection<T> collection = classCollectionMap.get(objClass);
        ArrayList<T> results = new ArrayList<>();
        collection.find().into(results);
        return results;
    }

    public <T> List<T> filter(Class objClass, Bson filters) {
        MongoCollection<T> collection = classCollectionMap.get(objClass);
        ArrayList<T> results = new ArrayList<>();
        collection.find(filters).into(results);
        return results;
    }

    public <T extends OmniRepoRoot> T get(Class<T> objClass, Object id) {
        MongoCollection<T> collection = classCollectionMap.get(objClass);
        return collection.find().filter(eq("_id", id)).first();
    }

    public <T> void delete(Class<T> objClass, Object id) {
        MongoCollection<T> collection = classCollectionMap.get(objClass);
        collection.deleteOne(Filters.eq("_id", id));
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    private Map<Class, MongoCollection> classCollectionMap = new HashMap<>();
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private String serverURI;
    private String dataBaseName;


}
