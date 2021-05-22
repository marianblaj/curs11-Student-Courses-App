package ro.fasttrackit.curs11.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<D, E> {
    D toDto(E source);

    E toEntity(D source);

    default List<D> toDto(Collection<E> source) {
        return source.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default List<E> toEntity(Collection<D> source) {
        return source.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
