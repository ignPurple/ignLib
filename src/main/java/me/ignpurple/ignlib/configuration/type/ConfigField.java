package me.ignpurple.ignlib.configuration.type;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigField<T> {
    private final Class<T> clazz;
    private final List<ConfigField<?>> componentTypes;

    public static ConfigField<?> create(Type type) {
        if (type instanceof Class) {
            return create((Class<?>) type, new ArrayList<>());
        }
        return create(type.getTypeName());
    }

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

    public ConfigField(Class<T> clazz, List<ConfigField<?>> componentTypes) {
        this.clazz = clazz;
        this.componentTypes = componentTypes;
    }

    public ConfigField(Class<T> clazz) {
        this(clazz, new ArrayList<>());
    }

    public Class<T> getType() {
        return this.clazz;
    }

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