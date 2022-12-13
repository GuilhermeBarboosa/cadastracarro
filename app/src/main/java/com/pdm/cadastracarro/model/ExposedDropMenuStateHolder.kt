package com.pdm.cadastracarro.model

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import com.pdm.cadastracarro.R

class ExposedDropMenuStateHolder {

    var enabled by mutableStateOf(false)
    var value by mutableStateOf("")
    var selectedIndex by mutableStateOf(-1)
    var size by mutableStateOf(Size.Zero)
    val icon:Int

    @Composable
    get() = if(enabled){
        R.drawable.more
    }else{
        R.drawable.less
    }

    val itens = (1..5).map{
        "Options $it"
    }

    fun onEnabled(newValue : Boolean){
        enabled = newValue
    }

    fun onSelectedIndex(newValue : Int){
        selectedIndex = newValue
    }

    fun onSize(newValue : Size){
        size= newValue
    }

}

@Composable
fun rememberExposedMenuStateHolder() = remember {
    ExposedDropMenuStateHolder()
}