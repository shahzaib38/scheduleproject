package sb.app.messageschedular.array

import sb.app.messageschedular.model.Todo

object ArrayUtils {

    fun removeItem(position :Int ,list:List<Todo>):List<Todo>{
        val newArray:Array<Todo> = Array<Todo>(list.size-1){Todo("")}

        var idx =0
        for(i:Int in list.indices step 1 ){
            if(i==position)continue
            newArray[idx++]=list[i] }

        return newArray.toList() }

}