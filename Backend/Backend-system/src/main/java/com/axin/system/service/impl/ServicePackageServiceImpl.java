package com.axin.system.service.impl;

import com.axin.system.domain.ServicePackage;
import com.axin.system.mapper.ServicePackageMapper;
import com.axin.system.service.IServicePackageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePackageServiceImpl implements IServicePackageService {

    private final ServicePackageMapper mapper;

    public ServicePackageServiceImpl(ServicePackageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ServicePackage selectServicePackageById(Long id) {
        return mapper.selectServicePackageById(id);
    }

    @Override
    public List<ServicePackage> selectServicePackageList(ServicePackage pkg) {
        return mapper.selectServicePackageList(pkg);
    }

    @Override
    public int insertServicePackage(ServicePackage pkg) {
        return mapper.insertServicePackage(pkg);
    }

    @Override
    public int updateServicePackage(ServicePackage pkg) {
        return mapper.updateServicePackage(pkg);
    }

    @Override
    public int deleteServicePackageByIds(Long[] ids) {
        return mapper.deleteServicePackageByIds(ids);
    }

    @Override
    public boolean isPackageNameExists(Long typeId, String packageName, Long excludeId) {
        ServicePackage existed = mapper.selectByTypeAndPackageName(typeId, packageName);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}

