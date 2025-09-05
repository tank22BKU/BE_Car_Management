package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRequest<T> {
    private int page;
    private int size;
    private T filters;
}