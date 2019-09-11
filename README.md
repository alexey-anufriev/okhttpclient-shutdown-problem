# okhttpclient-shutdown-problem

Demonstrates the problem with `OkHttpClient` shutdown. 

The app does not stop after executing all the instructions but hangs.

Reproducible on Java 11:

```
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)
```

BUT works fine on Java 8.
