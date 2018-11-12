package com.jackshepelev.profitability.controller.project;

import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.service.project.ProjectService;
import com.jackshepelev.profitability.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping(value="/projects", method = RequestMethod.GET)
    public ModelAndView find() {

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("projects", projectService.findAll());
        modelAndView.setViewName("/pages/project/projects");

        return modelAndView;
    }

    @RequestMapping(value="/projects/create", method = RequestMethod.GET)
    public ModelAndView create() {

        ModelAndView view = new ModelAndView();

        view.addObject("project", new Project());
        view.setViewName("/pages/project/project-create");

        return view;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute Project project) {

        ModelAndView view = new ModelAndView();

        User user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        projectService.save(user, project);

        view.setViewName("redirect:/projects");

        return view;
    }

    @RequestMapping(value="/projects/{id}", method = RequestMethod.GET)
    public ModelAndView find(@PathVariable(value = "id") long id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView();

        try {
            view.addObject("project", projectService.findById(id, request.getLocale()));
        } catch (ProfitabilityException e) {
            view.addObject("error", e.getMessage());
        }
        view.setViewName("/pages/project/project");

        return view;
    }
}
