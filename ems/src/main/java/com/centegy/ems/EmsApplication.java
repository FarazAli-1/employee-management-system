package com.centegy.ems;

import com.centegy.ems.model.*;
import com.centegy.ems.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class EmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(RoleService roleService,
								   EmployeeService employeeService,
								   AssetService assetService,
								   LeaveService leaveService) {
		return args -> {
			System.out.println("--- Starting Data Seeding ---");

			// 1. Create Roles
			Role adminRole = new Role();
			adminRole.setTitle("IT Admin");
			adminRole.setDepartmentName("IT");
			roleService.createRole(adminRole); // ID 1

			Role devRole = new Role();
			devRole.setTitle("Software Engineer");
			devRole.setDepartmentName("Engineering");
			roleService.createRole(devRole); // ID 2

			// 2. Create Employees
			Employee adminEmp = new Employee();
			adminEmp.setFirstName("Bruce");
			adminEmp.setLastName("Wayne");
			adminEmp.setEmail("admin@centegy.com");
			adminEmp.setEmploymentType("FULL_TIME");
			adminEmp.setRoleId(1); // Assign to IT Admin
			employeeService.addEmployee(adminEmp); // ID 1

			Employee stdEmp = new Employee();
			stdEmp.setFirstName("Clark");
			stdEmp.setLastName("Kent");
			stdEmp.setEmail("clark@centegy.com");
			stdEmp.setEmploymentType("FULL_TIME");
			stdEmp.setRoleId(2); // Assign to Software Engineer
			stdEmp.setManagerId(1); // Bruce is Clark's manager
			employeeService.addEmployee(stdEmp); // ID 2

			// 3. Create an Asset and Approve it!
			Asset laptop = new Asset();
			laptop.setAssetName("MacBook Pro M3");
			laptop.setAssetType("Laptop");
			laptop.setEmployeeId(2); // Giving it to Clark
			assetService.createAsset(laptop); // ID 1, Status will be REQUESTED

			// Admin Bruce (ID 1) approves the laptop for Clark
			assetService.approveAsset(1, 1);

			// 4. Create a Leave Request for Clark
			Leave leave = new Leave();
			leave.setEmployeeId(2);
			leave.setLeaveType("ANNUAL");
			leave.setStartDate(LocalDate.now().plusDays(5));
			leave.setEndDate(LocalDate.now().plusDays(10));
			leaveService.applyForLeave(leave); // ID 1, Status will be PENDING

			System.out.println("--- Data Seeding Complete! System is ready to test. ---");
		};
	}
}