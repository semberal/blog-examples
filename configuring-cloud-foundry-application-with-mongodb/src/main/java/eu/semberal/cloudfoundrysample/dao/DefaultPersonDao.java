package eu.semberal.cloudfoundrysample.dao;

import eu.semberal.cloudfoundrysample.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultPersonDao implements PersonDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    public Person find(String id) {
        return mongoTemplate.findById(id, Person.class);
    }

    public void savePerson(Person person) {
        mongoTemplate.save(person);
    }

    public List<Person> getAllPersons() {
        return mongoTemplate.findAll(Person.class);
    }

    public void delete(Person p) {
        mongoTemplate.remove(p, "person");

    }
}
