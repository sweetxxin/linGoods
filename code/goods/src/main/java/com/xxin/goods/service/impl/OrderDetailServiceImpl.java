package com.xxin.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxin.goods.entity.OrderDetail;
import com.xxin.goods.mapper.OrderDetailMapper;
import com.xxin.goods.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 21:05
 * @Description
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
