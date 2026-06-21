package com.jackalcode.gold_stack.service;

public interface IngestionService<T> {

    void ingest(T data);
}
