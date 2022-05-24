package me.ignpurple.ignlib.configuration.adapter.defaults;

import me.ignpurple.ignlib.configuration.adapter.CustomFieldLoader;
import me.ignpurple.ignlib.configuration.manager.ConfigurationManager;

import java.lang.reflect.ParameterizedType;
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
    public Object deserialize(ConfigurationManager configurationManager, Object fieldValue, Object object) {
        final List<Object> fieldList = (List<Object>) fieldValue;
        final ParameterizedType parameterizedType = (ParameterizedType) fieldList;
        final List<Object> objects = (List<Object>) object;
        final List<Object> deserializedObjects = new ArrayList<>();
        for (final Object obj : objects) {
            deserializedObjects.add(configurationManager.getLoader((Class<?>) parameterizedType.getActualTypeArguments()[0]).deserialize(configurationManager, null, obj));
        }
        return deserializedObjects;
    }
}
