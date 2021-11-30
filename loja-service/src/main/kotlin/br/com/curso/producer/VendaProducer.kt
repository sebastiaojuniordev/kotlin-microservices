package br.com.curso.producer

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface VendaProducer {

    @Topic("vendas-topic")
    fun publicarVenda(@KafkaKey id: String, vendaJson: String)
}