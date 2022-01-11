# Hazelcast 4.X.X MapStore Spring Data JPA Sample

This is an example of how to use `MapStore` and `Spring Data JPA`.

Required properties:

- `driverClassName` - JDBC Driver class e.g. `com.mysql.cj.jdbc.Driver` for MySQL
- `jdbcUrl` - JDBC URL, e.g. `jdbc:mysql://<host>:<port>/<database>`
- `username` - database username
- `password` - database password

## Classes

- [Person](src/main/java/sample/com/hazelcast/cloud/mapstore4/jpa/Person.java) Entity class
- [PersonRepository](src/main/java/sample/com/hazelcast/cloud/mapstore4/jpa/PersonRepository.java) JPA repository
  for Person Entity.
- [PersonMapStore](src/main/java/sample/com/hazelcast/cloud/mapstore4/jpa/JpaPersonMapStore.java) MapStore
  implementation for Person Entity.
- [PersonMapStoreTest](src/test/java/sample/com/hazelcast/cloud/mapstore4/jpa/JpaPersonMapStoreTest.java) Person
  MapStore tests.

