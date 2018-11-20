package com.jackshepelev.profitability.controller.project;

import com.jackshepelev.profitability.binding.BindingProjectInputData;
import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.service.project.EnergyTypeService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final EnergyTypeService energyTypeService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService,
                             EnergyTypeService energyTypeService,
                             UserService userService) {

        this.projectService = projectService;
        this.energyTypeService = energyTypeService;
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

        List<EnergyType> energyTypes = energyTypeService.findAll();
        BindingProjectInputData data = new BindingProjectInputData();

        List<EnergyTariff> tariffs = energyTypes
                .stream()
                .map(EnergyTariff::new)
                .collect(Collectors.toCollection(LinkedList::new));

        data.setTariffs(tariffs);

        view.addObject("data", data);
        view.setViewName("/pages/project/project-create");

        return view;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute BindingProjectInputData data) {

        ModelAndView view = new ModelAndView();

        User user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        projectService.save(user, data);

        view.setViewName("redirect:/projects");

        return view;
    }

    @RequestMapping(value="/projects/{id}", method = RequestMethod.GET)
    public ModelAndView find(@PathVariable(value = "id") long id,
                             HttpServletRequest request) {

        ModelAndView view = new ModelAndView();

        try {
            view.addObject("project", projectService.findById(id, request.getLocale()));
        } catch (ProfitabilityException e) {
            view.addObject("error", e.getMessage());
        }
        view.setViewName("/pages/project/project");

        return view;
    }

    @RequestMapping(value="/projects/{id}", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable(value = "id") long id) {

        ModelAndView view = new ModelAndView();

        projectService.deleteById(id);
        view.setViewName("redirect:/projects");

        return view;
    }
}
