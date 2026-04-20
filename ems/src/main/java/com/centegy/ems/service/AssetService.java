package com.centegy.ems.service;

import com.centegy.ems.model.Asset;
import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.Role;
import com.centegy.ems.model.enums.AssetStatus;
import com.centegy.ems.repository.AssetHistoryRepository;
import com.centegy.ems.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final EmployeeService employeeService;
    private final AssetHistoryRepository assetHistoryRepository;

    public AssetService(AssetRepository assetRepository, EmployeeService employeeService, AssetHistoryRepository assetHistoryRepository) {
        this.assetRepository = assetRepository;
        this.employeeService = employeeService;
        this.assetHistoryRepository = assetHistoryRepository;
    }

    public Asset createAsset(Asset asset) {

        Employee realEmployee = employeeService.getEmployeeByID(asset.getEmployee().getId());
        asset.setEmployee(realEmployee);

        if (asset.getStatus() == null ) {
                asset.setStatus(AssetStatus.REQUESTED);
        }


        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + id));
    }

    public Asset updateAsset(Long id, Asset updatedData,Long performerID) {

        Asset asset = getAssetById(id);
        validateRoleStatus(performerID);

        Employee Realemployee = employeeService.getEmployeeByID(updatedData.getEmployee().getId());


        asset.setAssetName(updatedData.getAssetName());
        asset.setAssetType(updatedData.getAssetType());
        asset.setStatus(updatedData.getStatus());
        asset.setEmployee(Realemployee);


        return assetRepository.save(asset);
    }

    public void deleteAsset(Long id, Long performerID) {

        validateRoleStatus(performerID);

        if (!assetRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Asset not found with ID: " + id);
        }
    }

    public Asset returnAsset(Long id, Long employeeId) {
        Asset asset = getAssetById(id);

        employeeService.getEmployeeByID(employeeId);


        if (asset.getEmployee().getId() == null || !asset.getEmployee().getId().equals(employeeId)) {
                throw new RuntimeException("Unauthorized: You cannot return an asset that is not assigned to you.");
        }

        AssetHistory  assetHistory = assetHistoryRepository.findByAssetIdAndReturnDateIsNull(id)
                .orElse(null);

        if (assetHistory != null) {
            assetHistory.setReturnDate(LocalDate.now());
            assetHistoryRepository.save(assetHistory);
        }


        asset.setEmployee(null);
        asset.setStatus(AssetStatus.RETURNED);
        return assetRepository.save(asset);
    }


    public Asset approveAsset(Long id, Long approverId) {
        Asset asset = getAssetById(id);
        validateRoleStatus(approverId);

        Employee realEmployee = employeeService.getEmployeeByID(approverId);


        asset.setStatus(AssetStatus.APPROVED);
        asset.setApprovedBy(realEmployee);

        AssetHistory assetHistory = new AssetHistory();
        assetHistory.setAsset(asset);
        assetHistory.setEmployee(asset.getEmployee());
        assetHistory.setApprovedBy(realEmployee);
        assetHistory.setApprovedDate(LocalDate.now());
        assetHistoryRepository.save(assetHistory);

        return assetRepository.save(asset);
    }

    public List<AssetHistory> getAssetHistory(Long assetId) {
        getAssetById(assetId);
        return assetHistoryRepository.findByAssetId(assetId);
    }

    public void validateRoleStatus(Long id) {
        Employee employee = employeeService.getEmployeeByID(id);
        Role role = employee.getRole();

       if (role.getRoleLevel().getLevel() > 2){
           throw new RuntimeException("Access Denied: You do not have the required rank to perform this action.");
       }

    }
}