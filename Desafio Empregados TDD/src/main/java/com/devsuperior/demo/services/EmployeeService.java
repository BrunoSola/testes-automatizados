package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EmployeeDTO;
import com.devsuperior.demo.entities.Department;
import com.devsuperior.demo.entities.Employee;
import com.devsuperior.demo.repositories.DepartmentRepository;
import com.devsuperior.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
public class EmployeeService implements Serializable {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private void copyDTOToEntity(EmployeeDTO employeeDTO, Employee entity) {
        entity.setName(employeeDTO.getName());
        entity.setEmail(employeeDTO.getEmail());
        Department department = departmentRepository.getReferenceById(employeeDTO.getDepartmentId());
        entity.setDepartment(department);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAllPaged(Pageable pageable) {
        Page<Employee> employeeResult = employeeRepository.findAllByOrderByNameAsc(pageable);
        return employeeResult.map(EmployeeDTO::new);
    }

    @Transactional
    public EmployeeDTO insert(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        copyDTOToEntity(employeeDTO, employee);
        employee = employeeRepository.save(employee);
        return new EmployeeDTO(employee);
    }
}
