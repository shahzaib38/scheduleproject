package sb.app.messageschedular.ext

import android.content.Context
import android.graphics.*
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class ItemTouchHelper(private var context: Context ,
                      private var recyclerView: RecyclerView)  : ItemTouchHelper.SimpleCallback( ItemTouchHelper.ACTION_STATE_IDLE,



                                                                           ItemTouchHelper.LEFT) {


    private val BUTTON_WIDTH: Int =10
    private val gesture: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                for (underlayButton in buttons!!) {

                    if (underlayButton.onClick(e!!.x, e.y))
                        break
                }


                return true
            }


        }


    private  var onTouchListener = object : View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            val e =event
            if(swipePosition<0  )return false

            println("Get touch x"+event?.rawX!!.toInt() + "GetY"+event?.rawY!!.toInt())
            val point = Point(event?.rawX!!.toInt(), event?.rawY!!.toInt())

        val swipedViewHolder =    recyclerView.findViewHolderForAdapterPosition(swipePosition)
        val swipedItem =   swipedViewHolder!!.itemView
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)

            if(event.action  == MotionEvent.ACTION_DOWN || event.action ==MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE)
            {

                println("Ontouched")
                if(rect.top < point.y && rect.bottom > point.y){


                    gestureDetector.onTouchEvent(e)

                }else{

                    recoverQueue.add(swipePosition)
                    swipePosition =-1
                    recoverSwipedItem()
                }

            }


            return false
        }


    }



    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

println("Move listener")
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

     val pos =   viewHolder.bindingAdapterPosition
        println("Swiped "+pos )

        if(swipePosition!=pos){

            println("swiped position"+swipePosition)
            recoverQueue.add(swipePosition)
        }
        swipePosition =pos


        if(buttonBuffers.containsKey(swipePosition)){

        buttons  =      buttonBuffers.get(swipePosition)!!


        }else {

            buttons.clear()
        }

        buttonBuffers.clear()
        val BUTTON_WIDTH = 1
        swipeThreshold = 0.5f * buttons!!.size * BUTTON_WIDTH;
        recoverSwipedItem()
    }

    private fun recoverSwipedItem() {
        if(!recoverQueue.isEmpty()){

          val pos =  recoverQueue.poll()
             if(pos>-1){
                 recyclerView.adapter?.notifyItemChanged(pos)

             }

        }

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

    var pos =     viewHolder.bindingAdapterPosition
        if(pos<0){
            swipePosition =pos
            return }
        var translationX = dX

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            if(dX<0){

                var buffer :MutableList<UnderlayButton> = ArrayList()

                if(!buttonBuffers.containsKey(pos)){


                    println("position "+pos )
                    buttonBuffers.put(pos , buffer)
                }else{

                    println("Bbuffer"+pos )
                    buffer = buttonBuffers[pos]!!
                }



                println("buffer size"+buffer.size)



                translationX = dX *buttonBuffers.size *BUTTON_WIDTH /viewHolder.itemView.width


                drawButton(c, viewHolder.itemView, buffer,pos ,translationX)
            }




        }




        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {

        return swipeThreshold
    }

    private var gestureDetector = GestureDetector(context, gesture)

    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private var recoverQueue: Queue<Int> = LinkedList()
    private var buttonBuffers: MutableMap<Int, MutableList<UnderlayButton>> = HashMap()

    init {
        this.recyclerView.setOnTouchListener(onTouchListener)


        val  itemTouchHelper =ItemTouchHelper(this )

        itemTouchHelper.attachToRecyclerView(this.recyclerView)


    }


    private var buttons: MutableList<UnderlayButton> = ArrayList()


    class UnderlayButton(
        private var text: String,
        private var imageIntRes: Int,
        private var color: Int,
        private var underlayButtonClickListener: UnderlayButtonClickListener
    ) {


        private var pos: Int = 0
        private var clickRegion: RectF? = null


        fun onClick(x: Float, y: Float): Boolean {

            if (clickRegion != null && clickRegion!!.contains(x, y)) {
                underlayButtonClickListener.onClick(pos)
                return true
            }


            return false
        }


        fun onDraw(canvas: Canvas, rectF: RectF, pos: Int) {
            val paint = Paint()
            paint.color = color
            canvas.drawRect(rectF, paint)


        }


    }

    interface UnderlayButtonClickListener {


        fun onClick(pos: Int)

    }


   private  fun drawButton(c:Canvas , itemView: View , buffer : List<UnderlayButton> ,pos :Int , dx :Float ){

        val right = itemView.right
       val dButtonWidth: Float = -1 * dx / buffer.size

       for(button  in  buffer){

           val left = right -dButtonWidth

           println("Left"+left)




       }



    }

}


@BindingAdapter("app:check")
fun setCheck(radioCheck: AppCompatRadioButton, ischeck: Boolean?) {

    println("Is check"+ischeck)
    if(ischeck!!){
        radioCheck.visibility =View.VISIBLE!!

    }else{
        radioCheck.visibility =View.GONE


    }


}