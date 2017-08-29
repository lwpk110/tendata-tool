package cn.tendata.batch.support;

import java.util.Collection;

public interface ConfigOptionsResolver {

    <T> Collection<T> resolve(Class<T> type);
}
