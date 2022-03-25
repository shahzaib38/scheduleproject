package sb.app.messageschedular.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import sb.app.messageschedular.api.UnSplashApi
import sb.app.messageschedular.database.SmsDatabase
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.model.UnSplashImage

//class MessagePagingSource(private val smsDatabase: SmsDatabase) : PagingSource<Int ,Sms>() {
//
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Sms> {
//
//
//        return   try {
//
//            var key :Int =   params.key?:1
//
//            println("Message paging source"+key )
//
//            val respose = smsDatabase.smsDao().getMessageList(params.loadSize)
//          val prevKey =if(key==1) null  else key-1
//          val afterKey = if(respose.isEmpty()) null else key+1
//
//          LoadResult.Page(data = respose, prevKey = prevKey, nextKey = afterKey)
//      } catch (e :Exception){
//
//          println("catched" +e.message)
//          LoadResult.Error(e) }
//
//
//
//    }
//
//
//    override fun getRefreshKey(state: PagingState<Int, Sms>): Int? {
//       return  state.anchorPosition
//    }
//
//
//}