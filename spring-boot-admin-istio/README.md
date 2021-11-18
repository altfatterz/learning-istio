```bash
$ mvn clean package
$ docker build -t altfatterz/spring-boot-admin-istio:1.0.0 .
$ kind load docker-image --name k8s-with-istio-$ISTIO_VERSION altfatterz/spring-boot-admin-istio:1.0.0
```


Access the UI:

```bash
$ k port-forward svc/spring-boot-admin-istio 8080:80
$ curl localhost:8080
```


