package me.ignpurple.ignlib.configuration.type;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Credit to Redempt for allowing me to use
 * this class to differentiate between generic types
 * and easily integrate it into my TypeAdapter System
 *
 * @author Redempt#0001
 */
public class ConfigField<T> {
    private final Class<T> clazz;
    private final List<ConfigField<?>> componentTypes;

    /**
     * Creates a ConfigType from an arbitrary Type
     *
     * @param type The Type
     * @return A ConfigType representation of the Type
     */
    public static ConfigField<?> create(Type type) {
        if (type instanceof Class) {
            return create((Class<?>) type, new ArrayList<>());
        }
        return create(type.getTypeName());
    }

    /**
     * Gets the ConfigType of a specific Field
     *
     * @param field The field
     * @return A ConfigType for the field
     */
    public static ConfigField<?> get(Field field) {
        return create(field.getGenericType());
    }

    private static ConfigField<?> create(String typeName) {
        try {
            int ind = typeName.indexOf('<');
            if (ind == -1) {
                return new ConfigField<>(Class.forName(typeName));
            }

            final Class<?> clazz = Class.forName(typeName.substring(0, ind));
            final List<ConfigField<?>> componentTypes = splitOnComma(typeName, ind + 1, typeName.length() - 1).stream().map(ConfigField::create).collect(Collectors.toList());
            return create(clazz, componentTypes);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("All parameter types for config must be known at compiletime", e);
        }
    }

    private static <T> ConfigField<T> create(Class<?> clazz, List<ConfigField<?>> componentTypes) {
        return new ConfigField<T>((Class<T>) clazz, componentTypes);
    }

    private static List<String> splitOnComma(String str, int start, int end) {
        int depth = 0;
        StringBuilder current = new StringBuilder();
        final List<String> split = new ArrayList<>();
        for (int i = start; i < end; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '<':
                    depth++;
                    break;
                case '>':
                    depth--;
                    break;
                case ',':
                    if (depth != 0) {
                        break;
                    }
                    split.add(current.toString().trim());
                    current = new StringBuilder();
                    continue;
            }
            current.append(c);
        }
        String last = current.toString().trim();
        if (last.length() != 0) {
            split.add(last);
        }
        return split;
    }

    /**
     * Constructs a ConfigType from a base class type and a list of component ConfigTypes
     *
     * @param clazz          The base class type
     * @param componentTypes The component types
     */
    public ConfigField(Class<T> clazz, List<ConfigField<?>> componentTypes) {
        this.clazz = clazz;
        this.componentTypes = componentTypes;
    }

    /**
     * Constructs a ConfigType with no component types
     *
     * @param clazz The class type
     */
    public ConfigField(Class<T> clazz) {
        this(clazz, new ArrayList<>());
    }

    /**
     * @return The base type of this ConfigType
     */
    public Class<T> getType() {
        return this.clazz;
    }
    
    /**
     * @return A list of all component types of this ConfigType
     */
    public List<ConfigField<?>> getComponentTypes() {
        return this.componentTypes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.clazz, this.componentTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConfigField)) {
            return false;
        }
        final ConfigField<?> type = (ConfigField<?>) o;
        return type.clazz.equals(this.clazz) && type.componentTypes.equals(this.componentTypes);
    }

    @Override
    public String toString() {
        String str = this.clazz.getName();
        if (this.componentTypes.size() > 0) {
            str += "<" + this.componentTypes.stream().map(ConfigField::toString).collect(Collectors.joining(", ")) + ">";
        }
        return str;
    }

}