package com.wrx.vo;

import lombok.Data;

@Data
public class StudentVo {
    private String sno;
    private String idcard;
    private String username;
    private int pageIndex;
    private int pageSize;
}
