package eu.semberal.cloudfoundrysample.service;

import eu.semberal.cloudfoundrysample.entity.Person;

import java.util.List;

public interface PersonService {
    void savePerson(Person p);

    List<Person> getAllPersons();

    void delete(String id);
}
