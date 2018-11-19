package com.jackshepelev.profitability.controller.eem;

import com.jackshepelev.profitability.binding.EemInputData;
import com.jackshepelev.profitability.entity.eem.EnergyEfficiency;
import com.jackshepelev.profitability.entity.project.EnergyType;
import com.jackshepelev.profitability.entity.project.Project;
import com.jackshepelev.profitability.exception.ProfitabilityException;
import com.jackshepelev.profitability.service.eem.EnergyEfficiencyMeasureService;
import com.jackshepelev.profitability.service.project.EnergyTypeService;
import com.jackshepelev.profitability.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EnergyEfficiencyMeasureController {

    private final EnergyEfficiencyMeasureService energyEfficiencyMeasureService;
    private final ProjectService projectService;
    private final EnergyTypeService energyTypeService;

    @Autowired
    public EnergyEfficiencyMeasureController(EnergyEfficiencyMeasureService energyEfficiencyMeasureService,
                                             ProjectService projectService, EnergyTypeService energyTypeService) {

        this.energyEfficiencyMeasureService = energyEfficiencyMeasureService;
        this.projectService = projectService;
        this.energyTypeService = energyTypeService;
    }

    @RequestMapping(value="/projects/{projectID}/eems/create", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable(value = "projectID") long projectID,
                               HttpServletRequest request) {

        ModelAndView view = new ModelAndView();
        try {
            Project project = projectService.findById(projectID, request.getLocale());
            view.addObject("project", project);

            EemInputData data = new EemInputData();
            List<EnergyType> energyTypes = energyTypeService.findAll();

            List<EnergyEfficiency> energyEfficiencies = energyTypes
                    .stream()
                    .map(EnergyEfficiency::new)
                    .collect(Collectors.toCollection(LinkedList::new));

            data.setEnergyEfficiencies(energyEfficiencies);
            view.addObject("data", data);
        } catch (ProfitabilityException e) {
            view.addObject("error", e.getMessage());
        }

        view.setViewName("/pages/eem/eem-create");

        return view;
    }

    @RequestMapping(value = "/projects/{projectID}/eems", method = RequestMethod.POST)
    public ModelAndView create(@PathVariable(value = "projectID") long projectID,
                               @Valid @ModelAttribute EemInputData data,
                               HttpServletRequest request) {

        ModelAndView view = new ModelAndView();

        try {
            energyEfficiencyMeasureService.save(projectID, data, request.getLocale());
            view.setViewName("redirect:/projects/" + projectID + "");
        } catch (ProfitabilityException e) {
            view.addObject("error", e.getMessage());
        }

        return view;
    }
}