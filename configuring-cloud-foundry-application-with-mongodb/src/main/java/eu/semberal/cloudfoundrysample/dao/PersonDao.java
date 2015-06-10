package eu.semberal.cloudfoundrysample.dao;

import eu.semberal.cloudfoundrysample.entity.Person;

import java.util.List;

public interface PersonDao {
    void savePerson(Person person);

    List<Person> getAllPersons();

    void delete(Person p);

    Person find(String id);
}
