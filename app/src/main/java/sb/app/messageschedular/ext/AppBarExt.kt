package sb.app.messageschedular.ext

import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.databinding.BindingAdapter
import sb.app.messageschedular.R
import sb.app.messageschedular.enums.ListEnum
import java.nio.file.Files.delete


@BindingAdapter("app:barBarText")
fun appBarText(textView : TextView, isSelected : ListEnum? ){
    if(isSelected==null )return
  val resource =  textView.context.resources
    when(isSelected){
        ListEnum.SELECTALL ->{
            textView.text = resource.getString(R.string.selectall) }
        ListEnum.DELETE ->{
            textView.text =resource.getString(R.string.deleteAll) }
        ListEnum.UNSELECT ->{
            textView.text =resource.getString(R.string.unselect) } }
}


@BindingAdapter("app:barBarColor")
fun appBarColor(textView : TextView, isSelected : ListEnum? ){
    if(isSelected==null )return
    val resource =  textView.context.resources
    when(isSelected){
        ListEnum.SELECTALL ->{
            textView.setTextColor(resource.getColor(R.color.selectAll_color ,null )) }
        ListEnum.DELETE ->{
            textView.setTextColor(resource.getColor(R.color.delete_color ,null )) }
        ListEnum.UNSELECT ->{
            textView.setTextColor(resource.getColor(R.color.selectAll_color ,null ))
        }
    }
}