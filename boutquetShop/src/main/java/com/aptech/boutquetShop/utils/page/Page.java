package com.aptech.boutquetShop.utils.page;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private List<T> dataRow;

    private int currentPage;
    private int pageSize;

    private int totalPage;
    private int totalRow;

    public Page() {
    }

    public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Page<T> compute(int totalRow){
        this.totalRow = totalRow;
        this.totalPage = (int) Math.ceil((double) totalRow/pageSize);
        return this;
    }

    public Page<T> withResult(List<T> dataRow){
        this.dataRow = dataRow;
        return this;
    }

}