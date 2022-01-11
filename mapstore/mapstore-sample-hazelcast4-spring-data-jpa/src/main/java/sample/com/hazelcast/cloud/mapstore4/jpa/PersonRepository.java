package sample.com.hazelcast.cloud.mapstore4.jpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Query("SELECT id FROM Person")
    Iterable<Integer> getAllIds();

    @Override
    @Modifying
    @Query("DELETE FROM Person WHERE id = ?1")
    void deleteById(Integer id);

}
