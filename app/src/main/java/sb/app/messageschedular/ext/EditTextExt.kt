package sb.app.messageschedular.ext

import android.os.Build
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import sb.app.messageschedular.R


@BindingAdapter("app:updateWrite")
fun updateEditText(editText: EditText ,itemText:String ){

    if(editText.isFocused && editText.text.isNotEmpty()){


            editText.background = ContextCompat.getDrawable(editText.context, R.drawable.edit_text_background);
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }else if(editText.isFocused && editText.text.isEmpty()){

        editText.background = ContextCompat.getDrawable(editText.context, R.drawable.edit_text_background_empty);
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);

    } else {
        editText.background = ContextCompat.getDrawable(editText.context, R.drawable.edit_text_background);

        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }


}