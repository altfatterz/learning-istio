```bash
$ kind create cluster --name k8s-without-istio
$ mvn clean install jib:dockerBuild -Dimage=altfatterz/hazelcast-embedded:1.0
$ kind load docker-image altfatterz/hazelcast-embedded:1.0
$ kubectl apply -f hazelcast-embedded.yaml
```


```bash
$ kind create cluster --name k8s-with-istio
$ cd ~/apps
$ curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.6.14 sh - 
$ export ISTIO_HOME=~/apps/istio-1.6.14
$ export PATH="$ISTIO_HOME/bin:$PATH"
$ istioctl version
$ istioctl x precheck
$ istioctl install --set profile=demo -y
$ kubectl get all -n istio-system
$ kubectl get crd
$ kubectl label namespace default istio-injection=enabled
```

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


https://github.com/hazelcast/hazelcast-kubernetes/issues/118

