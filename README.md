
Download the Istio versions:
Tested only with the latest patch number of each `major.minor.x` version

```bash
$ cd ~/apps
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.11.4 sh -
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.10.5 sh -
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.9.9 sh -
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.8.6 sh -
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.7.8 sh -
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.6.14 sh - 
```

Create the k8s clusters:

```bash
$ kind create cluster --name k8s-with-istio-1.11.4
$ kind create cluster --name k8s-with-istio-1.10.5
$ kind create cluster --name k8s-with-istio-1.9.9
$ kind create cluster --name k8s-with-istio-1.8.6
$ kind create cluster --name k8s-with-istio-1.7.8
$ kind create cluster --name k8s-with-istio-1.6.14
```

Build the example:

```bash
$ mvn clean package
$ docker build -t altfatterz/hazelcast-embedded:1.0.0 .
```

Try with each ISTIO_VERSION

```bash
$ export ISTIO_VERSION=<SET_IT>
$ export ISTIO_HOME=~/apps/istio-$ISTIO_VERSION
$ export PATH="$ISTIO_HOME/bin:$PATH"
  # switch to the cluster
$ kubectl config use-context kind-k8s-with-istio-$ISTIO_VERSION
$ istioctl version
  # check compatibility
$ istioctl x precheck
  # install istio with the demo profile
$ istioctl install --set profile=demo -y
  # verify that istio components are up and running
$ kubectl get all -n istio-system
  # enable envoy proxy injection
$ kubectl label namespace default istio-injection=enabled
  # load the docker image into the cluster
$ kind load docker-image --name k8s-with-istio-$ISTIO_VERSION altfatterz/hazelcast-embedded:1.0.0
  # allow only mutual TLS traffic
$ kubectl apply -f peer-authentication.yaml
  # deploy the application
$ kubectl apply -f hazelcast-embedded.yaml
$ kubectl get pods 
$ kubectl logs -f <pod> -c hazelcast-embedded
$ # The hazelcast pods should for a cluster like:
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



Results, for each istio version:

```bash
1.11.4 - works
1.10.5 - works
1.9.9  - works
1.8.6  - works
1.7.8  - works
1.6.14 - does not work, hazelcast pods does not form a cluster
````


Reference:
* https://github.com/hazelcast/hazelcast/issues/18320
* https://github.com/hazelcast/hazelcast/issues/18177
* https://github.com/hazelcast/hazelcast/issues/14156 
* https://github.com/hazelcast/hazelcast-kubernetes/issues/118

