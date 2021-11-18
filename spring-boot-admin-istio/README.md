```bash
$ mvn clean package
$ docker build -t altfatterz/spring-boot-admin-istio:1.0.0 .
$ kind load docker-image --name k8s-with-istio-1.11.4 altfatterz/spring-boot-admin-istio:1.0.0
```


Access the UI:

```bash
$ k port-forward svc/spring-boot-admin-istio 8080:80
$ curl localhost:8080
```


Issues:

1. https://stackoverflow.com/questions/67381065/java-util-concurrent-timeoutexception-did-not-observe-any-item-or-terminal-sign