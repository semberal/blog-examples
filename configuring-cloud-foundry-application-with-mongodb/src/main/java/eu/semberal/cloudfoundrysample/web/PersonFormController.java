package eu.semberal.cloudfoundrysample.web;

import eu.semberal.cloudfoundrysample.entity.Person;
import eu.semberal.cloudfoundrysample.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/add")
public class PersonFormController {
    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.GET)
    public String form(Model m) {
        m.addAttribute("person", new Person());

        return "form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String process(@ModelAttribute("person") Person p, BindingResult result) {

        if(result.hasErrors())
            return "form";
        personService.savePerson(p);
        return "redirect:/list";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setRequiredFields("firstName", "lastName");
    }
}
