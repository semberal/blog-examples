package eu.semberal.cloudfoundrysample.service;

import eu.semberal.cloudfoundrysample.dao.PersonDao;
import eu.semberal.cloudfoundrysample.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPersonService implements PersonService {
    @Autowired
    private PersonDao personDao;


    public void savePerson(Person p) {
        personDao.savePerson(p);
    }

    public List<Person> getAllPersons() {
        return personDao.getAllPersons();
    }

    public void delete(String id) {
        Person p = personDao.find(id);
        personDao.delete(p);
    }

}
