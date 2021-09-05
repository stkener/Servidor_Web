package ar.edu.unahur.obj2.servidorWeb

import java.net.URL
import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime)
class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido)


class ServidorWeb(var modulos: MutableList<Modulo>){

  var analizadores = mutableListOf<Analizador>()


//////////////////////////////////////////////////////////FUNCIONES MODULO

  fun agregarModulo(unModulo: Modulo){
    if(!modulos.contains(unModulo)){ modulos.add(unModulo) }
    else { println("El modulo ya esta cargado en el servidor")}
  }

  fun sacarModulo(unModulo: Modulo){
    if(modulos.contains(unModulo)){ modulos.remove(unModulo) }
    else { println("El modulo no se encuentra en el servidor")}
  }

////////////////////////////////////////////////////////////////FUNCIONES ANALIZADORES
  fun agregarAnalizador(unAnalizador: Analizador){
    if(!analizadores.contains(unAnalizador)){ analizadores.add(unAnalizador) }
    else { println("El analizador ya esta cargado en el servidor")}
  }

  fun sacarAnalizador(unAnalizador: Analizador){
    if(analizadores.contains(unAnalizador)){ analizadores.remove(unAnalizador) }
    else { println("El analizador no se encuentra en el servidor")}
  }

///////////////////////////////////////////////////////////////////FUNCIONES INTERNAS
  fun algunModuloPuedeResponder(pedido: Pedido): Boolean{

    return modulos.any { it.extensiones.contains(this.extensionArchivo(pedido)) }
  }

  fun moduloQueRespondeA(pedido: Pedido): Nothing? {
      var modulo = null
       if(this.algunModuloPuedeResponder(pedido)){modulo = modulos.find { this.algunModuloPuedeResponder(pedido) } as Nothing?
       }

    return modulo

  }

  fun cuerpoRespuesta(pedido: Pedido): String{
    var cuerpo = " "
    if (this.algunModuloPuedeResponder(pedido)){
      cuerpo = (modulos.find { this.algunModuloPuedeResponder(pedido) })!!.retorna
    }
    return cuerpo
  }

  fun tiempoRespuesta(pedido: Pedido): Int {
    var tiempo = 10
    if (this.algunModuloPuedeResponder(pedido)){
      tiempo = (modulos.find { this.algunModuloPuedeResponder(pedido) })!!.tiempo
    }
    return tiempo
  }

  fun generarCodigoRespuesta(pedido: Pedido): CodigoHttp{
    var codigo = when {

      !esHTTP(pedido) -> CodigoHttp.NOT_IMPLEMENTED
      this.algunModuloPuedeResponder(pedido) -> CodigoHttp.OK
      else -> CodigoHttp.NOT_FOUND
    }
    return codigo
  }

  fun generarRespuesta(pedido: Pedido): Respuesta{
    val codigoRespuesta = this.generarCodigoRespuesta(pedido)

    val cuerpo = this.cuerpoRespuesta(pedido)

    val tiempo = this.tiempoRespuesta(pedido)

    var respuesta = Respuesta(codigoRespuesta, cuerpo, tiempo, pedido)

    return respuesta
  }



  fun enviarRespuestaAnalizadores(respuesta: Respuesta, modulo: Modulo){

    analizadores.forEach { a -> a.recibirRespuesta(respuesta, modulo) }
  }



////////////////////////////////////////////////////////VERIFICADORES URL
  fun extensionArchivo(pedido: Pedido): String{
    var partesUrl = pedido.url.split(".")

    return partesUrl.last()
  }

  fun esHTTP(pedido: Pedido): Boolean{
    var miUrl = URL(pedido.url)

    return miUrl.protocol == "http"
  }
/////////////////////////////////////////////////////////FUNCIONES EXTERNAS

fun atenderPedido(pedido: Pedido){
  var unaRespuesta = this.generarRespuesta(pedido)
  var modulo = this.moduloQueRespondeA(pedido)

  if(unaRespuesta.codigo == CodigoHttp.OK){

  }

  if(modulo != null) this.enviarRespuestaAnalizadores(unaRespuesta, modulo)
}

}