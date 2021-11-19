```bash
$ kind create cluster --name k8s-with-istio-1.9.9
$ export ISTIO_VERSION=1.9.9
  # I have already downloaded this version
$ export ISTIO_HOME=~/apps/istio-$ISTIO_VERSION
$ export PATH="$ISTIO_HOME/bin:$PATH"
$ kubectl config use-context kind-k8s-with-istio-$ISTIO_VERSION
$ istioctl version
  no running Istio pods in "istio-system"
  1.9.9
$ istioctl x precheck
  # values.global.proxy.holdApplicationUntilProxyStarts is deprecated
$ istioctl install --set profile=demo --set meshConfig.defaultConfig.holdApplicationUntilProxyStarts=true -y
$ kubectl get all -n istio-system
$ kubectl apply -f k8s.yaml
```

In the logs we see that the istio-proxy starts first and blocks the other container to start until the istio-proxy is ready.

```bash
$ stern jetty-deployment
+ jetty-deployment-76dbf9c6f9-6r8sl › jetty
+ jetty-deployment-76dbf9c6f9-6r8sl › istio-proxy
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073004Z	info	FLAG: --concurrency="2"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073022Z	info	FLAG: --domain="default.svc.cluster.local"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073025Z	info	FLAG: --help="false"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073027Z	info	FLAG: --log_as_json="false"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073028Z	info	FLAG: --log_caller=""
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073030Z	info	FLAG: --log_output_level="default:info"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073031Z	info	FLAG: --log_rotate=""
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073033Z	info	FLAG: --log_rotate_max_age="30"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073035Z	info	FLAG: --log_rotate_max_backups="1000"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073036Z	info	FLAG: --log_rotate_max_size="104857600"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073038Z	info	FLAG: --log_stacktrace_level="default:none"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073043Z	info	FLAG: --log_target="[stdout]"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073045Z	info	FLAG: --meshConfig="./etc/istio/config/mesh"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073046Z	info	FLAG: --outlierLogPath=""
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073048Z	info	FLAG: --proxyComponentLogLevel="misc:error"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073049Z	info	FLAG: --proxyLogLevel="warning"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073051Z	info	FLAG: --serviceCluster="jetty.default"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073052Z	info	FLAG: --stsPort="0"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073060Z	info	FLAG: --templateFile=""
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073062Z	info	FLAG: --tokenManagerPlugin="GoogleTokenExchange"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073066Z	info	Version 1.9.9-08ce6e1ce27b60c9a296bc1b6417aa840ab22d91-Clean
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073201Z	info	Apply proxy config from env {"holdApplicationUntilProxyStarts":true}
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073766Z	info	Effective config: binaryPath: /usr/local/bin/envoy
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy concurrency: 2
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy configPath: ./etc/istio/proxy
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy controlPlaneAuthPolicy: MUTUAL_TLS
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy discoveryAddress: istiod.istio-system.svc:15012
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy drainDuration: 45s
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy holdApplicationUntilProxyStarts: true
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy parentShutdownDuration: 60s
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy proxyAdminPort: 15000
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy serviceCluster: jetty.default
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy statNameLength: 189
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy statusPort: 15020
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy terminationDrainDuration: 5s
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy tracing:
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy   zipkin:
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy     address: zipkin.istio-system:9411
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073781Z	info	Proxy role	ips=[10.244.0.8 fe80::b06d:bfff:fef0:372c] type=sidecar id=jetty-deployment-76dbf9c6f9-6r8sl.default domain=default.svc.cluster.local
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073786Z	info	JWT policy is third-party-jwt
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073790Z	info	Pilot SAN: [istiod.istio-system.svc]
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073792Z	info	CA Endpoint istiod.istio-system.svc:15012, provider Citadel
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073820Z	info	Using CA istiod.istio-system.svc:15012 cert with certs: var/run/secrets/istio/root-cert.pem
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.073877Z	info	citadelclient	Citadel client using custom root cert: istiod.istio-system.svc:15012
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.104760Z	info	ads	All caches have been synced up in 33.987187ms, marking server ready
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.105072Z	info	sds	SDS server for workload certificates started, listening on "./etc/istio/proxy/SDS"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.105096Z	info	xdsproxy	Initializing with upstream address "istiod.istio-system.svc:15012" and cluster "Kubernetes"
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.105574Z	info	Starting proxy agent
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.105761Z	info	sds	Start SDS grpc server
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.105874Z	info	Opening status port 15020
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.106071Z	info	Received new config, creating new Envoy epoch 0
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.106373Z	info	Epoch 0 starting
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.126981Z	info	Envoy command: [-c etc/istio/proxy/envoy-rev0.json --restart-epoch 0 --drain-time-s 45 --parent-shutdown-time-s 60 --service-cluster jetty.default --service-node sidecar~10.244.0.8~jetty-deployment-76dbf9c6f9-6r8sl.default~default.svc.cluster.local --local-address-ip-version v4 --bootstrap-version 3 --log-format %Y-%m-%dT%T.%fZ	%l	envoy %n	%v -l warning --component-log-level misc:error --concurrency 2]
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.162215Z	warning	envoy runtime	Unable to use runtime singleton for feature envoy.http.headermap.lazy_map_min_size
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.162257Z	warning	envoy runtime	Unable to use runtime singleton for feature envoy.http.headermap.lazy_map_min_size
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.162636Z	warning	envoy runtime	Unable to use runtime singleton for feature envoy.http.headermap.lazy_map_min_size
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.162678Z	warning	envoy runtime	Unable to use runtime singleton for feature envoy.http.headermap.lazy_map_min_size
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.197500Z	info	xdsproxy	connected to upstream XDS server: istiod.istio-system.svc:15012
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.214581Z	info	ads	ADS: new connection for node:sidecar~10.244.0.8~jetty-deployment-76dbf9c6f9-6r8sl.default~default.svc.cluster.local-1
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.215042Z	info	ads	ADS: new connection for node:sidecar~10.244.0.8~jetty-deployment-76dbf9c6f9-6r8sl.default~default.svc.cluster.local-2
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.392415Z	info	cache	Root cert has changed, start rotating root cert
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.392450Z	info	ads	XDS: Incremental Pushing:0 ConnectedEndpoints:2 Version:
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.392483Z	info	cache	generated new workload certificate	latency=287.17421ms ttl=23h59m59.607523484s
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.392518Z	info	cache	returned delayed workload certificate from cache	ttl=23h59m59.60748353s
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.392880Z	info	sds	SDS: PUSH	resource=default
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.415221Z	info	sds	SDS: PUSH	resource=ROOTCA
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.415898Z	info	sds	SDS: PUSH	resource=ROOTCA
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.456820Z	warning	envoy filter	mTLS PERMISSIVE mode is used, connection can be either plaintext or TLS, and client cert can be omitted. Please consider to upgrade to mTLS STRICT mode for more secure configuration that only allows TLS connection with client cert. See https://istio.io/docs/tasks/security/mtls-migration/
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.458690Z	warning	envoy filter	mTLS PERMISSIVE mode is used, connection can be either plaintext or TLS, and client cert can be omitted. Please consider to upgrade to mTLS STRICT mode for more secure configuration that only allows TLS connection with client cert. See https://istio.io/docs/tasks/security/mtls-migration/
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.460560Z	warning	envoy filter	mTLS PERMISSIVE mode is used, connection can be either plaintext or TLS, and client cert can be omitted. Please consider to upgrade to mTLS STRICT mode for more secure configuration that only allows TLS connection with client cert. See https://istio.io/docs/tasks/security/mtls-migration/
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.461916Z	warning	envoy filter	mTLS PERMISSIVE mode is used, connection can be either plaintext or TLS, and client cert can be omitted. Please consider to upgrade to mTLS STRICT mode for more secure configuration that only allows TLS connection with client cert. See https://istio.io/docs/tasks/security/mtls-migration/
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.637211Z	info	Initialization took 578.776716ms
jetty-deployment-76dbf9c6f9-6r8sl istio-proxy 2021-11-19T13:35:17.637366Z	info	Envoy proxy is ready
jetty-deployment-76dbf9c6f9-6r8sl jetty 2021-11-19 13:35:29.550:INFO :oejs.Server:main: jetty-10.0.7; built: 2021-10-06T19:34:02.766Z; git: da8a4553af9dd84080931fa0f8c678cd2d60f3d9; jvm 17.0.1+12-39
jetty-deployment-76dbf9c6f9-6r8sl jetty 2021-11-19 13:35:29.590:INFO :oejdp.ScanningAppProvider:main: Deployment monitor [file:///var/lib/jetty/webapps/]
jetty-deployment-76dbf9c6f9-6r8sl jetty 2021-11-19 13:35:29.610:INFO :oejs.AbstractConnector:main: Started ServerConnector@4802796d{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
jetty-deployment-76dbf9c6f9-6r8sl jetty 2021-11-19 13:35:29.631:INFO :oejs.Server:main: Started Server@70e8f8e{STARTING}[10.0.7,sto=5000] @440ms
```