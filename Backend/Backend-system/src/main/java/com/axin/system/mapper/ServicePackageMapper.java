package com.axin.system.mapper;

import com.axin.system.domain.ServicePackage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServicePackageMapper {
    ServicePackage selectServicePackageById(@Param("id") Long id);

    List<ServicePackage> selectServicePackageList(ServicePackage pkg);

    int insertServicePackage(ServicePackage pkg);

    int updateServicePackage(ServicePackage pkg);

    int deleteServicePackageByIds(@Param("ids") Long[] ids);

    ServicePackage selectByTypeAndPackageName(@Param("typeId") Long typeId, @Param("packageName") String packageName);
}

