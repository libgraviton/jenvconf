# jenvconf

Small utility that helps configure applications via ENV variables from the outside.

Convert ENV variables with optional prefix and prints out variable output to configure an application.
It can either output `-Dprop=value` output to include in a `java` system call or it can outout a properties file.

## Name conversion

The following conversions are applied if activated on the command line.

* All is converted to lowercase (if activated)
* `_` is removed and the following letter is uppercased (if camelCase option is active). So ENV `ENV_NAME` becomes `envName`.
* `__` is replaced by a `.`. So ENV `ENV__NAME` becomes `env.name`
