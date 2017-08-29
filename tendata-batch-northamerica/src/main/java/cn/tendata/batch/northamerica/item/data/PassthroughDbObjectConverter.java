package cn.tendata.batch.northamerica.item.data;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

public class PassthroughDbObjectConverter implements Converter<DBObject, DBObject> {

    @Override
    public DBObject convert(DBObject source) {
        return source;
    }

}
