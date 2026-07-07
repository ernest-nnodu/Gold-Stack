package com.jackalcode.gold_stack.service;

public interface IngestionService<T> {

    void ingest(T data);

    void reIngest(T data);

    void delete(Long id);
}
