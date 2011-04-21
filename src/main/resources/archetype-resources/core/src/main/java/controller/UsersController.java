#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ${package}.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
@Transactional
public class UsersController {

    @PersistenceContext
    private EntityManager em;

	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @ResponseBody
	public User get(@PathVariable Long userId) {
        logger.info("Fetching user with id: {}", userId);
        User user = em.find(User.class, userId);
        logger.info("Found user: {}", user);
        return user;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
	@ResponseBody
    public User create(User user) {
        logger.info("Creating new user: {}", user);
        user = em.merge(user);
        logger.info("Created user: {}", user);
        return user;
    }

}
