package com.jackshepelev.profitability.controller.project;

import com.jackshepelev.profitability.binding.BindingProjectInputData;
import com.jackshepelev.profitability.binding.ValidList;
import com.jackshepelev.profitability.entity.project.EnergyTariff;
import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.entity.user.User;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.service.project.EnergyTypeService;
import com.jackshepelev.profitability.service.project.ProjectService;
import com.jackshepelev.profitability.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
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

        ValidList<EnergyTariff> tariffs = energyTypes
                .stream()
                .map(EnergyTariff::new)
                .collect(Collectors.toCollection(ValidList::new));

        data.setTariffs(tariffs);

        view.addObject("data", data);
        view.setViewName("/pages/project/project-create");

        return view;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("data") BindingProjectInputData data,
                               BindingResult result) {

        ModelAndView view = new ModelAndView();

        if (result.hasErrors()) {
            view.addObject("data", data);
            view.addAllObjects(result.getModel());
            view.setViewName("/pages/project/project-create");
            return view;
        }

        User user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        projectService.save(user, data);

        view.setViewName("redirect:/projects");

        return view;
    }

    @RequestMapping(value="/projects/{id}/update", method = RequestMethod.GET)
    public ModelAndView update(@PathVariable(value = "id") long id,
                               HttpServletRequest request) {

        ModelAndView view = new ModelAndView();
        try {
            Project project = projectService.findById(id, request.getLocale());
            BindingProjectInputData data = new BindingProjectInputData();
            data.setTariffs(project.getTariffs().stream().collect(Collectors.toCollection(ValidList::new)));
            data.setNominalDiscountRate(project.getDiscountRate().getNominalDiscountRate().multiply(BigDecimal.valueOf(100)));
            data.setInflationRate(project.getDiscountRate().getInflationRate().multiply(BigDecimal.valueOf(100)));
            data.setTitle(project.getTitle());
            view.addObject("project", project);
            view.addObject("data", data);
        } catch (ProfitabilityException e) {
            view.addObject("error", e.getMessage());
        }

        view.setViewName("/pages/project/project-update");

        return view;
    }

    @RequestMapping(value="/projects/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable(value = "id") long id,
                               @Valid @ModelAttribute("data") BindingProjectInputData data,
                               BindingResult result,
                               HttpServletRequest request) {

        ModelAndView view = new ModelAndView();

        try {
            if (result.hasErrors()) {
                Project project = projectService.findById(id, request.getLocale());
                view.addObject("project", project);
                view.addObject("data", data);
                view.addAllObjects(result.getModel());
                view.setViewName("/pages/project/project-update");
                return view;
            }
            projectService.update(id, data, request.getLocale());
            view.setViewName("redirect:/projects");
        } catch (ProfitabilityException e) {
            view.addObject("error", e.getMessage());
        }

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
