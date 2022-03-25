package sb.app.messageschedular.model
//
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable

//@Serializable
data class UnSplashImage(
                        val  id :String
                        ,
                         val urls :Url

                         )


//@Serializable
data class Url(
   // @SerialName("regular")
    val regular:String,
    //@SerialName("small")
    val small:String,
    //@SerialName("thumb")
    val thumb:String)