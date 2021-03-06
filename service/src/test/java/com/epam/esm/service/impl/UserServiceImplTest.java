package com.epam.esm.service.impl;

import com.epam.esm.domain.user.Role;
import com.epam.esm.domain.user.Status;
import com.epam.esm.domain.user.User;
import com.epam.esm.dto.UserDto;
import com.epam.esm.mapper.*;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.impl.IdValidator;
import com.epam.esm.service.validator.impl.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class, OrderMapperImpl.class,
        GiftCertificateMapperImpl.class, TagMapperImpl.class})
class UserServiceImplTest {
    private static final List<User> TEST_USERS = List.of(
            new User(1L, "User first", "user1@gmail.com", "pass1", Role.USER, Status.ACTIVE, Collections.emptyList()),
            new User(2L, "User second", "user2@gmail.com", "pass2", Role.USER, Status.ACTIVE, Collections.emptyList()));

    private UserService userService;
    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final IdValidator idValidator = new IdValidator();
    private final UserValidator userValidator = new UserValidator();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    UserServiceImplTest(UserMapper userMapper, OrderMapper orderMapper, GiftCertificateMapper giftCertificateMapper, TagMapper tagMapper) {
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
    }

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepositoryMock, userMapper, orderMapper, idValidator, userValidator, passwordEncoder);
    }

    @Test
    void findById() {
        //then
        Assertions.assertThrows(ServiceException.class, () -> userService.findById(1L));
    }

    @Test
    void findAll() {
        //given
        Mockito.when(userRepositoryMock.findAll(PageRequest.of(1, 10))).thenReturn(new PageImpl<>(TEST_USERS));
        int expected = TEST_USERS.size();
        //when
        Page<UserDto> userPage = userService.findAll(PageRequest.of(1, 10));
        int actual = userPage.getContent().size();
        //then
        Assertions.assertNotNull(userPage);
        Assertions.assertEquals(expected, actual);
    }
}