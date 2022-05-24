package com.epam.esm.service.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.DtoConverter;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.pagination.Page;
import com.epam.esm.service.validator.impl.IdValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DtoConverter<UserDto, User> userDtoUserDtoConverter;
    private final DtoConverter<OrderDto, Order> orderDtoOrderDtoConverter;
    private final IdValidator idValidator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           @Qualifier("userDtoConverter") DtoConverter<UserDto, User> userDtoUserDtoConverter,
                           @Qualifier("orderDtoConverter") DtoConverter<OrderDto, Order> orderDtoOrderDtoConverter, IdValidator idValidator) {
        this.userRepository = userRepository;
        this.userDtoUserDtoConverter = userDtoUserDtoConverter;
        this.orderDtoOrderDtoConverter = orderDtoOrderDtoConverter;
        this.idValidator = idValidator;
    }

    @Override
    public UserDto findById(Long id) {
        if (!idValidator.validate(id)) {
            throw new ServiceException("request.validate.error", id);
        }
        User user = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new ServiceException("user.not.found", id));
        return userDtoUserDtoConverter.convertDtoFromEntity(user);
    }

    @Override
    public Page<UserDto> findAll(Integer pageIndex, Integer size) {
        List<UserDto> users = userRepository.findAll(pageIndex, size)
                .stream().map(userDtoUserDtoConverter::convertDtoFromEntity).collect(Collectors.toList());
        return new Page<>(pageIndex, size, userRepository.countAll(), users);
    }

    @Override
    public Page<OrderDto> findUserOrders(Integer pageIndex, Integer size, Long id) {
        User user = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new ServiceException("user.not.found", id));
        List<OrderDto> orders = user.getOrders()
                .stream().skip((long) (pageIndex - 1) * size).limit(size)
                .map(orderDtoOrderDtoConverter::convertDtoFromEntity).collect(Collectors.toList());
        return new Page<>(pageIndex, size, orders.size(), orders);
    }
}
