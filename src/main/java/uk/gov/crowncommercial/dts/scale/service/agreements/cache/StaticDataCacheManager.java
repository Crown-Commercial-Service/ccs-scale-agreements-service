package uk.gov.crowncommercial.dts.scale.service.agreements.cache;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class StaticDataCacheManager implements CacheManagerCustomizer<ConcurrentMapCacheManager> {


    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {

        List<String> agreementCacheList=List.of("getAgreements", "findAgreementByNumber","findLotByAgreementNumberAndLotNumber","getLot","findLotSupplierOrgRolesByAgreementNumberAndLotNumber");
        cacheManager.setCacheNames(agreementCacheList);
    }



}
