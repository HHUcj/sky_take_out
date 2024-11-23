package com.sky.mapper;

import com.sky.annotion.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    Employee getByUsername(String username);

    @AutoFill(value = OperationType.INSERT)
    void addEmployee(Employee employee);

    List<Employee> page(@Param("name")String name,
                        @Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize);

    Integer count(@Param("name")String name,
                  @Param("pageNo")Integer pageNo,
                  @Param("pageSize")Integer pageSize);

    void updateEmpStatusById(@Param("id") Long id, @Param("abs") int abs);

    Employee getEmpById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void updateEmp(Employee employee);
}
