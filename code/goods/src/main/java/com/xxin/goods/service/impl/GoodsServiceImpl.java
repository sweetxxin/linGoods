package com.xxin.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxin.goods.entity.Goods;
import com.xxin.goods.mapper.GoodsMapper;
import com.xxin.goods.service.GoodsService;
import org.springframework.stereotype.Service;


/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 13:37
 * @Description
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}
