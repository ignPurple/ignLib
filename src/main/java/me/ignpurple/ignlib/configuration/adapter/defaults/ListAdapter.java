package me.ignpurple.ignlib.configuration.adapter.defaults;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.field.ObjectField;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter implements CustomFieldLoader {

    @Override
    public Object serialize(ConfigurationManager configurationManager, Object object) {
        final List<Object> objects = (List<Object>) object;
        final List<Object> serializedObjects = new ArrayList<>();
        for (final Object obj : objects) {
            serializedObjects.add(configurationManager.getLoader(obj.getClass()).serialize(configurationManager, obj));
        }
        return serializedObjects;
    }

    @Override
    public Object deserialize(ConfigurationManager configurationManager, ObjectField fieldValue, Object object) {
        final List<Object> fieldList = (List<Object>) fieldValue.getFieldObject();
        final ParameterizedType parameterizedType = (ParameterizedType) fieldList.getClass().getGenericSuperclass();
        final List<Object> objects = (List<Object>) object;
        final List<Object> deserializedObjects = new ArrayList<>();
        for (final Object obj : objects) {
            final Type type = parameterizedType.getActualTypeArguments()[0];
            if (type instanceof Class<?>) {
                deserializedObjects.add(configurationManager.getLoader((Class<?>) type).deserialize(configurationManager, null, obj));
                continue;
            }

            if (fieldList.size() == 0) {
                continue;
            }

            deserializedObjects.add(configurationManager.getLoader(fieldList.get(0).getClass()).deserialize(configurationManager, null, obj));
        }
        return deserializedObjects;
    }
}
