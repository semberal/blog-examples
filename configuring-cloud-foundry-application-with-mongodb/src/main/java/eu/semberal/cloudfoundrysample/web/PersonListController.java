package eu.semberal.cloudfoundrysample.web;

import eu.semberal.cloudfoundrysample.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PersonListController {
    @Autowired
    private PersonService personService;
    
    @RequestMapping({"/", "/list"})
    public String list(Model model) {
        model.addAttribute("personList", personService.getAllPersons());
        return "list";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("id") String id) {

        personService.delete(id);
        return "redirect:/list";
    }
    
}
