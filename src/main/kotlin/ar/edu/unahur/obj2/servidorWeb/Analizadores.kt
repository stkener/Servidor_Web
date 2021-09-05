package ar.edu.unahur.obj2.servidorWeb

import kotlin.collections.List as List

class Analizador(val demora: Int){
    var listaRespuestas = mutableListOf<RespuestaYModulo>()

    fun recibirRespuesta(respuesta: Respuesta, modulo: Modulo){
        var RYM = RespuestaYModulo(respuesta, modulo)

        listaRespuestas.add(RYM)
    }

    fun estaDemorada(unModulo: Modulo) = unModulo.tiempo < demora

    fun todosLosModulos(unModulo: Modulo): List<RespuestaYModulo> {
        var modulosDeUnTipo = listaRespuestas.filter { it.modulo == unModulo }

        return modulosDeUnTipo
    }

    fun cantidadDeDemoradas(unModulo: Modulo): Int{
        var totalDemoradas = 0

        todosLosModulos(unModulo).forEach { if(this.estaDemorada(it.modulo)){ totalDemoradas = totalDemoradas + 1} }

        return totalDemoradas
    }

}

class RespuestaYModulo(var respuesta: Respuesta, var modulo: Modulo)