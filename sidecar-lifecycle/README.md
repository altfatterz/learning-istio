```bash
$ kubectl apply -f k8s.yaml
```

Let's check what is the sidecar container that gets injected:
```bash
$ kubectl get pod busybox -o yaml | yq e '.spec.containers[1]' -
```

```yaml
args:
  - proxy
  - sidecar
  - --domain
  - $(POD_NAMESPACE).svc.cluster.local
  - --proxyLogLevel=warning
  - --proxyComponentLogLevel=misc:error
  - --log_output_level=default:info
  - --concurrency
  - "2"
env:
  - name: JWT_POLICY
    value: third-party-jwt
  - name: PILOT_CERT_PROVIDER
    value: istiod
  - name: CA_ADDR
    value: istiod.istio-system.svc:15012
  - name: POD_NAME
    valueFrom:
      fieldRef:
        apiVersion: v1
        fieldPath: metadata.name
  - name: POD_NAMESPACE
    valueFrom:
      fieldRef:
        apiVersion: v1
        fieldPath: metadata.namespace
  - name: INSTANCE_IP
    valueFrom:
      fieldRef:
        apiVersion: v1
        fieldPath: status.podIP
  - name: SERVICE_ACCOUNT
    valueFrom:
      fieldRef:
        apiVersion: v1
        fieldPath: spec.serviceAccountName
  - name: HOST_IP
    valueFrom:
      fieldRef:
        apiVersion: v1
        fieldPath: status.hostIP
  - name: PROXY_CONFIG
    value: |
      {}
  - name: ISTIO_META_POD_PORTS
    value: |-
      [
          {"containerPort":80,"protocol":"TCP"}
      ]
  - name: ISTIO_META_APP_CONTAINERS
    value: jetty
  - name: ISTIO_META_CLUSTER_ID
    value: Kubernetes
  - name: ISTIO_META_INTERCEPTION_MODE
    value: REDIRECT
  - name: ISTIO_META_WORKLOAD_NAME
    value: jetty-deployment
  - name: ISTIO_META_OWNER
    value: kubernetes://apis/apps/v1/namespaces/default/deployments/jetty-deployment
  - name: ISTIO_META_MESH_ID
    value: cluster.local
  - name: TRUST_DOMAIN
    value: cluster.local
image: docker.io/istio/proxyv2:1.11.4
imagePullPolicy: IfNotPresent
name: istio-proxy
ports:
  - containerPort: 15090
    name: http-envoy-prom
    protocol: TCP
readinessProbe:
  failureThreshold: 30
  httpGet:
    path: /healthz/ready
    port: 15021
    scheme: HTTP
  initialDelaySeconds: 1
  periodSeconds: 2
  successThreshold: 1
  timeoutSeconds: 3
resources:
  limits:
    cpu: "2"
    memory: 1Gi
  requests:
    cpu: 10m
    memory: 40Mi
securityContext:
  allowPrivilegeEscalation: false
  capabilities:
    drop:
      - ALL
  privileged: false
  readOnlyRootFilesystem: true
  runAsGroup: 1337
  runAsNonRoot: true
  runAsUser: 1337
terminationMessagePath: /dev/termination-log
terminationMessagePolicy: File
volumeMounts:
  - mountPath: /var/run/secrets/istio
    name: istiod-ca-cert
  - mountPath: /var/lib/istio/data
    name: istio-data
  - mountPath: /etc/istio/proxy
    name: istio-envoy
  - mountPath: /var/run/secrets/tokens
    name: istio-token
  - mountPath: /etc/istio/pod
    name: istio-podinfo
  - mountPath: /var/run/secrets/kubernetes.io/serviceaccount
    name: kube-api-access-qz2jt
    readOnly: true
```

The jetty container starts up faster, which we don't want:

```bash
$ stern jetty-deployment
+ jetty-deployment-76dbf9c6f9-kh95f › jetty
+ jetty-deployment-76dbf9c6f9-kh95f › istio-proxy
jetty-deployment-76dbf9c6f9-kh95f jetty 2021-11-19 10:46:15.449:INFO :oejs.Server:main: jetty-10.0.7; built: 2021-10-06T19:34:02.766Z; git: da8a4553af9dd84080931fa0f8c678cd2d60f3d9; jvm 17.0.1+12-39
jetty-deployment-76dbf9c6f9-kh95f jetty 2021-11-19 10:46:15.489:INFO :oejdp.ScanningAppProvider:main: Deployment monitor [file:///var/lib/jetty/webapps/]
jetty-deployment-76dbf9c6f9-kh95f jetty 2021-11-19 10:46:15.517:INFO :oejs.AbstractConnector:main: Started ServerConnector@4802796d{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
jetty-deployment-76dbf9c6f9-kh95f jetty 2021-11-19 10:46:15.548:INFO :oejs.Server:main: Started Server@70e8f8e{STARTING}[10.0.7,sto=5000] @594ms
jetty-deployment-76dbf9c6f9-kh95f istio-proxy 2021-11-19T10:46:14.579028Z	info	FLAG: --concurrency="2"
jetty-deployment-76dbf9c6f9-kh95f istio-proxy 2021-11-19T10:46:14.579074Z	info	FLAG: --domain="default.svc.cluster.local"
jetty-deployment-76dbf9c6f9-kh95f istio-proxy 2021-11-19T10:46:14.579123Z	info	FLAG: --help="false"
jetty-deployment-76dbf9c6f9-kh95f istio-proxy 2021-11-19T10:46:14.579137Z	info	FLAG: --log_as_json="false"
jetty-deployment-76dbf9c6f9-kh95f istio-proxy 2021-11-19T10:46:14.579150Z	info	FLAG: --log_caller=""
```

We would like to guarantee that the sidecar container starts up first and then the app container:

This does not work:

```bash
spec:
  containers:
    - name: istio-proxy
      lifecyle: 
        type: Sidecar
```

# pods "jetty-deployment-76dbf9c6f9-kh95f" was not valid:
# * <nil>: Invalid value: "The edited file failed validation": ValidationError(Pod.spec.containers[1].lifecycle): unknown field "type" in io.k8s.api.core.v1.Lifecycle

```




Resources:

* https://banzaicloud.com/blog/k8s-sidecars/