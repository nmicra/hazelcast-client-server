package com.example.hazelcast_client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class HazelcastServerApplication

fun main(args: Array<String>) {
	runApplication<HazelcastServerApplication>(*args)
}
