package com.centegy.ems.service;

import com.centegy.ems.model.Asset;
import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.model.Employee;
import com.centegy.ems.repository.AssetHistoryRepository;
import com.centegy.ems.repository.AssetRepository;
import com.centegy.ems.repository.EmployeeRepository;
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


        if (asset.getEmployeeId() != null) {
            employeeService.getEmployeeByID(asset.getEmployeeId());
        }

        if (asset.getStatus() == null || asset.getStatus().isBlank() ) {
                asset.setStatus("REQUESTED");
        }


        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Asset getAssetById(Integer id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + id));
    }

    public Asset updateAsset(Integer id, Asset updatedData) {

        Asset asset = getAssetById(id);

        if (updatedData.getEmployeeId() != null) {
            employeeService.getEmployeeByID(updatedData.getEmployeeId());
        }

        asset.setAssetName(updatedData.getAssetName());
        asset.setAssetType(updatedData.getAssetType());
        asset.setStatus(updatedData.getStatus());
        asset.setEmployeeId(updatedData.getEmployeeId());

        return assetRepository.save(asset);
    }

    public void deleteAsset(Integer id) {
        boolean isDeleted = assetRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Cannot delete. Asset not found with ID: " + id);
        }
    }

    public Asset returnAsset(Integer id, Integer employeeId) {
        Asset asset = getAssetById(id);

        employeeService.getEmployeeByID(employeeId);


        if (asset.getEmployeeId() == null || !asset.getEmployeeId().equals(employeeId)) {
                throw new RuntimeException("Unauthorized: You cannot return an asset that is not assigned to you.");
        }

        AssetHistory  assetHistory = assetHistoryRepository.findAssetHistory(id);

        if (assetHistory != null) {
            assetHistory.setReturnDate(LocalDate.now());
            assetHistoryRepository.save(assetHistory);
        }


        asset.setEmployeeId(null);
        asset.setStatus("RETURNED");
        return assetRepository.save(asset);
    }


    public Asset approveAsset(Integer id, Integer adminId) {
        Asset asset = getAssetById(id);

        employeeService.getEmployeeByID(adminId);

        asset.setStatus("APPROVED");
        asset.setApprovedBy(adminId);

        AssetHistory assetHistory = new AssetHistory();
        assetHistory.setAssetId(id);
        assetHistory.setEmployeeId(asset.getEmployeeId());
        assetHistory.setApprovedBy(adminId);
        assetHistory.setApprovedDate(LocalDate.now());
        assetHistoryRepository.save(assetHistory);

        return assetRepository.save(asset);



    }
}