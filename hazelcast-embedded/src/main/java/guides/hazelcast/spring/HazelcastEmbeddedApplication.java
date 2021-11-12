package guides.hazelcast.spring;

import com.hazelcast.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HazelcastEmbeddedApplication {

    public static void main(String[] args) {
        SpringApplication.run(HazelcastEmbeddedApplication.class, args);
    }


    @Bean
    public Config config() {
        Config config = new Config();
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getKubernetesConfig().setProperty("service-dns","hazelcast-embedded-headless");
        return config;
    }
}
