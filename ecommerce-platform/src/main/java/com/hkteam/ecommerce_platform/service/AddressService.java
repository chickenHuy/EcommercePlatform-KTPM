package com.hkteam.ecommerce_platform.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hkteam.ecommerce_platform.dto.request.AddressCreationRequest;
import com.hkteam.ecommerce_platform.dto.request.AddressUpdateRequest;
import com.hkteam.ecommerce_platform.dto.response.AddressResponse;
import com.hkteam.ecommerce_platform.dto.response.PaginationResponse;
import com.hkteam.ecommerce_platform.entity.user.Address;
import com.hkteam.ecommerce_platform.exception.AppException;
import com.hkteam.ecommerce_platform.exception.ErrorCode;
import com.hkteam.ecommerce_platform.mapper.AddressMapper;
import com.hkteam.ecommerce_platform.repository.AddressRepository;
import com.hkteam.ecommerce_platform.util.AuthenticatedUserUtil;
import com.hkteam.ecommerce_platform.util.PageUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    AuthenticatedUserUtil authenticatedUserUtil;

    public AddressResponse createAddress(AddressCreationRequest request) {
        var user = authenticatedUserUtil.getAuthenticatedUser();

        Address address = addressMapper.toAddress(request);
        address.setUser(user);

        try {
            addressRepository.save(address);
        } catch (DataIntegrityViolationException e) {
            log.info("Error while creating address {}", e.getMessage());
            throw new AppException(ErrorCode.UNKNOWN_ERROR);
        }

        return addressMapper.toAddressResponse(address);
    }

    public AddressResponse updateAddress(Long id, AddressUpdateRequest request) {
        var user = authenticatedUserUtil.getAuthenticatedUser();

        Address address = addressRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        addressMapper.updateAddressFromRequest(request, address);

        try {
            addressRepository.save(address);
        } catch (DataIntegrityViolationException e) {
            log.info("Error while updating address {}", e.getMessage());
            throw new AppException(ErrorCode.UNKNOWN_ERROR);
        }

        return addressMapper.toAddressResponse(address);
    }

    public void deleteAddress(Long id) {
        var user = authenticatedUserUtil.getAuthenticatedUser();

        Address address = addressRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        addressRepository.delete(address);
    }

    public PaginationResponse<AddressResponse> getAllAddresses(String pageStr, String sizeStr) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageUtils.createPageable(pageStr, sizeStr, sort);

        var pageData = addressRepository.findAll(pageable);
        int page = Integer.parseInt(pageStr);

        PageUtils.validatePageBounds(page, pageData);

        return PaginationResponse.<AddressResponse>builder()
                .currentPage(Integer.parseInt(pageStr))
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .hasNext(pageData.hasNext())
                .hasPrevious(pageData.hasPrevious())
                .nextPage(pageData.hasNext() ? page + 1 : null)
                .previousPage(pageData.hasPrevious() ? page - 1 : null)
                .data(pageData.getContent().stream()
                        .map(addressMapper::toAddressResponse)
                        .toList())
                .build();
    }

    public AddressResponse getOneAddressById(Long id) {
        var user = authenticatedUserUtil.getAuthenticatedUser();

        Address address = addressRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        return addressMapper.toAddressResponse(address);
    }
}