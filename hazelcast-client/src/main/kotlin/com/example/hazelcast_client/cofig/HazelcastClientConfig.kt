package com.example.hazelcast_client.cofig

import com.example.hazelcast_client.domain.Kuku
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import com.hazelcast.client.config.ClientNetworkConfig
import com.hazelcast.collection.ISet
import com.hazelcast.config.InMemoryFormat
import com.hazelcast.config.KubernetesConfig
import com.hazelcast.config.NearCacheConfig
import com.hazelcast.core.EntryEvent
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.cp.CPSubsystem
import com.hazelcast.map.IMap
import com.hazelcast.map.listener.EntryExpiredListener
import com.hazelcast.nio.serialization.IdentifiedDataSerializable
import java.time.LocalDateTime
import java.util.*

@Configuration
class HazelcastClientConfig(
    @Value("\${hzclientms.hazelcast.clustername}") val clusterName: String,
    @Value("\${hzclientms.hazelcast.servicename}") val k8sservicename: String,
    @Value("\${hzclientms.hazelcast.communicationPort}") val communicationPort: Int,
    @Value("\${hzclientms.hazelcast.k8snamespace}") val k8snamespace: String,
    @Value("\${hzclientms.hazelcast.nonK8sServers}") val nonK8sServers: String,
    @Value("\${hzclientms.hazelcast.isK8s}") val isK8s: Boolean
) {
    @Bean
    fun hazelcastInstance(): HazelcastInstance {
        val clientConfig = ClientConfig()
        clientConfig.clusterName = clusterName

        if (isK8s) {
            val k8sConfig = KubernetesConfig()
            k8sConfig.isEnabled = true
            k8sConfig.setProperty("namespace", k8snamespace)
            k8sConfig.setProperty("service-port", "$communicationPort")
            k8sConfig.setProperty("service-name", k8sservicename)
            val cnc = ClientNetworkConfig().apply {
                kubernetesConfig = k8sConfig
            }
            clientConfig.networkConfig = cnc

        } else {
            if (nonK8sServers.isNotBlank()) {
                val networkConfig = ClientNetworkConfig()
                networkConfig.addresses = nonK8sServers.split(",")
                clientConfig.networkConfig = networkConfig
            }
        }

        return HazelcastClient.newHazelcastClient(clientConfig)
    }


    @Bean
    fun kukuCache(hazelcastInstance: HazelcastInstance, hzLockMechanism : CPSubsystem): IMap<String, Kuku>
            = hazelcastInstance.getMap<String, Kuku>(Kuku::class.java.simpleName)
        .also { it.addEntryListener(object : EntryExpiredListener<String, Kuku>{
            override fun entryExpired(event: EntryEvent<String, Kuku>) {
                val lock = hzLockMechanism.getLock(event.key)
                if (lock.tryLock()) {
                    println(">>>> KUKU EXPIRED ${LocalDateTime.now()}: ${event.oldValue}")
                } else println(">>> do nothing ...")
            }
        }, true) }

    @Bean
    fun trackingKeysSet(hazelcastInstance: HazelcastInstance, hzLockMechanism : CPSubsystem): ISet<String> {
        val iset = hazelcastInstance.getSet<String>("trackingkeys")
        return iset
    }


    @Bean
    fun hzLockMechanism(hazelcast: HazelcastInstance) : CPSubsystem = hazelcast.cpSubsystem
    
}