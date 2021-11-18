```bash
$ mvn clean package
$ docker build -t altfatterz/spring-boot-admin-istio:1.0.0 .
$ kind load docker-image --name k8s-with-istio-1.9.9 altfatterz/hazelcast-embedded:1.0.0
```

