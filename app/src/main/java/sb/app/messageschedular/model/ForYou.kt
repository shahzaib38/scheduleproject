package sb.app.messageschedular.model

open class ForYou(
    var id:Int   ,
     var   name : String,
             var navigate :Boolean
)



//
//
//data class Language(override var id: Int ,
//                    override var name: String,
//                    override var navigate: Boolean =true
//                    ):ForYou
//
//data class Suggestions(override var id: Int ,
//                      override var name: String,
//                       override var navigate: Boolean =false):ForYou
//
//data class PopularApps(override var id: Int ,
//                      override var name: String,
//                       override var navigate: Boolean =false
//                       ):ForYou
//
//data class PremiumApps(override var id: Int ,
//                      override var name: String,
//                      override var navigate: Boolean =true
//
//                      ):ForYou
//
//data class RecommendedApps(override var id: Int ,
//                           override var name: String,
//                           override var navigate: Boolean =true
//                           ):ForYou
//
//data class Communication(override var id: Int,
//                         override var name: String,
//                         override var navigate: Boolean =true):ForYou