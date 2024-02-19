package com.xxin.goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 13:23
 * @Description
 */
@Data
@TableName("tb_order")
public class Order {
    @TableId
    private String orderNo;
    private String phone;
    private BigDecimal totalPrice;
    private String customer;
    private String address;
    private String chinesePrice;
    private LocalDate orderDate;
    private LocalDateTime createTime;
}
