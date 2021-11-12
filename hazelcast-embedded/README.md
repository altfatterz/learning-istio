```bash
$ mvn clean install jib:dockerBuild -Dimage=altfatterz/hazelcast-embedded:1.0
$ kind load docker-image altfatterz/hazelcast-embedded:1.0
```

