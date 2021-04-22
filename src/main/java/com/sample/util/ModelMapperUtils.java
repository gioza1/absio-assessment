package com.sample.util;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperUtils {
    public static <S, D> D map(S source, Class<D> destinationClass, ModelMapper mapper) {
        return mapper.map(source, destinationClass);
    }

    public static <S, D> List<D> mapAll(Collection<S> sourceList, Class<D> destinationClass, ModelMapper mapper) {
        return sourceList.stream()
                         .map(source -> map(source, destinationClass, mapper))
                         .collect(Collectors.toList());
    }
}
