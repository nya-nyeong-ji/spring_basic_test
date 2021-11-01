package com.example.spring_basic_test.domain.model;

import org.springframework.data.domain.Sort;

public final class PageRequest {
    private int page;
    private int size;
    private Sort.Direction direction;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int default_size = 10;
        int max_size = 50;
        this.size = size < max_size ? default_size : size;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createDate");
    }
}
