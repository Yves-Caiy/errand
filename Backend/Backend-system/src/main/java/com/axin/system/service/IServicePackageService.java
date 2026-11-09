package com.axin.system.service;

import com.axin.system.domain.ServicePackage;

import java.util.List;

public interface IServicePackageService {
    ServicePackage selectServicePackageById(Long id);

    List<ServicePackage> selectServicePackageList(ServicePackage pkg);

    int insertServicePackage(ServicePackage pkg);

    int updateServicePackage(ServicePackage pkg);

    int deleteServicePackageByIds(Long[] ids);

    boolean isPackageNameExists(Long typeId, String packageName, Long excludeId);
}

