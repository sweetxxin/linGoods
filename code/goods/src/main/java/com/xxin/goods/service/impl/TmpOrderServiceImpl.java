package com.xxin.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxin.goods.entity.TmpOrder;
import com.xxin.goods.mapper.TmpOrderMapper;
import com.xxin.goods.service.TmpOrderService;
import org.springframework.stereotype.Service;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 17:36
 * @Description
 */
@Service
public class TmpOrderServiceImpl extends ServiceImpl<TmpOrderMapper, TmpOrder> implements TmpOrderService {
}
