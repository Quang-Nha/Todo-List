package com.nhale.todoList.dataModel;

import java.time.LocalDate;

public class TodoItem {
    private String shortDescription;
    private String detail;
    private LocalDate kpi;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDate getKpi() {
        return kpi;
    }

    public void setKpi(LocalDate kpi) {
        this.kpi = kpi;
    }

    public TodoItem(String shortDescription, String detail, LocalDate kpi) {
        this.shortDescription = shortDescription;
        this.detail = detail;
        this.kpi = kpi;
    }

//    @Override
//    public String toString() {
//        return shortDescription;
//    }
}
