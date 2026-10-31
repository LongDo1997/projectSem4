package com.aptech.boutquetShop.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapperUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
    }
    public static <D, T> D map(final T entity, Class<D> outClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(entity, outClass);
    }
    private static <D, T> List<D> mapper(final Collection<T> entityList, Class<D> outCLass) {
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return ObjectMapperUtils.mapAll(entityList, outCLass);
    }
}
