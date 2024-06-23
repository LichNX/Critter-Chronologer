package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface EmployeeService {

    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO getEmployee(long employeeId);

    void setAvailability(Set<DayOfWeek> daysAvailable,long employeeId);

    List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeDTO);
}
