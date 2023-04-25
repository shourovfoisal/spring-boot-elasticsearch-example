package com.stech.dto;

import com.stech.model.Employee;

public class SearchResultDto {
    private Employee employee;
    private Double score;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
