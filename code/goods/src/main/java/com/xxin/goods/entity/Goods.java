package com.xxin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 13:28
 * @Description
 */
@Data
@TableName("tb_goods")
public class Goods {
    @TableId(type = IdType.UUID)
    private String goodsId;
    private String goodsName;
    private String unit;
    private int hot;
    private String goodsCode;
    private BigDecimal sellingPrice;
    private BigDecimal buyingPrice;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @TableField(exist = false)
    private int amount=1;
}
