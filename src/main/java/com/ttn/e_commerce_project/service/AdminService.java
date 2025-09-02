package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.vo.*;
import org.springframework.data.domain.Page;

public interface AdminService {

    Page<CustomerVo> listAllCustomers(int pageSize, int pageOffset, String sort, String email);
    Page<SellerVo> listAllSellers(int pageSize, int pageOffset, String sort, String email);
    boolean activeCustomer(Long id);
    boolean activateSeller (Long id);
    boolean deactivateCustomer(Long id);
    boolean deactivateSeller(Long id);
    CategoryMetaDataField addMetaDataField(MetadataFieldCo metadataFieldCo);
    Page<MetadataFieldVo> getAllMetadataFields(int offset, int max, String sortBy, String order, String query);
    CategoryVo addCategory(CategoryCo categoryCo);
}
