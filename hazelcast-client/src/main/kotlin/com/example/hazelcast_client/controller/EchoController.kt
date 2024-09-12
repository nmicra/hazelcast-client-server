package com.example.hazelcast_client.controller

import com.example.hazelcast_client.domain.Kuku
import com.hazelcast.map.IMap
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


@RestController
class EchoController {

    @Autowired
    lateinit var kukuCache : IMap<String, Kuku>


    @Operation(summary = "Get a echo message")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/echo/{txt}")
    fun echo(@PathVariable txt: String): String = "$txt !!!"


    @Operation(summary = "Put to Cache")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/put/{txt}")
    fun put(@PathVariable txt: String): String {
        val newKuku = Kuku(LocalDateTime.now(), "$txt-$txt")
        kukuCache.put(txt, newKuku)
        kukuCache.setTtl(txt, 20, TimeUnit.SECONDS)
        return "done: $newKuku"
    }

    @Operation(summary = "Get from Cache")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping("/get/{txt}")
    fun get(@PathVariable txt: String): String {
        return kukuCache[txt].toString()
    }
}