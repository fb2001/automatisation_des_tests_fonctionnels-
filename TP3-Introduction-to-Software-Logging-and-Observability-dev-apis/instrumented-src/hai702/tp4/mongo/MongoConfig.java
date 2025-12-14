// ----
// pour l'utilsation de mangodb sur ton ordi , 2 terminal :
// 1 er : mongod
// fadelbenomar@Mac-air-de-fadel TP3 % mongodb
// zsh: command not found: mongodb
// fadelbenomar@Mac-air-de-fadel TP3 % sudo mkdir -p /data/db
// sudo chown -R $(whoami) /data/db
// 
// Password:
// mkdir: /data: Read-only file system
// chown: /data/db: No such file or directory
// fadelbenomar@Mac-air-de-fadel TP3 % mongodb
// zsh: command not found: mongodb
// fadelbenomar@Mac-air-de-fadel TP3 % mkdir -p ~/data/db
// 
// fadelbenomar@Mac-air-de-fadel TP3 % mongod --dbpath ~/data/db
// 
// {"t":{"$date":"2025-11-04T11:00:22.112+01:00"},"s":"I",  "c":"CONTROL",  "id":23285,   "ctx":"thread1","msg":"Automatically disabling TLS 1.0, to force-enable TLS 1.0 specify --sslDisabledProtocols 'none'"}
// 2 eme : mongosh
// show dbs
// use database_mongo_tp3_HAI913I
// 
package hai702.tp4.mongo;
public class MongoConfig {
    private static final java.lang.String URI = "mongodb://localhost:27017";

    private static final java.lang.String DB_NAME = "database_mongo_tp3_HAI913I";

    private static com.mongodb.client.MongoClient client = com.mongodb.client.MongoClients.create(hai702.tp4.mongo.MongoConfig.URI);

    public static com.mongodb.client.MongoDatabase getDatabase() {
        return hai702.tp4.mongo.MongoConfig.client.getDatabase(hai702.tp4.mongo.MongoConfig.DB_NAME);
    }
}