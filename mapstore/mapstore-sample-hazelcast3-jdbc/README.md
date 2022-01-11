# Hazelcast 3.X.X MapStore JDBC Sample

This is an example of how to use `MapStore` and `JDBC`.

Required properties:

- `driverClassName` - JDBC Driver class e.g. `com.mysql.cj.jdbc.Driver` for MySQL
- `jdbcUrl` - JDBC URL, e.g. `jdbc:mysql://<host>:<port>/<database>`
- `username` - database username
- `password` - database password

## Classes

- [Person](src/main/java/sample/com/hazelcast/cloud/mapstore3/jdbc/Person.java) Entity class
- [JdbcPersonRepository](src/main/java/sample/com/hazelcast/cloud/mapstore3/jdbc/JdbcPersonRepository.java) JDBC
  implementation for Person Entity.
- [PersonMapStore](src/main/java/sample/com/hazelcast/cloud/mapstore3/jdbc/JdbcPersonMapStore.java) MapStore
  implementation for Person Entity.
- [PersonMapStoreTest](src/test/java/sample/com/hazelcast/cloud/mapstore3/jdbc/JdbcPersonMapStoreTest.java) Person
  MapStore tests.

