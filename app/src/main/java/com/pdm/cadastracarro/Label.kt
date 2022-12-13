package com.pdm.cadastracarro;

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize
import com.pdm.cadastracarro.model.ExposedDropMenuStateHolder
import com.pdm.cadastracarro.model.rememberExposedMenuStateHolder

@Composable
fun Label() {
    val stateHolder = rememberExposedMenuStateHolder()
    ExposedDropdownMenu(stateHolder = stateHolder)
}

@Composable
fun ExposedDropdownMenu(stateHolder: ExposedDropMenuStateHolder) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box {
            OutlinedTextField(
                value = stateHolder.value,
                onValueChange = {},
                label = { Text(text = "Label") },
                trailingIcon = {
                    Icon(painter = painterResource(id = stateHolder.icon),
                        contentDescription = null,
                        Modifier.clickable {
                            stateHolder.onEnabled(!(stateHolder.enabled))
                        })
                }, modifier = Modifier.onGloballyPositioned {
                    stateHolder.onSize(it.size.toSize())
                })

            DropdownMenu(expanded = stateHolder.enabled, onDismissRequest = {
                stateHolder.onEnabled(false)
            }, modifier = Modifier.width(with(LocalDensity.current){stateHolder.size.width.toDp()})) {
                stateHolder.itens.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        stateHolder.onSelectedIndex(index)
                        stateHolder.onEnabled(false)
                    }) {
                        Text(text= s)
                    }
                }
            }
        }
    }
}
