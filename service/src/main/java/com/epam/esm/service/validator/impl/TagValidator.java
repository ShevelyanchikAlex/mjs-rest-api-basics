package com.epam.esm.service.validator.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.ValidatorRegexPattern;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * Implemented {@link Validator} for {@link TagDto}
 */
@Component
public class TagValidator implements Validator<TagDto> {
    @Override
    public void validate(TagDto tagDto) {
        if ((tagDto == null) || !validateName(tagDto.getName())) {
            throw new ServiceException("tag.validate.error");
        }
    }

    private boolean validateName(String name) {
        if (name == null) {
            return false;
        }
        Predicate<String> tagNamePredicate = str -> str.matches(ValidatorRegexPattern.TAG_NAME_REGEX_PATTERN);
        return tagNamePredicate.test(name);
    }
}
