package com.example.hazelcast_client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class HazelcastClientApplication

fun main(args: Array<String>) {
	runApplication<HazelcastClientApplication>(*args)
}
