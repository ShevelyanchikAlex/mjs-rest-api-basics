package com.epam.esm.service.validator.impl;

import com.epam.esm.repository.filter.condition.GiftCertificateFilterCondition;
import com.epam.esm.repository.filter.condition.SortDirection;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.ValidatorRegexPattern;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * Implemented {@link Validator} for {@link GiftCertificateFilterCondition}
 */
@Component
public class FilterConditionValidator implements Validator<GiftCertificateFilterCondition> {
    @Override
    public void validate(GiftCertificateFilterCondition giftCertificateFilterCondition) {
        if ((giftCertificateFilterCondition == null) ||
                !(validateTagNames(giftCertificateFilterCondition.getTagNames()) &&
                        validatePartOfName(giftCertificateFilterCondition.getName()) &&
                        validatePartOfDescription(giftCertificateFilterCondition.getDescription()) &&
                        validateSortDirection(giftCertificateFilterCondition.getSortDirection()))) {
            throw new ServiceException("gift.certificate.filter.condition.validate.error");
        }
    }

    private boolean validateTagNames(List<String> tagNames) {
        if (tagNames == null) {
            return true;
        }
        Predicate<String> tagNamePredicate = str -> str.matches(ValidatorRegexPattern.TAG_NAME_REGEX_PATTERN);
        for (String tagName : tagNames) {
            if (!tagNamePredicate.test(tagName)) {
                return false;
            }
        }
        return true;
    }

    private boolean validatePartOfName(String partOfName) {
        if (partOfName == null) {
            return true;
        }
        Predicate<String> partOfNamePredicate = str -> str.matches(ValidatorRegexPattern.GIFT_CERTIFICATE_NAME_REGEX_PATTERN);
        return partOfNamePredicate.test(partOfName);
    }

    private boolean validatePartOfDescription(String partOfDescription) {
        if (partOfDescription == null) {
            return true;
        }
        Predicate<String> partOfDescriptionPredicate = str -> str.matches(ValidatorRegexPattern.GIFT_CERTIFICATE_DESCRIPTION_REGEX_PATTERN);
        return partOfDescriptionPredicate.test(partOfDescription);
    }

    private boolean validateSortDirection(SortDirection sortDirection) {
        if (sortDirection == null) {
            return true;
        }
        Predicate<String> orderPredicate = str -> str.matches(ValidatorRegexPattern.SORT_DIRECTION_REGEX_PATTERN);
        return orderPredicate.test(sortDirection.getSortDirection());
    }
}
