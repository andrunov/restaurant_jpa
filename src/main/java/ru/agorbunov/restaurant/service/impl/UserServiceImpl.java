package ru.agorbunov.restaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.agorbunov.restaurant.model.User;
import ru.agorbunov.restaurant.repository.UserRepository;
import ru.agorbunov.restaurant.service.UserService;
import ru.agorbunov.restaurant.util.ValidationUtil;
import ru.agorbunov.restaurant.util.exception.NotFoundException;
import ru.agorbunov.restaurant.web.AuthorizedUser;

import java.util.List;

import static ru.agorbunov.restaurant.util.ValidationUtil.*;

/**
 * Class for exchange user-entity data between web and repository layers
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository repository;

    /*save user, check that user not noll*/
    @CacheEvict(value = "users",allEntries = true)
    @Override
    public User save(User user) {
        Assert.notNull(user,"user must not be null");
        ValidationUtil.checkEmpty(user.getName(),"name");
        ValidationUtil.checkEmpty(user.getEmail(),"email");
        ValidationUtil.checkEmpty(user.getPassword(),"password");
        return repository.save(user);
    }

    /*delete user by Id, check that user was found */
    @CacheEvict(value = "users",allEntries = true)
    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id),id);
    }

    /*get all users*/
    @Cacheable("users")
    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    /*get user by Id, check that user was found*/
    @Override
    public User get(int id) {
        return checkNotFoundWithId(repository.get(id),id);
    }

    /*get user by Id with collection of orders were made by the user,
    * check that user was found*/
    @Override
    public User getWithOrders(int id) {
        return checkNotFoundWithId(repository.getWithOrders(id),id);
    }

    /*get user by email, check that user was found*/
    @Override
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    /*evict service-layer cash of user-entities*/
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void evictCache() {
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repository.getByEmail(email.toLowerCase());
        if (u == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(u);
    }
}
