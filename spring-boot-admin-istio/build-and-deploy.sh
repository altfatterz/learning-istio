mvn clean package
docker build -t altfatterz/spring-boot-admin-istio:1.0.0 .
kind load docker-image --name k8s-with-istio-1.11.4 altfatterz/spring-boot-admin-istio:1.0.0
kubectl delete -f k8s.yaml
kubectl apply -f k8s.yaml