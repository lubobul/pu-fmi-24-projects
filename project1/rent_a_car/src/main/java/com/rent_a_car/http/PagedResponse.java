package com.rent_a_car.http;

import java.util.ArrayList;
import java.util.List;

public class PagedResponse<T> {
    private int totalItems;
    private int page;
    private int pageSize;

    public PagedResponse(List<T> values){
        this.values = values;
    }
    private List<T> values;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public static PagedResponse getEmpty(int page, int pageSize){
        var pagedResponse = new PagedResponse<>(new ArrayList<>());
        pagedResponse.setPageSize(page);
        pagedResponse.setPage(pageSize);
        pagedResponse.setTotalItems(0);
        return pagedResponse;
    }
}
