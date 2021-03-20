# JDBC Diff

Simple GUI application to compare data in difference databases. It's possible to compare binary fields using predefined
parsers to convert BLOB's to Java objects.

Parsers must return objects with simple hierarchy consisting of Maps, Lists and wrapped primitive types (numbers and
strings).

## Configuration

Configuration file example:

```yaml
---
font: # font settings for query and result editors
  name: "Monospaced" # Font family, default value "Monospaced"
  style: BOLD # Font style: PLAIN, BOLD, ITALIC or BOLD_ITALIC
  size: 14 # Font size in pixels

rowSimilarity: 80 # percent of different values in row to count rows different
rdiffAlgorithm: GREEDY # select default diff algorithm: CLASSIC, GREEDY or TRIVIAL. By default - GREEDY

blobParsers: # define BLOB parser libraries
  " Do not parser BLOBs":
    libraryPath: # in library is NULL this parser will be replace with default
    parserClass:

  "PostGis": # example for using existing parser
    libraryPath: "file:lib/postgis-parser.jar"
    parserClass: "ru.snake.postgis.parser.PostGisParserFactory"

# Map connection name to driver setting. Several connections can
# use similar settings with different parameters.
drivers:
  "PostgreSQL": # Connection name. This name will be shown in driver settings dialog.
    # URL-like path to JDBC driver library.
    driverPath: "file:lib/postgresql-42.2.5.jar"

    # Full JDBC driver class name
    driverClass: "org.postgresql.Driver"

    # JDBC connection URL to use for this connection. URL can contain
    # special place holders for parameters. Placeholder must be in
    # format "{parameter_name}", where parameter_name - parameter name.
    url: "jdbc:postgresql://{host}:{port}/{database}?user={user}&password={password}"

    # Set of binary types for this driver. If field type is matches to this type
    # BLOB parser will be called for this field
    binaryTypes:
      - "bytea" # default binary type for PostgreSQL
      - "geometry" # PostGis geometry type

    # Parameter names for JDBC URL placeholders. All these parameters
    # will be shown in connection dialog.
    parameters:
      - "host"
      - "port"
      - "database"
      - "user"
      - "password"
```
