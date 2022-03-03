package com.wendo.bank.converter.dtoconverter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Converter<D, E> {

    E convertToEntity(D d);

    D convertToDto(E e);

    default Set<E> convertToEntitySet(List<D> d) {
        return d.stream().map(this::convertToEntity).collect(Collectors.toSet());
    }
    default List<D> convertToDtoList(Set<E> e) {
        return e.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
