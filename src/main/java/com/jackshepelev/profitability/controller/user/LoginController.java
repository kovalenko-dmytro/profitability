package com.jackshepelev.profitability.controller.user;

import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("login");

        return modelAndView;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public ModelAndView logoutPage (HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        modelAndView.setViewName("index");

        return modelAndView;
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("user", new User());
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView();

        User userExists = userService.findByEmail(user.getEmail());

        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            userService.save(user);
            modelAndView.setViewName("redirect:/index");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin-index");

        return modelAndView;
    }
}
