package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.AbstractMap;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractResponse {
    private String status = "SUCCESS";
    private String message = "OK";
    private int message_code = 200;
    private String message_type;
    private Object data;
    private Paging meta = null;

    public AbstractResponse(AbstractMap.SimpleEntry<Object, Paging> dataMeta) {
        this.data = dataMeta.getKey();
        this.meta = dataMeta.getValue();
    }

    public AbstractResponse(Object data) {
        this.data = data;
    }

    public AbstractResponse(Object data, Paging paging) {
        this.data = data;
        this.meta = paging;
    }

    public AbstractResponse(String message) {
        this.message = message;
    }

    public AbstractResponse(String message, int message_code) {
        this.message = message;
        this.message_code = message_code;
    }

    public AbstractResponse(String status, String message, int message_code) {
        this.status = status;
        this.message = message;
        this.message_code = message_code;
    }

    public AbstractResponse(String status, String message, int message_code, Object data) {
        this.status = status;
        this.message = message;
        this.message_code = message_code;
        this.data = data;
    }

    public AbstractResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
