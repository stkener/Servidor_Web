package ar.edu.unahur.obj2.servidorWeb

abstract class Modulo(){
    open var extensiones= mutableListOf<String>()

    fun agregarExtension(extension: String){
        if(!extensiones.contains(extension)){ extensiones.add(extension) }
        }

    fun removerExtension(extension: String){
        if(extensiones.contains(extension)){ extensiones.remove(extension) }
    }

    abstract var retorna: String

    abstract var tiempo: Int
}

class ModuloTexto(): Modulo() {
    override var extensiones = mutableListOf<String>("txt", "doc", "docx", "odt", "html")

    override var retorna = "Esto es un texto"

    override var tiempo = 15
}

class ModuloImagen(): Modulo() {
    override var extensiones = mutableListOf<String>("jpg", "jpeg", "png", "gif")

    override var retorna = "Esto es una imagen"

    override var tiempo = 20
}