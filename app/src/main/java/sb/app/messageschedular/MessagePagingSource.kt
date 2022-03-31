package sb.app.messageschedular


//
//
//class MessagePagingSource(private val smsDatabase: SmsDatabase) : PagingSource<Int ,Sms>() {
//
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 0
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Sms>): Int? {
//
//       return  null }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Sms>{
//
//
//        val position = params.key?: INITIAL_PAGE_INDEX
//
//        val messages = smsDatabase.smsDao().getMessageList(position)
//
//        return LoadResult.page(
//            data =messages ,
//            prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
//            nextKey = if (messages.isNullOrEmpty()) null else position + 1)
//
//    }
//
//
//}