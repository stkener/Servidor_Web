package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.net.URL
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  describe("Un servidor web") {
    var moduloTexto = ModuloTexto()
    var moduloImagen = ModuloImagen()

    var modulosParaServidorWeb = mutableListOf<Modulo>(moduloTexto)

    var servidorWeb = ServidorWeb(modulosParaServidorWeb)
    var fechaYHora = LocalDateTime.of(21, 6, 22, 15, 30, 50)

    var unPedido = Pedido("192.168.100.23", "http://pepito.com.ar/documentos/doc1.html", fechaYHora)
    var pedidoMalo = Pedido("192.168.100.23", "https://pepito.com.ar/documentos/doc1.html", fechaYHora)
    var pedidoMaloDos = Pedido("192.168.100.23", "http://pepito.com.ar/documentos/doc1.rar", fechaYHora)

    servidorWeb.agregarModulo(moduloImagen)

    describe("Requerimiento 1"){
      var respuestaDelServidor = servidorWeb.generarRespuesta(unPedido)

      respuestaDelServidor.codigo.shouldBe(CodigoHttp.OK)
      respuestaDelServidor.body.shouldBe("este es el cuerpo de la respuesta")
      respuestaDelServidor.tiempo.shouldBe(15)

      var respuestaMala = servidorWeb.generarRespuesta(pedidoMalo)

      respuestaMala.codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)

    }

    describe("Requerimiento 2"){
      servidorWeb.generarRespuesta(unPedido).body.shouldBe("Esto es un texto")
      servidorWeb.generarRespuesta(unPedido).codigo.shouldBe(CodigoHttp.OK)
      servidorWeb.generarRespuesta(unPedido).tiempo.shouldBe(15)

      servidorWeb.generarRespuesta(pedidoMalo).body.shouldBe(" ")
      servidorWeb.generarRespuesta(pedidoMalo).codigo.shouldBe(CodigoHttp.NOT_IMPLEMENTED)
      servidorWeb.generarRespuesta(pedidoMalo).tiempo.shouldBe(10)

      servidorWeb.generarRespuesta(pedidoMaloDos).body.shouldBe(" ")
      servidorWeb.generarRespuesta(pedidoMaloDos).codigo.shouldBe(CodigoHttp.NOT_FOUND)
      servidorWeb.generarRespuesta(pedidoMaloDos).tiempo.shouldBe(10)

    }

  }
})
