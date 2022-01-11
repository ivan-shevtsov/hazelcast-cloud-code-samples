package sample.com.hazelcast.cloud.mapstore4.jpa;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.testcontainers.containers.MySQLContainer;
import sample.com.hazelcast.cloud.mapstore4.jpa.JpaConfig;
import sample.com.hazelcast.cloud.mapstore4.jpa.JpaPersonMapStore;
import sample.com.hazelcast.cloud.mapstore4.jpa.Person;
import sample.com.hazelcast.cloud.mapstore4.jpa.PersonRepository;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import static org.assertj.core.api.Assertions.assertThat;

public class JpaPersonMapStoreTest {

    @ClassRule
    public static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest")
        .withUrlParam("useSSL", "false")
        .withUsername("mapstore")
        .withPassword("maploader")
        .withInitScript("person_table.sql");

    private static final String MAP_NAME = "person";

    private static HazelcastInstance hazelcast;

    private static final AnnotationConfigApplicationContext applicationContext =
        new AnnotationConfigApplicationContext();

    @BeforeClass
    public static void init() {
        Properties properties = new Properties();
        properties.setProperty("driverClassName", mysql.getDriverClassName());
        properties.setProperty("jdbcUrl", mysql.getJdbcUrl());
        properties.setProperty("username", mysql.getUsername());
        properties.setProperty("password", mysql.getPassword());

        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setClassName(JpaPersonMapStore.class.getCanonicalName());
        mapStoreConfig.setProperties(properties);
        mapStoreConfig.setWriteDelaySeconds(0);
        XmlConfigBuilder configBuilder = new XmlConfigBuilder();
        Config config = configBuilder.build();
        MapConfig mapConfig = config.getMapConfig(MAP_NAME);
        mapConfig.setMapStoreConfig(mapStoreConfig);

        hazelcast = Hazelcast.newHazelcastInstance(config);
        applicationContext.register(JpaConfig.class);
        applicationContext.getEnvironment().getPropertySources().addFirst(
            new PropertiesPropertySource("test", properties));
        applicationContext.refresh();

    }

    @AfterClass
    public static void shutdown() {
        if (hazelcast != null) {
            hazelcast.shutdown();
        }
        applicationContext.close();
    }

    @Before
    @After
    public void clearAll() {
        getMap().clear();
        getRepository().deleteAll();
    }

    @Test
    public void store() {
        Person john = Person.builder()
            .id(1)
            .name("John")
            .lastname("Wick")
            .build();

        Person iosef = Person.builder()
            .id(2)
            .name("Iosef")
            .lastname("Tarasov")
            .build();

        getMap().put(1, john);
        getMap().put(2, iosef);

        assertThat(getRepository().findById(1)).hasValue(john);
        assertThat(getRepository().findById(2)).hasValue(iosef);

    }

    @Test
    public void storeAll() {
        Person john = Person.builder()
            .id(1)
            .name("John")
            .lastname("Wick")
            .build();

        Person iosef = Person.builder()
            .id(2)
            .name("Iosef")
            .lastname("Tarasov")
            .build();

        Map<Integer, Person> persons = new LinkedHashMap<>();
        persons.put(1, john);
        persons.put(2, iosef);
        getMap().putAll(persons);

        assertThat(getRepository().findById(1)).hasValue(john);
        assertThat(getRepository().findById(2)).hasValue(iosef);

    }

    @Test
    public void delete() {
        Person john = Person.builder()
            .id(1)
            .name("John")
            .lastname("Wick")
            .build();

        Person iosef = Person.builder()
            .id(2)
            .name("Iosef")
            .lastname("Tarasov")
            .build();

        getMap().put(1, john);
        getMap().put(2, iosef);

        assertThat(getRepository().findById(1)).hasValue(john);
        assertThat(getRepository().findById(2)).hasValue(iosef);

        getMap().remove(1);
        getMap().remove(2);

        assertThat(getRepository().findById(1)).isEmpty();
        assertThat(getRepository().findById(2)).isEmpty();

    }

    @Test
    public void deleteAll() {
        Person john = Person.builder()
            .id(1)
            .name("John")
            .lastname("Wick")
            .build();

        Person iosef = Person.builder()
            .id(2)
            .name("Iosef")
            .lastname("Tarasov")
            .build();

        getMap().put(1, john);
        getMap().put(2, iosef);

        assertThat(getRepository().findById(1)).hasValue(john);
        assertThat(getRepository().findById(2)).hasValue(iosef);

        getMap().clear();

        assertThat(getRepository().findById(1)).isEmpty();
        assertThat(getRepository().findById(2)).isEmpty();

    }

    @Test
    public void load() {

        assertThat(getMap().get(1)).isNull();

        Person john = Person.builder()
            .id(1)
            .name("John")
            .lastname("Wick")
            .build();

        Person iosef = Person.builder()
            .id(2)
            .name("Iosef")
            .lastname("Tarasov")
            .build();

        getRepository().save(john);
        getRepository().save(iosef);

        assertThat(getMap().get(1)).isEqualTo(john);
        assertThat(getMap().get(2)).isEqualTo(iosef);

    }

    @Test
    public void loadAll() {
        Person john = Person.builder()
            .id(1)
            .name("John")
            .lastname("Wick")
            .build();

        Person iosef = Person.builder()
            .id(2)
            .name("Iosef")
            .lastname("Tarasov")
            .build();

        getRepository().save(john);
        getRepository().save(iosef);

        getMap().loadAll(true);

        getRepository().deleteAll();

        assertThat(getMap().get(1)).isEqualTo(john);
        assertThat(getMap().get(2)).isEqualTo(iosef);

    }

    private static IMap<Integer, Person> getMap() {
        return hazelcast.getMap(MAP_NAME);
    }

    private static PersonRepository getRepository() {
        return applicationContext.getBean(PersonRepository.class);
    }

}
