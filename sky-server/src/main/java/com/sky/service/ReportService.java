package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * ClassName: ReportService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/22 - 下午5:24
 * @Version: v1.0
 */
public interface ReportService {
    TurnoverReportVO amountReport(LocalDate begin, LocalDate end);

    UserReportVO userReport(LocalDate begin, LocalDate end);

    OrderReportVO orderReport(LocalDate begin, LocalDate end);

    SalesTop10ReportVO top10Report(LocalDate begin, LocalDate end);

    void export(HttpServletResponse response);
}
