package com.logging.platform.pagination;

import java.util.List;

public class Pagination <T>{
    private int pageIndex;
    private int pageSize;
    private int pageCount;
    private List<T> items;

    public int getPageIndex() {
        return pageIndex;
    }

    public Pagination<T> setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Pagination<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Pagination<T> setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public Pagination<T> setItems(List<T> items) {
        this.items = items;
        return this;
    }
}
