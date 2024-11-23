package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    Employee getByUsername(String userName);

    List<Employee> page(String name, Integer pageNo, Integer pageSize);

    Integer count(String name, Integer pageNo, Integer pageSize);

    void updateEmpStatusById(Long id, int abs);

    Employee getEmpById(Long id);

    void updateEmp(EmployeeDTO employeeDTO);
}
