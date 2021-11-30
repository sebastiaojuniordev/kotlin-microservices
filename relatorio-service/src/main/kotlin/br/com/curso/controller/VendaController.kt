package br.com.curso.controller

import br.com.curso.model.Venda
import br.com.curso.service.VendaService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/vendas")
class VendaController(
    private val vendaService: VendaService
) {

    @Get
    fun getAll(): HttpResponse<List<Venda>> {
        return HttpResponse.ok(vendaService.getAll())
    }
}