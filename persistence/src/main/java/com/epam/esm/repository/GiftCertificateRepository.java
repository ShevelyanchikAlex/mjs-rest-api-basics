package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.filter.condition.FilterCondition;

import java.util.List;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificate> {
    List<GiftCertificate> findWithFilter(FilterCondition filterCondition);

    void associateGiftCertificateWithTag(long giftCertificateId, long tagId);

    void deAssociateGiftCertificateWithTag(long giftCertificateId, long tagId);

    boolean existsGiftCertificateByName(String name);
}
