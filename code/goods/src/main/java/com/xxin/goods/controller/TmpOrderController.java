package com.xxin.goods.controller;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.github.pagehelper.PageInfo;
import com.xxin.goods.dto.ApiResponse;
import com.xxin.goods.entity.Goods;
import com.xxin.goods.entity.TmpOrder;
import com.xxin.goods.service.GoodsService;
import com.xxin.goods.service.TmpOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 17:34
 * @Description
 */
@RestController
@RequestMapping("tmp")
public class TmpOrderController {
    @Autowired
    private TmpOrderService tmpService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping({"/get"})
    public ApiResponse<PageInfo<Goods>> getTmp() {
        List<TmpOrder> tmps = ((LambdaQueryChainWrapper)this.tmpService.lambdaQuery().orderByAsc(TmpOrder::getNum)).list();
        PageInfo<TmpOrder> goodsPageInfo = new PageInfo(tmps);
        if (!tmps.isEmpty()) {
            List<String> collect = (List)tmps.stream().map(TmpOrder::getGoodsId).collect(Collectors.toList());
            Collection<Goods> goods = this.goodsService.listByIds(collect);
            if (!goods.isEmpty()) {
                Map<String, Goods> goodsMap = (Map)goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity()));
                Iterator var6 = tmps.iterator();

                while(var6.hasNext()) {
                    TmpOrder tmp = (TmpOrder)var6.next();
                    Goods g = (Goods)goodsMap.get(tmp.getGoodsId());
                    if (g != null) {
                        tmp.setGoodsName(g.getGoodsName());
                        tmp.setSellingPrice(g.getSellingPrice());
                        tmp.setUnit(g.getUnit());
                        tmp.setGoodsCode(g.getGoodsCode());
                    }
                }
            }
        }
        return ApiResponse.success(goodsPageInfo);
    }
    @PostMapping("/save")
    public ApiResponse<String> addTmp(@RequestBody TmpOrder tmp){
        this.tmpService.saveOrUpdate(tmp);
        return ApiResponse.success(null);
    }
    @PostMapping("/remove")
    public ApiResponse<String> removeTmp(String id){
        this.tmpService.removeById(id);
        return ApiResponse.success(null);
    }
    @PostMapping("/clear")
    public ApiResponse<String> clearTmp(){
        this.tmpService.remove(null);
        return ApiResponse.success(null);
    }
}
