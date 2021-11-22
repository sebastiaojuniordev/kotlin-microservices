package br.com.curso.http.fallback

import br.com.curso.dto.output.Veiculo
import br.com.curso.http.VeiculoHttp
import io.micronaut.http.HttpResponse
import io.micronaut.retry.annotation.Fallback

@Fallback
class VeiculoHttpFallback: VeiculoHttp {

    override fun findById(id: Long): Veiculo {
        print("chamou fallback")
        return Veiculo(3, "MONZA", "GM", "PPP-1111")
    }
}