package me.huangduo.hms.mapper;

public interface BaseMapper<E, M> {
    E toEntity(M m);

    M toModel(E e);
}
