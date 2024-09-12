# HOW2 Jenkins Build & Creating a Report

```
$ ./mvnw clean
$ ./mvnw checkstyle:checkstyle
$ ./mvnw package -Dspring.profiles.active=test -Dmaven.test.failure.ignore=true
$ ./mvnw spotbugs:spotbugs

```

