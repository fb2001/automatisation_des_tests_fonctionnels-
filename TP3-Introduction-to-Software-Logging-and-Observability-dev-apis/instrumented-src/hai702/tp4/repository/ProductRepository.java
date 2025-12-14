package hai702.tp4.repository;
public class ProductRepository {
    private com.mongodb.client.MongoCollection<org.bson.Document> collection;

    public ProductRepository() {
        com.mongodb.client.MongoDatabase db = hai702.tp4.mongo.MongoConfig.getDatabase();
        collection = db.getCollection("products");
    }

    public void save(hai702.tp4.model.Product p) {
        org.bson.Document doc = new org.bson.Document().append("id", p.getId()).append("name", p.getName()).append("price", p.getPrice()).append("expirationDate", p.getExpirationDate());
        collection.insertOne(doc);
    }

    public hai702.tp4.model.Product findById(int id) {
        org.bson.Document doc = collection.find(com.mongodb.client.model.Filters.eq("id", id)).first();
        if (doc == null)
            return null;

        return new hai702.tp4.model.Product(doc.getInteger("id"), doc.getString("name"), doc.getDouble("price"), doc.getString("expirationDate"));
    }

    public boolean exists(int id) {
        return collection.find(com.mongodb.client.model.Filters.eq("id", id)).first() != null;
    }

    public void update(hai702.tp4.model.Product p) {
        org.bson.Document doc = new org.bson.Document().append("name", p.getName()).append("price", p.getPrice()).append("expirationDate", p.getExpirationDate());
        collection.updateOne(com.mongodb.client.model.Filters.eq("id", p.getId()), new org.bson.Document("$set", doc));
    }

    public void delete(int id) {
        collection.deleteOne(com.mongodb.client.model.Filters.eq("id", id));
    }

    public java.util.List<hai702.tp4.model.Product> findAll() {
        java.util.List<hai702.tp4.model.Product> list = new java.util.ArrayList<>();
        for (org.bson.Document doc : collection.find()) {
            list.add(new hai702.tp4.model.Product(doc.getInteger("id"), doc.getString("name"), doc.getDouble("price"), doc.getString("expirationDate")));
        }
        return list;
    }
}