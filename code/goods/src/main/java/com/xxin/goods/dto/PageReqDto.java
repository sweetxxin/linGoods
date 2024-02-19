package com.xxin.goods.dto;

import lombok.Data;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 14:25
 * @Description
 */
@Data
public class PageReqDto {
    private int pageNo;
    private int pageSize;
    private String keyword;
    private String goodsCode;
}
