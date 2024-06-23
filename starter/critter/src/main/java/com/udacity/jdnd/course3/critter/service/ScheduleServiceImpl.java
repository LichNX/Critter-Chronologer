package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {

        List<Employee>  employees = employeeRepository.findAllById(scheduleDTO.getEmployeeIds());

        List<Pet> pets = petRepository.findAllById(scheduleDTO.getPetIds());

        Schedule schedule = revertScheduleDTOToSchedule(scheduleDTO, employees, pets);

        Schedule scheduleSave = scheduleRepository.save(schedule);

        return convertScheduleToScheduleDTO(scheduleSave);
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {

        List<Schedule> schedules = scheduleRepository.findAll();

        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getScheduleForPet(long petId) {
        Optional<Pet> petOptional = petRepository.findById(petId);
        if(!petOptional.isPresent()){
            return null;
        }

        List<Pet> pets = new ArrayList<>();
        pets.add(petOptional.get());

        List<Schedule> schedules = scheduleRepository.findByPetsIn(pets);

        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if(!employeeOptional.isPresent()){
            return null;
        }

        List<Employee> employees = new ArrayList<>();
        employees.add(employeeOptional.get());

        List<Schedule> schedules = scheduleRepository.findByEmployeesIn(employees);

        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        List<Pet> pets = petRepository.findByOwnerId(customerId);
        if(pets == null){
            return null;
        }

        List<Schedule> schedules = scheduleRepository.findByPetsIn(pets);

        return schedules.stream()
                .map(this::convertScheduleToScheduleDTO)
                .collect(Collectors.toList());
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        BeanUtils.copyProperties(schedule, scheduleDTO, "employees", "pets");

        if(schedule.getEmployees() != null) {
            List<Long> employeeIds = schedule.getEmployees().stream()
                                                            .map(Employee::getId)
                                                            .collect(Collectors.toList());
            scheduleDTO.setEmployeeIds(employeeIds);
        }

        if(schedule.getPets() != null) {
            List<Long> petIds = schedule.getPets().stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());

            scheduleDTO.setPetIds(petIds);
        }

        return scheduleDTO;
    }

    private Schedule revertScheduleDTOToSchedule(ScheduleDTO scheduleDTO, List<Employee> employees, List<Pet> pets) {
        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule, "employeeIds", "petIds");

        if(employees != null) {
            schedule.setEmployees(employees);
        }

        if(pets != null) {
            schedule.setPets(pets);
        }

        return schedule;
    }
}
