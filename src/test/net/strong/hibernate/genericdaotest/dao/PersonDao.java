package test.net.strong.hibernate.genericdaotest.dao;

import java.util.List;
import java.util.Iterator;

import net.strong.hibernate.genericdao.GenericDao;
import test.net.strong.hibernate.genericdaotest.domain.Person;

public interface PersonDao extends GenericDao<Person, Long>
{
    List<Person> findByName(String name);
    Iterator<Person> iterateByWeight(int weight);
}
