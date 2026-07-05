package com.bhawanisingh.airesume.company.service;

import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import com.bhawanisingh.airesume.company.dto.CompanyCreateRequest;
import com.bhawanisingh.airesume.company.dto.CompanyResponse;
import com.bhawanisingh.airesume.company.dto.CompanyUpdateRequest;
import com.bhawanisingh.airesume.company.entity.Company;
import com.bhawanisingh.airesume.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyResponse createCompany(CompanyCreateRequest request, Long createdByUserId) {
        Company company = new Company();
        company.setName(request.getName().trim());
        company.setWebsite(trimToNull(request.getWebsite()));
        company.setIndustry(trimToNull(request.getIndustry()));
        company.setLocation(trimToNull(request.getLocation()));
        company.setDescription(trimToNull(request.getDescription()));
        company.setLogoUrl(trimToNull(request.getLogoUrl()));
        company.setSize(request.getSize());
        company.setFoundedYear(request.getFoundedYear());
        company.setCreatedByUserId(createdByUserId);
        company.setDeleted(false);

        Company savedCompany = companyRepository.save(company);
        return mapToResponse(savedCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAllByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long companyId) {
        Company company = getActiveCompanyOrThrow(companyId);
        return mapToResponse(company);
    }

    @Override
    public CompanyResponse updateCompany(Long companyId, CompanyUpdateRequest request) {
        Company company = getActiveCompanyOrThrow(companyId);

        company.setName(request.getName().trim());
        company.setWebsite(trimToNull(request.getWebsite()));
        company.setIndustry(trimToNull(request.getIndustry()));
        company.setLocation(trimToNull(request.getLocation()));
        company.setDescription(trimToNull(request.getDescription()));
        company.setLogoUrl(trimToNull(request.getLogoUrl()));
        company.setSize(request.getSize());
        company.setFoundedYear(request.getFoundedYear());

        Company updatedCompany = companyRepository.save(company);
        return mapToResponse(updatedCompany);
    }

    @Override
    public void deleteCompany(Long companyId) {
        Company company = getActiveCompanyOrThrow(companyId);
        company.setDeleted(true);
        companyRepository.save(company);
    }

    private Company getActiveCompanyOrThrow(Long companyId) {
        return companyRepository.findByIdAndDeletedFalse(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
    }

    private CompanyResponse mapToResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setWebsite(company.getWebsite());
        response.setIndustry(company.getIndustry());
        response.setLocation(company.getLocation());
        response.setDescription(company.getDescription());
        response.setLogoUrl(company.getLogoUrl());
        response.setSize(company.getSize());
        response.setFoundedYear(company.getFoundedYear());
        response.setCreatedByUserId(company.getCreatedByUserId());
        response.setCreatedAt(company.getCreatedAt());
        response.setUpdatedAt(company.getUpdatedAt());
        return response;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();
        return trimmedValue.isEmpty() ? null : trimmedValue;
    }
}