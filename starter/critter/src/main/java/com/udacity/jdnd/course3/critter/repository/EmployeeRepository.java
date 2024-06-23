package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.skills IN (:skills) AND e.daysAvailable IN (:daysAvailable)")
    List<Employee> findAvailableEmployees(@Param("skills") Set<EmployeeSkill> skills, @Param("daysAvailable") Set<DayOfWeek> daysAvailable);

    List<Employee> findBySkillsInAndDaysAvailableIn(Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable);
}

