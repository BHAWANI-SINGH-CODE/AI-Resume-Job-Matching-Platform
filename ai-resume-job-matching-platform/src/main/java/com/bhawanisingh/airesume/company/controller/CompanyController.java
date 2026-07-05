package com.bhawanisingh.airesume.company.controller;

import com.bhawanisingh.airesume.common.response.ApiResponse;
import com.bhawanisingh.airesume.company.dto.CompanyCreateRequest;
import com.bhawanisingh.airesume.company.dto.CompanyResponse;
import com.bhawanisingh.airesume.company.dto.CompanyUpdateRequest;
import com.bhawanisingh.airesume.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @Valid @RequestBody CompanyCreateRequest request) {

        CompanyResponse response = companyService.createCompany(request, null);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        List<CompanyResponse> response = companyService.getAllCompanies();

        return ResponseEntity.ok(ApiResponse.success("Companies fetched successfully", response));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompanyById(companyId);

        return ResponseEntity.ok(ApiResponse.success("Company fetched successfully", response));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @PathVariable Long companyId,
            @Valid @RequestBody CompanyUpdateRequest request) {

        CompanyResponse response = companyService.updateCompany(companyId, request);

        return ResponseEntity.ok(ApiResponse.success("Company updated successfully", response));
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);

        return ResponseEntity.ok(ApiResponse.success("Company deleted successfully", null));
    }
}