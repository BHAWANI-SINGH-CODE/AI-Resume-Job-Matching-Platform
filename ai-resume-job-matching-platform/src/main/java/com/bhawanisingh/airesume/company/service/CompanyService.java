package com.bhawanisingh.airesume.company.service;

import com.bhawanisingh.airesume.company.dto.CompanyCreateRequest;
import com.bhawanisingh.airesume.company.dto.CompanyResponse;
import com.bhawanisingh.airesume.company.dto.CompanyUpdateRequest;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(CompanyCreateRequest request, Long createdByUserId);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse getCompanyById(Long companyId);

    CompanyResponse updateCompany(Long companyId, CompanyUpdateRequest request);

    void deleteCompany(Long companyId);
}