package tests.config;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;

public class MongoOperator {
    public static MongoClient getClient(){
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        return mongoClient;
    }
    public static MongoDatabase getDb(String dbName){
        return getClient().getDatabase(dbName);
    }
    public static MongoCollection getCollection(String dbName, String collectionName){
        return getDb(dbName).getCollection(collectionName);
    }
    public static void main(String[] args){
        MongoCollection<Document> collection = getCollection("myDatabase", "docs");
        System.out.println(collection.count());
//        Document doc = new Document("id", "dokument1")
//                .append("content", "important");
//        collection.insertOne(doc);
        Document  myDoc = collection.find(eq("id", "dokument123")).first();
//        Document myDoc1 = new Document("id","dokument1");
//        collection.updateOne(myDoc, new Document("$set", myDoc1));
//        System.out.println(myDoc.toJson());
//        System.out.println(myDoc.toJson());


        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        System.out.println(collection.count());
    }

}
