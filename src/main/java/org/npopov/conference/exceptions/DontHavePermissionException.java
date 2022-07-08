package org.npopov.conference.exceptions;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class DontHavePermissionException extends RuntimeException {

    public DontHavePermissionException(Class clazz, String... searchParamsMap) {
        super(DontHavePermissionException.generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, (Object[]) searchParamsMap)));
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return "You don't have permission to access " + StringUtils.capitalize(entity);
    }

    private static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
}
