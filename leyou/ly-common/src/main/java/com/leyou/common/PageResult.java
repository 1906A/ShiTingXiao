package com.leyou.common;

import java.util.List;

public class PageResult<T> {
    private Long total; //分页总条数
    private List<T> items; //数据

    private Integer totalPage; // 总页数



    public PageResult() {
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", items=" + items +
                '}';
    }

    public PageResult(Long total, List<T> items, Integer totalPage) {
        this.total = total;
        this.items = items;
        this.totalPage = totalPage;
    }

    public PageResult(Long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
