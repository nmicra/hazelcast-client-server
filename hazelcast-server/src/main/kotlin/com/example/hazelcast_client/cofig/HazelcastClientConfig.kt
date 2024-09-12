package com.example.hazelcast_client.cofig

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HazelcastClientConfig(
    @Value("\${hzserverms.hazelcast.clustername}") val clusterName: String,
    @Value("\${hzserverms.hazelcast.servicename}") val k8sservicename: String,
    @Value("\${hzserverms.hazelcast.communicationPort}") val communicationPort: Int,
    @Value("\${hzserverms.hazelcast.k8snamespace}") val k8snamespace: String,
    @Value("\${hzserverms.hazelcast.isK8s}") val isK8s: Boolean
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun hazelcast(): HazelcastInstance = Hazelcast.newHazelcastInstance(getHazelcastCong())

    private fun getHazelcastCong(): Config {
        logger.debug(">>>> HazelcastConfig isK8s = $isK8s")
        val hzCnf = Config()

        if (isK8s) {
            logger.info(">>> Hazelcast starts in KUBERNETES mode. ServiceName=$k8sservicename, NameSpace=$k8snamespace")
            hzCnf.networkConfig.join.tcpIpConfig.isEnabled = false
            hzCnf.networkConfig.join.multicastConfig.isEnabled = false
            hzCnf.networkConfig.join.kubernetesConfig.isEnabled = true
            hzCnf.networkConfig.join.kubernetesConfig.setProperty("namespace", k8snamespace)
            hzCnf.networkConfig.join.kubernetesConfig.setProperty("service-port", "$communicationPort")
            hzCnf.networkConfig.join.kubernetesConfig.setProperty("service-name", k8sservicename)
        }
        hzCnf.clusterName = clusterName
        hzCnf.networkConfig.restApiConfig.isEnabled = true
        return hzCnf
    }
    
}