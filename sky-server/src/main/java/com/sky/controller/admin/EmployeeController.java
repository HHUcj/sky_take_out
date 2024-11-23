package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.UserNameExistException;
import com.sky.properties.JwtProperties;

import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        String userName = employeeDTO.getUsername();
        Employee employee = employeeService.getByUsername(userName);
        if (employee != null) {
            throw new UserNameExistException(MessageConstant.USERNAME_EXIST);
        }

        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        String name = employeePageQueryDTO.getName();
        Integer page = employeePageQueryDTO.getPage();
        Integer pageSize = employeePageQueryDTO.getPageSize();
        Integer pageNo = (page-1)*pageSize;
        List<Employee> record = employeeService.page(name, pageNo, pageSize);
        Integer total = employeeService.count(name, pageNo, pageSize);
        return Result.success(new PageResult(total == null ? 0 : total, record));
    }

    @PostMapping("/status/{status}")
    public Result startOrstop(@PathVariable Integer status, Long id) {
        employeeService.updateEmpStatusById(id, status);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getEmpById(@PathVariable Long id) {
        Employee employee = employeeService.getEmpById(id);
        return Result.success(employee);
    }

    @PutMapping
    public Result updateEmp(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmp(employeeDTO);
        return Result.success();
    }
}
