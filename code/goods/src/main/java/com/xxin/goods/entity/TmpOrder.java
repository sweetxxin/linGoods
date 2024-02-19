package com.xxin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 13:30
 * @Description
 */
@Data
@TableName("tb_order_tmp")
public class TmpOrder {
    @TableId
    private String goodsId;
    private int amount;
    private int num;
    @TableField(exist = false)
    private String goodsName;
    @TableField(exist = false)
    private String goodsCode;
    @TableField(exist = false)
    private String unit;
    @TableField(exist = false)
    private BigDecimal sellingPrice;

}
