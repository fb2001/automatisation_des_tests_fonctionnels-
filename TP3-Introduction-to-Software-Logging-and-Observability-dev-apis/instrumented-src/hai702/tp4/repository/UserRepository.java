package hai702.tp4.repository;
public class UserRepository {
    private com.mongodb.client.MongoCollection<org.bson.Document> collection;

    public UserRepository() {
        com.mongodb.client.MongoDatabase db = hai702.tp4.mongo.MongoConfig.getDatabase();
        collection = db.getCollection("users");
    }

    public void save(hai702.tp4.model.User user) {
        org.bson.Document doc = new org.bson.Document().append("id", user.getId()).append("name", user.getName()).append("age", user.getAge()).append("email", user.getEmail()).append("password", user.getPassword());
        collection.insertOne(doc);
    }

    public hai702.tp4.model.User findByEmail(java.lang.String email) {
        org.bson.Document doc = collection.find(com.mongodb.client.model.Filters.eq("email", email)).first();
        if (doc == null)
            return null;

        return new hai702.tp4.model.User(doc.getInteger("id"), doc.getString("name"), doc.getString("email"), doc.getString("password"), doc.getInteger("age"));
    }
}