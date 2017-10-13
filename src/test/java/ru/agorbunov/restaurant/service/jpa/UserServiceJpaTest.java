package ru.agorbunov.restaurant.service.jpa;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.agorbunov.restaurant.Profiles;
import ru.agorbunov.restaurant.repository.jpa.JpaUtil;
import ru.agorbunov.restaurant.service.UserServiceImplTest;

/**
 * Created by Admin on 23.02.2017.
 */
// TODO: 23.02.2017 delete class before production
@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends UserServiceImplTest {

    @Autowired
    protected JpaUtil jpaUtil;

    @Before
    @Override
    public void setUp() throws Exception {
        jpaUtil.clear2ndLevelHibernateCache();
        super.setUp();
    }
}
