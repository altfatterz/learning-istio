```bash
$ mvn clean package
$ docker build -t altfatterz/spring-boot-admin-client:1.0.0 .
$ kind load docker-image --name k8s-with-istio-$ISTIO_VERSION altfatterz/spring-boot-admin-client:1.0.0
```
