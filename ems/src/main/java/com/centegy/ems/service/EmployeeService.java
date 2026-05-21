package com.centegy.ems.service;

import com.centegy.ems.dto.request.EmployeeRequestDTO;
import com.centegy.ems.dto.response.EmployeeResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.exceptions.ResourceNotFoundException;
import com.centegy.ems.mapper.EmployeeMapper;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.LeaveBalance;
import com.centegy.ems.model.Role;
import com.centegy.ems.model.enums.LeaveType;
import com.centegy.ems.repository.EmployeeRepository;
import com.centegy.ems.repository.LeaveBalanceRepository;
import com.centegy.ems.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, LeaveBalanceRepository leaveBalanceRepository, EmployeeMapper employeeMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO) {

        Employee employee = employeeMapper.toEntity(employeeRequestDTO);

        Role role = roleRepository.findById(employeeRequestDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeeRequestDTO.getRoleId()));

        employee.setRole(role);

        if (employeeRequestDTO.getManagerId() != null) {
            Employee manager = employeeRepository.findById(employeeRequestDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + employeeRequestDTO.getManagerId()));

            employee.setManager(manager);
        }

        employee.setPassword(passwordEncoder.encode(employeeRequestDTO.getPassword()));

        Employee newEmployee = employeeRepository.save(employee);

        generateLeaves(newEmployee);

        return employeeMapper.toEmployeeResponseDTO(newEmployee);


    }

    public List<Employee> getAllEmployeesEntities(){
        return employeeRepository.findAll();
    }

    public PageResponseDTO<EmployeeResponseDTO> getAllEmployees(Pageable pageable) {

        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        List<EmployeeResponseDTO> content = employeePage.getContent()
                .stream()
                .map(employeeMapper::toEmployeeResponseDTO)
                .toList();

        PageResponseDTO<EmployeeResponseDTO> response = new PageResponseDTO<>();
        response.setContent(content);
        response.setPageNo(employeePage.getNumber());
        response.setPageSize(employeePage.getSize());
        response.setTotalElements(employeePage.getTotalElements());
        response.setTotalPages(employeePage.getTotalPages());
        response.setLast(employeePage.isLast());


        return response;
    }

//
//    public PageResponseDTO<EmployeeResponseDTO> getAllEmployees1(Pageable pageable) {
//
//
//        Page<EmployeeResponseDTO> employeeResponseDTOS = employeeRepository.findAllAsDTOs(pageable);
//
//        PageResponseDTO<EmployeeResponseDTO> response = new PageResponseDTO<>();
//
//        response.setContent(employeeResponseDTOS.getContent());
//        response.setPageNo(employeeResponseDTOS.getNumber());
//        response.setPageSize(employeeResponseDTOS.getSize());
//        response.setTotalElements(employeeResponseDTOS.getTotalElements());
//        response.setTotalPages(employeeResponseDTOS.getTotalPages());
//        response.setLast(employeeResponseDTOS.isLast());
//
//
//        return response;
//
//
//    }

    public Employee getEmployeeEntityByID(Long id){

        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

    }

    public EmployeeResponseDTO getEmployeeByID(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return employeeMapper.toEmployeeResponseDTO(employee);
    }

    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Employee not found with ID: " + id);
        }

        employeeRepository.deleteById(id);

    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO updatedData) {

        Employee employee = getEmployeeEntityByID(id);

        Role role = roleRepository.findById(updatedData.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + updatedData.getRoleId()));

        employee.setRole(role);

        if (updatedData.getManagerId() != null) {
            Employee manager = employeeRepository.findById(updatedData.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + updatedData.getManagerId()));

            employee.setManager(manager);
        }else {
            employee.setManager(null);
        }

        employee.setFirstName(updatedData.getFirstName());
        employee.setLastName(updatedData.getLastName());
        employee.setEmail(updatedData.getEmail());
        employee.setPhone(updatedData.getPhone());

        return employeeMapper.toEmployeeResponseDTO(employeeRepository.save(employee));
    }

    @Transactional
    public String fixLegacyLeaveBalances() {
        List<Employee> employees = getAllEmployeesEntities();

        for (Employee employee : employees) {
            if (leaveBalanceRepository.findByEmployeeIdAndLeaveType(employee.getId(), LeaveType.ANNUAL).isEmpty()) {
                generateLeaves(employee);
            }
        }
        return "All legacy employees have been fixed and given all 3 leave types";
    }

    public EmployeeResponseDTO addDocument(Long id,String documentType , String documentUrl) {
        Employee employee = getEmployeeEntityByID(id);

        try {
            new java.net.URL(documentUrl);
        } catch (java.net.MalformedURLException e) {
            throw new RuntimeException("Invalid document URL format! Please provide a valid web address.");
        }

        switch (documentType.toUpperCase()) {
            case "CNIC":
                employee.setCnicURL(documentUrl);
                break;
            case "CONTRACT":
                employee.setContractURL(documentUrl);
                break;
            case "CERTIFICATE":
                employee.setCertificateURL(documentUrl);
                break;
            default:
                throw new RuntimeException("Invalid document type! Use CNIC, CONTRACT, or CERTIFICATE.");

        }

        Employee newEmployee = employeeRepository.save(employee);

       return employeeMapper.toEmployeeResponseDTO(newEmployee);

    }


    private void generateLeaves(Employee employee){

        // Annual Leave
        createLeaveBalance(employee, LeaveType.ANNUAL, 20);

        // Sick Leave
        createLeaveBalance(employee, LeaveType.SICK, 10);

        // Casual Leave
        createLeaveBalance(employee, LeaveType.CASUAL, 5);


    }

    private void createLeaveBalance(Employee emp, LeaveType type, int days){

        LeaveBalance leaveBalance = new LeaveBalance();

        leaveBalance.setEmployee(emp);
        leaveBalance.setLeaveType(type);
        leaveBalance.setRemainingDays(days);

        leaveBalanceRepository.save(leaveBalance);

    }


}
