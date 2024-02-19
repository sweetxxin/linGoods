package com.xxin.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 13:27
 * @Description
 */
@Data
@TableName("tb_order_detail")
public class OrderDetail {
    @TableId
    private String detailId;
    private String orderNo;
    private String goodsId;
    private String goodsName;
    private String unit;
    private BigDecimal price;
    private int amount;
    @TableField(exist = false)
    private BigDecimal totalPrice;
    @TableField(exist = false)
    private int num;
}
