
Download Istio:
```bash
$ cd ~/apps
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.6.14 sh - 
$ export ISTIO_HOME=~/apps/istio-1.6.14
$ export PATH="$ISTIO_HOME/bin:$PATH"
$ istioctl version
no running Istio pods in "istio-system"
1.6.14
```

Create k8s cluster

```bash
$ kind create cluster --name k8s-with-istio-1.6.14
$ kubectl get nodes
NAME                                  STATUS   ROLES                  AGE   VERSION
k8s-with-istio-1.6.14-control-plane   Ready    control-plane,master   94s   v1.21.1
```

Install Istio (with `demo` profile) on the cluster

```bash
$ istioctl x precheck
$ istioctl install --set profile=demo -y
$ kubectl get all -n istio-system
$ kubectl get crd
$ kubectl label namespace default istio-injection=enabled
```

Build the example and load into kind cluster:

```bash
$ docker build -t altfatterz/hazelcast-embedded:1.0.0 .
$ kind load docker-image --name k8s-with-istio-1.6.14 altfatterz/hazelcast-embedded:1.0.0
```

Check the loaded images in the kind cluster:

```bash
$ docker exec -it k8s-with-istio-1.6.14-control-plane bash
$ crictl images | grep altfatterz
docker.io/altfatterz/hazelcast-embedded    1.0.0                0cf52558a1f83       282MB
```

First try it without STRICT security enabled:

```bash
$ kubectl apply -f hazelcast-embedded.yaml
```
The hazelcast pods should form a cluster without any issue:

```bash
2021-11-13 18:39:51.340  INFO 1 --- [           main] com.hazelcast.core.LifecycleService      : [10.244.0.17]:5701 [dev] [5.0] [10.244.0.17]:5701 is STARTING
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.hazelcast.internal.networking.nio.SelectorOptimizer (file:/application/BOOT-INF/lib/hazelcast-5.0.jar) to field sun.nio.ch.SelectorImpl.selectedKeys
WARNING: Please consider reporting this to the maintainers of com.hazelcast.internal.networking.nio.SelectorOptimizer
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
2021-11-13 18:39:51.478  INFO 1 --- [.IO.thread-in-0] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.17]:5701 [dev] [5.0] Initialized new cluster connection between /127.0.0.1:5701 and /127.0.0.1:39846
2021-11-13 18:39:52.302  INFO 1 --- [.IO.thread-in-0] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.17]:5701 [dev] [5.0] Initialized new cluster connection between /127.0.0.1:5701 and /127.0.0.1:39902
2021-11-13 18:39:52.430  INFO 1 --- [.IO.thread-in-1] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.17]:5701 [dev] [5.0] Initialized new cluster connection between /10.244.0.17:35575 and /10.244.0.16:5701
2021-11-13 18:39:52.431  INFO 1 --- [.IO.thread-in-2] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.17]:5701 [dev] [5.0] Initialized new cluster connection between /10.244.0.17:43593 and /10.244.0.15:5701
2021-11-13 18:39:59.451  INFO 1 --- [ration.thread-1] c.h.internal.cluster.ClusterService      : [10.244.0.17]:5701 [dev] [5.0]

Members {size:3, ver:3} [
	Member [10.244.0.15]:5701 - 437a6ce3-435d-4612-a390-5d6ca77bfbc8
	Member [10.244.0.16]:5701 - 28ec1a2d-a30b-42ec-bd5b-19cbc448839b
	Member [10.244.0.17]:5701 - 0aab2241-b10e-45d0-bf06-ff3903530e89 this
]

2021-11-13 18:40:00.478  INFO 1 --- [           main] com.hazelcast.core.LifecycleService      : [10.244.0.17]:5701 [dev] [5.0] [10.244.0.17]:5701 is STARTED
```

Remove the application
```bash
$ kubectl delete -f hazelcast-embedded.yaml
```

Apply the STRICT security:

```bash
kubectl apply -f - <<EOF
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: "default"
spec:
  mtls:
    mode: STRICT
EOF
```





# From here is try and error:

```bash
kubectl apply -f - <<EOF
apiVersion: networking.istio.io/v1alpha3
kind: ServiceEntry
metadata:
  name: hazelcast-external
spec:
  hosts:
  - hazelcast # not used
  addresses:
  - 10.96.0.0/16 # cluster ip cidr
  ports:
  - name: tcp
    number: 5701
    protocol: tcp
  location: MESH_EXTERNAL
  resolution: NONE
EOF  
```

```bash
$ kind load docker-image altfatterz/hazelcast-embedded:1.0
$ kubectl apply -f hazelcast-embedded.yaml
```

```bash
$ istioctl ps
```

https://github.com/hazelcast/hazelcast-kubernetes/issues/118

