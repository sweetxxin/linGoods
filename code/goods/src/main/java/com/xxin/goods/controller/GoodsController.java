package com.xxin.goods.controller;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.xxin.goods.dto.ApiResponse;
import com.xxin.goods.dto.PageReqDto;
import com.xxin.goods.entity.Goods;
import com.xxin.goods.service.GoodsService;
import com.xxin.goods.service.TmpOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xxin
 * @Created
 * @Date 2023/1/22 13:42
 * @Description
 */
@Api(tags = "货物管理接口")
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TmpOrderService tmpOrderService;


    @PostMapping("/save")
    public ApiResponse<String> save(@RequestBody Goods goods){
        if (goods.getCreateTime() ==null){
            goods.setCreateTime(LocalDateTime.now());
        }
        goods.setUpdateTime(LocalDateTime.now());
        boolean b = goodsService.saveOrUpdate(goods);
        if (b){
            return ApiResponse.success(null);
        }
        return ApiResponse.fail("保存失败");
    }
    @PostMapping("/delete")
    public ApiResponse delete(String id){
        goodsService.removeById(id);
        tmpOrderService.removeById(id);
        return ApiResponse.success(null);
    }
    @PostMapping("/search")
    public ApiResponse<PageInfo<Goods>> search(@RequestBody PageReqDto reqDto){
        PageHelper.startPage(reqDto.getPageNo(),reqDto.getPageSize());
        LambdaQueryChainWrapper<Goods> query = goodsService.lambdaQuery();
        if (StringUtil.isNotEmpty(reqDto.getKeyword())){
           query.like(Goods::getGoodsName,reqDto.getKeyword());
        }
        if (StringUtils.isNotBlank(reqDto.getGoodsCode())){
            query.like(Goods::getGoodsCode,reqDto.getGoodsCode());
        }
        query.orderByDesc(Goods::getUpdateTime,Goods::getHot);
        List<Goods> list = query.list();
        return ApiResponse.success(new PageInfo<Goods>(list));
    }
    @GetMapping("/get/hot")
    public ApiResponse<PageInfo<Goods>> getHot(){
        PageHelper.startPage(1,1000);
        LambdaQueryChainWrapper<Goods> query = goodsService.lambdaQuery();
        query.orderByDesc(Goods::getHot);
        List<Goods> list = query.list();
        return ApiResponse.success(new PageInfo<Goods>(list));
    }
    @PostMapping("/import/file")
    public ApiResponse<String> importFile(MultipartFile file){
        try {
            dealExcelData(file.getInputStream());
        }catch (Exception e){
            log.error("导入异常:");
            return ApiResponse.fail("上传异常"+e);
        }
        return ApiResponse.success("上传成功");
    }
    @PostMapping("/import/path")
    public void importData(String path){
        try {
            if (path==null){
                path = "C:\\Users\\cxxin\\Desktop\\data.xlsx";
            }
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            dealExcelData(inputStream);
        }catch (Exception e){
            log.error("异常",e);
        }
    }
    private int dealExcelData(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<Goods> goods = new ArrayList<>();
        for(int i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum(); ++i) {
            Row row = sheet.getRow(i);
            Goods g = new Goods();
            if (row.getCell(0)!=null){
                if (row.getCell(0).getCellType().equals(CellType.NUMERIC))
                {
                    g.setGoodsCode((int)row.getCell(0).getNumericCellValue()+"");
                }else{
                    g.setGoodsCode(row.getCell(0).getStringCellValue());
                }
            }
            if (row.getCell(1)!=null && StringUtils.isNotBlank(row.getCell(1).getStringCellValue())){
                g.setGoodsName(row.getCell(1).getStringCellValue());
            }else{
                continue;
            }
            if ( row.getCell(2)!=null){
                g.setUnit( row.getCell(2).getStringCellValue());
            }
            Double sellingPrice = row.getCell(3).getNumericCellValue();
            Cell cell4 = row.getCell(4);
            if (cell4 != null) {
                Double buyingPrice = row.getCell(4).getNumericCellValue();
                g.setBuyingPrice(new BigDecimal(buyingPrice));
            }

            Cell cell5 = row.getCell(5);
            if (cell5 != null) {
                int sort = (int)row.getCell(5).getNumericCellValue();
                g.setHot(sort);
            }
            if (row.getCell(6)!=null){
                g.setDescription(row.getCell(6).getStringCellValue());
            }
            g.setSellingPrice(new BigDecimal(sellingPrice));
            g.setCreateTime(LocalDateTime.now());
            g.setUpdateTime(LocalDateTime.now());
            goods.add(g);
        }
        int exist = 0;
        if (!goods.isEmpty()) {
            List<String> collect = goods.stream().map(Goods::getGoodsName).collect(Collectors.toList());
            List<Goods> list = this.goodsService.lambdaQuery().in(Goods::getGoodsName, collect).list();
            Map<String, Goods> goodsMap = list.stream().collect(Collectors.toMap(Goods::getGoodsName, Function.identity()));
            Iterator var20 = goods.iterator();

            while(var20.hasNext()) {
                Goods good = (Goods)var20.next();
                Goods g = goodsMap.get(good.getGoodsName());
                if (g != null) {
                    good.setGoodsId(g.getGoodsId());
                    good.setCreateTime(g.getCreateTime());
                    exist++;
                }
            }
            this.goodsService.saveOrUpdateBatch(goods);
        }
        log.info("导入总数：{}，新增:{},更新:{}",goods.size(),goods.size()-exist,exist);
        return goods.size();
    }
}
