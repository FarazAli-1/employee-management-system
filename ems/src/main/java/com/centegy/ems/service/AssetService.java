package com.centegy.ems.service;

import com.centegy.ems.dto.request.AssetRequestDTO;
import com.centegy.ems.dto.response.AssetHistoryResponseDTO;
import com.centegy.ems.dto.response.AssetResponseDTO;
import com.centegy.ems.dto.response.PageResponseDTO;
import com.centegy.ems.mapper.AssetHistoryMapper;
import com.centegy.ems.mapper.AssetMapper;
import com.centegy.ems.model.Asset;
import com.centegy.ems.model.AssetHistory;
import com.centegy.ems.model.Employee;
import com.centegy.ems.model.Role;
import com.centegy.ems.model.enums.AssetStatus;
import com.centegy.ems.repository.AssetHistoryRepository;
import com.centegy.ems.repository.AssetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final EmployeeService employeeService;
    private final AssetHistoryRepository assetHistoryRepository;
    private final AssetMapper assetMapper;
    private final AssetHistoryMapper assetHistoryMapper;


    public AssetService(AssetRepository assetRepository, EmployeeService employeeService, AssetHistoryRepository assetHistoryRepository, AssetMapper assetMapper, AssetHistoryMapper assetHistoryMapper) {
        this.assetRepository = assetRepository;
        this.employeeService = employeeService;
        this.assetHistoryRepository = assetHistoryRepository;
        this.assetMapper = assetMapper;
        this.assetHistoryMapper = assetHistoryMapper;
    }

    public AssetResponseDTO createAsset(AssetRequestDTO assetRequestDTO) {

        Asset asset = assetMapper.toEntity(assetRequestDTO);

        Employee realEmployee = employeeService.getEmployeeEntityByID(assetRequestDTO.getEmployeeId());
        asset.setEmployee(realEmployee);

        if (asset.getStatus() == null ) {
                asset.setStatus(AssetStatus.REQUESTED);
        }

        return assetMapper.toAssetResponseDTO(assetRepository.save(asset));

    }

    public PageResponseDTO<AssetResponseDTO> getAllAssets(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Asset> assetPage = assetRepository.findAll(pageRequest);
        List<Asset> assetsList = assetPage.getContent();
        List<AssetResponseDTO> content = assetsList.stream().map(assetMapper::toAssetResponseDTO).toList();

        PageResponseDTO<AssetResponseDTO> response = new PageResponseDTO<>();

        response.setContent(content);
        response.setPageNo(assetPage.getNumber());
        response.setPageSize(assetPage.getSize());
        response.setTotalElements(assetPage.getTotalElements());
        response.setTotalPages(assetPage.getTotalPages());
        response.setLast(assetPage.isLast());

        return response;



    }



    public Asset getAssetEntityById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + id));
    }

    public AssetResponseDTO getAssetById(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + id));

        return assetMapper.toAssetResponseDTO(asset);
    }

    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO updatedData,Long performerID) {


        Asset asset = getAssetEntityById(id);
        validateRoleStatus(performerID);

        Employee Realemployee = employeeService.getEmployeeEntityByID(updatedData.getEmployeeId());

        asset.setAssetName(updatedData.getAssetName());
        asset.setAssetType(updatedData.getAssetType());
        asset.setEmployee(Realemployee);


        return assetMapper.toAssetResponseDTO(assetRepository.save(asset));
    }

    public void deleteAsset(Long id, Long performerID) {

        validateRoleStatus(performerID);

        if (!assetRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Asset not found with ID: " + id);
        }
    }

    public AssetResponseDTO returnAsset(Long id, Long employeeId) {

        Asset asset = getAssetEntityById(id);


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

        return assetMapper.toAssetResponseDTO(assetRepository.save(asset));
    }


    public AssetResponseDTO approveAsset(Long id, Long approverId) {
        Asset asset = getAssetEntityById(id);
        validateRoleStatus(approverId);

        Employee realEmployee = employeeService.getEmployeeEntityByID(approverId);


        asset.setStatus(AssetStatus.APPROVED);
        asset.setApprovedBy(realEmployee);

        AssetHistory assetHistory = new AssetHistory();
        assetHistory.setAsset(asset);
        assetHistory.setEmployee(asset.getEmployee());
        assetHistory.setApprovedBy(realEmployee);
        assetHistory.setApprovedDate(LocalDate.now());
        assetHistoryRepository.save(assetHistory);


        return assetMapper.toAssetResponseDTO(assetRepository.save(asset));
    }

    public List<AssetHistoryResponseDTO> getAssetHistory(Long assetId) {

        List<AssetHistory> assetHistory = assetHistoryRepository.findByAssetId(assetId);


        return assetHistory.stream().map(assetHistoryMapper::toAssetHistoryResponseDTO).toList();
    }

    public void validateRoleStatus(Long id) {
        Employee employee = employeeService.getEmployeeEntityByID(id);
        Role role = employee.getRole();

       if (role.getRoleLevel().getLevel() > 2){
           throw new RuntimeException("Access Denied: You do not have the required rank to perform this action.");
       }

    }
}