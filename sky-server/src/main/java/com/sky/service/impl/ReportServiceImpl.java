package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ClassName: ReportServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/22 - 下午5:25
 * @Version: v1.0
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkSpaceService workSpaceService;

    @Override
    public TurnoverReportVO amountReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getLocalDates(begin, end);

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map<String, Object> map = getMap(date);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.getAmount(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();

        return turnoverReportVO;

    }

    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getLocalDates(begin, end);

        List<Long> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map<String, Object> map = getMap(date);
            Long totalUser = userMapper.getUserCnt(map);
            totalUser = totalUser == null ? 0 : totalUser;
            totalUserList.add(totalUser);
        }

        List<Long> newUserList = new ArrayList<>();
        newUserList.add(totalUserList.get(0));
        for (int i = 1; i < totalUserList.size(); i++) {
            newUserList.add(totalUserList.get(i) - totalUserList.get(i-1));
        }

        UserReportVO userReportVO = UserReportVO.builder()
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .dateList(StringUtils.join(dateList, ","))
                .build();

        return userReportVO;
    }

    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getLocalDates(begin, end);

        List<Integer> orderCntList = new ArrayList<>();
        List<Integer> validOrderCntList = new ArrayList<>();
        for (LocalDate date : dateList) {
            Map<String, Object> map = getMap(date);
            map.put("status", null);
            Integer orderCount = orderMapper.getOrderCnt(map);
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.getOrderCnt(map);

            orderCount = orderCount == null ? 0 : orderCount;
            validOrderCount = validOrderCount == null ? 0 : validOrderCount;

            orderCntList.add(orderCount);
            validOrderCntList.add(validOrderCount);
        }

        Integer totalOrderCount = 0;
        for (Integer orderCnt : orderCntList) {
            totalOrderCount += orderCnt;
        }

        Integer vaildOrderCount = 0;
        for (Integer validOrderCnt : orderCntList) {
            vaildOrderCount += validOrderCnt;
        }

        Double orderCompletionRate = (double) vaildOrderCount / totalOrderCount * 1.0;

        OrderReportVO orderReportVO = OrderReportVO.builder().orderCompletionRate(orderCompletionRate)
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCntList, ","))
                .validOrderCount(vaildOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCntList, ","))
                .totalOrderCount(totalOrderCount)
                .build();
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO top10Report(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> list = orderMapper.getTop10Report(beginTime, endTime);

        List<String> names = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();

        return salesTop10ReportVO;
    }

    @Override
    public void export(HttpServletResponse response) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workSpaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<LocalDate> getLocalDates(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }

    private Map<String, Object> getMap(LocalDate date) {
        LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
        Map<String, Object> map = new HashMap<>();
        map.put("begin", beginTime);
        map.put("end", endTime);
        return map;
    }
}