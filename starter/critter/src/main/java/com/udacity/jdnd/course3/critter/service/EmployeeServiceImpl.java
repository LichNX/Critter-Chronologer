package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = revertEmployeeDTOToEmployee(employeeDTO);
        Employee employeeSave = employeeRepository.save(employee);
        return convertEmployeeToEmployeeDTO(employeeSave);
    }

    @Override
    public EmployeeDTO getEmployee(long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        return employeeOptional.map(this::convertEmployeeToEmployeeDTO).orElse(null);
    }

    @Override
    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        }
    }

    @Override
    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO) {
        Set<EmployeeSkill> skills = employeeRequestDTO.getSkills();

        Set<DayOfWeek> daysAvailable = new HashSet<>();
        daysAvailable.add(employeeRequestDTO.getDate().getDayOfWeek());

        //List<Employee> employees = employeeRepository.findAvailableEmployees(skills, daysAvailable);

        List<Employee> employees = employeeRepository.findBySkillsInAndDaysAvailableIn(skills, daysAvailable);

        if(employees != null) {
            List<Employee> EmployeeRquired = employees.stream()
                                                    .filter(employee -> employee.getSkills().containsAll(skills))
                                                    .collect(Collectors.toList());


            return EmployeeRquired.stream()
                    .map(this::convertEmployeeToEmployeeDTO)
                    .collect(Collectors.toList());
        }

        return null;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee revertEmployeeDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

}
