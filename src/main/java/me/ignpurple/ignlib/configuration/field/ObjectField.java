package me.ignpurple.ignlib.configuration.field;

import java.lang.reflect.Field;

public class ObjectField {
    private Field field;
    private Object fieldObject;

    public ObjectField(Field field, Object fieldObject) {
        this.field = field;
        this.field.setAccessible(true);
        
        this.fieldObject = fieldObject;
    }

    public Field getField() {
        return this.field;
    }

    public Object getFieldObject() {
        return this.fieldObject;
    }
}
