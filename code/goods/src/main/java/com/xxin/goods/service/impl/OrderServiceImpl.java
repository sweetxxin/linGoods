package com.xxin.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxin.goods.entity.Order;
import com.xxin.goods.mapper.OrderMapper;
import com.xxin.goods.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 20:44
 * @Description
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {
}
