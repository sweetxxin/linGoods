package com.xxin.goods.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.github.pagehelper.PageHelper;
import com.xxin.goods.dto.ApiResponse;
import com.xxin.goods.entity.Goods;
import com.xxin.goods.entity.Order;
import com.xxin.goods.entity.OrderDetail;
import com.xxin.goods.entity.TmpOrder;
import com.xxin.goods.service.GoodsService;
import com.xxin.goods.service.OrderDetailService;
import com.xxin.goods.service.OrderService;
import com.xxin.goods.service.TmpOrderService;
import com.xxin.goods.util.MoneyConvertUtil;
import io.swagger.annotations.Api;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/order"})
@Api(
        tags = {"订单服务接口"}
)
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TmpOrderService tmpOrderService;
    @Autowired
    private OrderDetailService detailService;
    private String templatePath = "F:\\printService\\打印模板.xlsx";
    @Value("${excel.export.path}")
    private String exportPath;

    public OrderController() {
    }

    @GetMapping({"/generateOrderNo"})
    public ApiResponse<String> generateOrderNo() {
        PageHelper.startPage(1, 1);
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<Order> list =this.orderService.lambdaQuery().likeRight(Order::getOrderNo, format).orderByDesc(Order::getOrderNo).list();
        if (list.isEmpty()) {
            format = format + "00000";
        } else {
            format = list.get(0).getOrderNo();
        }
        Long aLong = Long.valueOf(format);
        return ApiResponse.success(aLong + 1L + "");
    }

    @PostMapping({"/save"})
    public ApiResponse<String> saveOrder(@RequestBody Order order) {
        this.orderService.saveOrUpdate(order);
        List<TmpOrder> list = this.tmpOrderService.list();
        List<String> collect = list.stream().map(TmpOrder::getGoodsId).collect(Collectors.toList());
        List<OrderDetail> details = new ArrayList();
        if (!collect.isEmpty()) {
            Collection<Goods> goods = this.goodsService.listByIds(collect);
            Map<String, Goods> goodsMap = goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity()));
            Iterator var7 = list.iterator();

            while(var7.hasNext()) {
                TmpOrder tmpOrder = (TmpOrder)var7.next();
                Goods g = goodsMap.get(tmpOrder.getGoodsId());
                if (g != null) {
                    OrderDetail detail = new OrderDetail();
                    detail.setAmount(tmpOrder.getAmount());
                    detail.setGoodsId(g.getGoodsId());
                    detail.setGoodsName(g.getGoodsName());
                    detail.setUnit(g.getUnit());
                    detail.setOrderNo(order.getOrderNo());
                    detail.setPrice(g.getSellingPrice());
                    detail.setDetailId(order.getOrderNo() + "-" + g.getGoodsId());
                    details.add(detail);
                }
            }

            if (!details.isEmpty()) {
                this.detailService.saveOrUpdateBatch(details);
            }
        }

        return ApiResponse.success(null);
    }

    @PostMapping({"/print"})
    public ApiResponse<String> printOrder(@RequestBody Order order) {
        String file = null;
        List<TmpOrder> list = this.tmpOrderService.lambdaQuery().orderByAsc(TmpOrder::getNum).list();
        List<String> collect = list.stream().map(TmpOrder::getGoodsId).collect(Collectors.toList());
        List<OrderDetail> details = new ArrayList();
        BigDecimal totalPrice = new BigDecimal(0);
        int totalAmount = 0;
        if (!collect.isEmpty()) {
            Collection<Goods> goods = this.goodsService.listByIds(collect);
            Map<String, Goods> goodsMap = goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity()));
            int i = 0;
            Iterator var10 = list.iterator();

            while(var10.hasNext()) {
                TmpOrder tmpOrder = (TmpOrder)var10.next();
                Goods g = goodsMap.get(tmpOrder.getGoodsId());
                if (g != null) {
                    OrderDetail detail = new OrderDetail();
                    ++i;
                    detail.setNum(i);
                    detail.setAmount(tmpOrder.getAmount());
                    detail.setGoodsId(g.getGoodsId());
                    detail.setGoodsName(g.getGoodsName());
                    detail.setUnit(g.getUnit());
                    detail.setOrderNo(order.getOrderNo());
                    detail.setPrice(g.getSellingPrice());
                    detail.setDetailId(order.getOrderNo() + "-" + g.getGoodsId());
                    detail.setTotalPrice(detail.getPrice().multiply(BigDecimal.valueOf((long)detail.getAmount())));
                    totalPrice = totalPrice.add(detail.getTotalPrice());
                    totalAmount += detail.getAmount();
                    details.add(detail);
                }
            }
            order.setTotalPrice(totalPrice);
            file = this.exportData(order, details, totalPrice, totalAmount);
            this.orderService.saveOrUpdate(order);
            if (!details.isEmpty()) {
                this.detailService.saveOrUpdateBatch(details);
            }

            this.tmpOrderService.remove(null);
        }

        return ApiResponse.success(file);
    }
    @GetMapping("/open/excel")
    public void openExcel(String path){
        try {
            Runtime.getRuntime().exec("cmd  /c  start "+path);
        }catch (Exception e){
            log.error("自动打开excel异常",e);
        }
    }

    private String exportData(Order order, List<OrderDetail> details, BigDecimal totalPrice, int totalAmount) {
        LocalDate now = LocalDate.now();
        File file = new File(this.exportPath+"/"+now.getYear()+"-"+now.getMonthValue());
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception var12) {
                log.error("error", var12);
            }
        }

        String resultFile = file.getAbsolutePath() + "/订单" + order.getOrderNo() + ".xlsx";

        try {
            ExcelWriter excelWriter = EasyExcel.write(resultFile).withTemplate(this.templatePath).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            Map<String, Object> map = new HashMap();
            map.put("customer", order.getCustomer());
            map.put("orderNo", order.getOrderNo());
            map.put("address", order.getAddress());
            map.put("phone", order.getPhone());
            map.put("totalPrice", totalPrice);
            map.put("totalAmount", totalAmount);
            map.put("year", order.getOrderDate().getYear());
            map.put("month", order.getOrderDate().getMonthValue());
            map.put("day", order.getOrderDate().getDayOfMonth());
            map.put("chinesePrice", MoneyConvertUtil.toChinese(totalPrice.toString()));
            excelWriter.fill(map, writeSheet);
            excelWriter.fill(new FillWrapper("goods", details), fillConfig, writeSheet);
            excelWriter.finish();
        } catch (Exception var11) {
            log.error("异常:", var11);
        }
        return resultFile;
    }
}
