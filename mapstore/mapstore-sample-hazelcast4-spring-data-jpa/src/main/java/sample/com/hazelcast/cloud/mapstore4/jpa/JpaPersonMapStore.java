package sample.com.hazelcast.cloud.mapstore4.jpa;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;

public class JpaPersonMapStore implements MapStore<Integer, Person>, MapLoaderLifecycleSupport {

    private static final ILogger log = Logger.getLogger(JpaPersonMapStore.class);

    private final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    private PersonRepository personRepository;

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        log.info(String.format("JpaPersonMapStore(%s)::initializing", mapName));
        this.applicationContext.getEnvironment().getPropertySources().addFirst(
            new PropertiesPropertySource("JpaPersonMapStore", properties));
        this.applicationContext.register(JpaConfig.class);
        this.applicationContext.refresh();
        this.personRepository = this.applicationContext.getBean(PersonRepository.class);
        log.info(String.format("JpaPersonMapStore(%s)::initialized", mapName));
    }

    @Override
    public void destroy() {
        log.info("JpaPersonMapStore::destroying");
        this.applicationContext.close();
        log.info("JpaPersonMapStore::destroyed");
    }

    @Override
    public void store(Integer key, Person value) {
        log.info(String.format("JpaPersonMapStore::store key %s value %s", key, value));
        this.personRepository.save(value);
    }

    @Override
    public void storeAll(Map<Integer, Person> map) {
        log.info(String.format("JpaPersonMapStore::store all %s", map));
        for (Map.Entry<Integer, Person> entry : map.entrySet()) {
            store(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void delete(Integer key) {
        log.info(String.format("JpaPersonMapStore::delete key %s", key));
        this.personRepository.deleteById(key);
    }

    @Override
    public void deleteAll(Collection<Integer> keys) {
        log.info(String.format("JpaPersonMapStore::delete all %s", keys));
        keys.forEach(this::delete);
    }

    @Override
    public Person load(Integer key) {
        log.info(String.format("JpaPersonMapStore::load by key %s", key));
        return this.personRepository.findById(key).orElse(null);
    }

    @Override
    public Map<Integer, Person> loadAll(Collection<Integer> keys) {
        log.info(String.format("JpaPersonMapStore::loadAll by keys %s", keys));
        Map<Integer, Person> result = new LinkedHashMap<>();
        for (Integer key : keys) {
            Person person = load(key);
            if (person != null) {
                result.put(key, person);
            }
        }
        return result;
    }

    @Override
    public Iterable<Integer> loadAllKeys() {
        log.info("JpaPersonMapStore::loadAllKeys");
        return this.personRepository.getAllIds();
    }

}
