package dania.app.web.utils;

import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Cleaner {

    Predicate<Integer> greaterOrEqualsToZero = number -> number >= 0;
    Predicate<Integer> greaterThenZero = number -> number > 0;

    static <T> String getSortFrom(String givenSortBy, Class<T> tClass) {
        return Optional.ofNullable(givenSortBy)
                .filter(StringUtils::hasText)
                .filter(Stream.of(tClass.getDeclaredFields())
                        .map(Field::getName)
                        .collect(Collectors.toSet())::contains)
                .orElseThrow(() -> new RuntimeException("The sortBy parameter is incorrect."));
    }

    static <T> PageParams processGivenPageParams(PageParams params, Class<T> tClass){
        final Integer page = Optional.ofNullable(params.getPage()).filter(greaterOrEqualsToZero).orElse(0);
        final Integer size = Optional.ofNullable(params.getSize()).filter(greaterThenZero).orElse(Constants.MAX_ITEMS_PER_PAGE);
        final Sort.Direction direction = Optional.ofNullable(params.getDirection()).orElse(Sort.Direction.ASC);
        final String sortBy = getSortFrom(params.getSortBy(), tClass);
        return new PageParams(page,size,sortBy, direction);
    }

    static <T> Map<String, List<T>> getSortedMap(Map<String, List<T>> unsortedMap){
        return unsortedMap.entrySet().stream().
                sorted(Map.Entry.comparingByKey()).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue,newValue)->oldValue, LinkedHashMap::new));
    }
}
