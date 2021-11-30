package br.com.curso.consumer

import br.com.curso.model.Venda
import br.com.curso.service.VendaService
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import java.util.*

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
class VendaConsumer(
    private val objectMapper: ObjectMapper,
    private val vendaService: VendaService
) {

    @Topic("vendas-topic")
    fun receberVenda(@KafkaKey id: String, vendaJson: String) {
        println("Received Message: $id - Payload: $vendaJson")
        val venda = objectMapper.readValue(vendaJson, Venda::class.java)
        vendaService.create(venda)
    }
}