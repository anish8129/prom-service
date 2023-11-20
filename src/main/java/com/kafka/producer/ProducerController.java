/* (C) Games24x7 */
package com.kafka.producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private final KafkaProducerDriver kafkaProducerDriver;

    public ProducerController(KafkaProducerDriver kafkaProducerDriver) {
        this.kafkaProducerDriver = kafkaProducerDriver;
    }

    @GetMapping("/send/{key}/{value}")
    void send(@PathVariable String key, @PathVariable String value) {
        kafkaProducerDriver.send(key, value);
    }
}
