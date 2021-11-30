package br.com.curso.service

import br.com.curso.config.RedisConnection
import br.com.curso.dto.output.Veiculo
import br.com.curso.http.VeiculoHttp
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Singleton

@Singleton
class VeiculoService(
    private val veiculoHttp: VeiculoHttp,
    private val objectMapper: ObjectMapper
) {

    fun getVeiculo(id: Long): Veiculo {
        val veiculo = veiculoHttp.findById(id)
        gravarCache(veiculo)
        return veiculo
    }

    fun gravarCache(veiculo: Veiculo) {
        val jedis = RedisConnection.getConnection()
        val veiculoJson = objectMapper.writeValueAsString(veiculo)
        jedis.mset(veiculo.id.toString(), veiculoJson)
    }
}