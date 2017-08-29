package cn.tendata.batch.northamerica.item.data;

import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements InitializingBean {

    private MongoDbFactory mongoDbFactory;
    
    private String databaseName;
    
    private DBCollection collection;
    
    private String collectionName;
    
    private DBCursor cursor;
    
    private String [] fields;
    
    private DBObject refDbObject;
    
    private Converter<DBObject,T> dbObjectConverter;
    
    public MongoCursorItemReader() {
        super();
        setName(ClassUtils.getShortName(MongoCursorItemReader.class));
    }
    
    @Override
    protected void doOpen() throws Exception {
        cursor = collection.find(createDbObjectRef(),createDbObjectKeys());
    }

    @Override
    protected T doRead() throws Exception {
        if(!cursor.hasNext()) {
            return null;
        } else {
            DBObject dbObj = cursor.next();
            return dbObjectConverter.convert(dbObj);
        }
    }
    
    @Override
    protected void doClose() throws Exception {
        cursor.close();
    }
    
    @Override
    protected void jumpToItem(int itemIndex) throws Exception {
        cursor = cursor.skip(itemIndex);
    }
    
    protected DBObject createDbObjectKeys() {
        if(fields == null) {
            return new BasicDBObject();
        } else {
            BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
            for(String field : fields) {
                builder.add(field,1);
            }
            return builder.get();
        }
    }

    protected DBObject createDbObjectRef() {
        if(refDbObject == null) {
            return new BasicDBObject();
        } else {
            return refDbObject;
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mongoDbFactory,"mongoDbFactory must be specified");
        Assert.notNull(collectionName,"collectionName must be set");
        DB db = databaseName != null ? mongoDbFactory.getDb(databaseName) : mongoDbFactory.getDb();
        collection = db.getCollection(collectionName);
    }
    
    public void setRefDbObject(DBObject refDbObject) {
        this.refDbObject = refDbObject;
    }
    
    public void setMongoDbFactory(MongoDbFactory mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setDbObjectConverter(Converter<DBObject, T> dbObjectConverter) {
        this.dbObjectConverter = dbObjectConverter;
    }
}
