package com.estudo.provapdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.estudo.provapdm.model.Veiculo
import com.estudo.provapdm.model.enum.TipoVeiculo
import com.pdm.cadastracarro.model.ExposedDropMenuStateHolder
import com.pdm.cadastracarro.model.rememberExposedMenuStateHolder
import com.pdm.cadastracarro.ui.theme.CadastracarroTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildLayout()
        }
    }
}

@Composable
fun BuildLayout() {
    var veiculoDisplayed by remember {
        mutableStateOf(
            Veiculo(
                "Empty...",
                0.0,
                TipoVeiculo.TRUCK,
                false
            )
        )
    }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var stateHolder = rememberExposedMenuStateHolder()

    CadastracarroTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {


            Column(modifier = Modifier.fillMaxWidth()) {

                Row() {

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = model,
                        label = { Text(text = "Model") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { it ->
                            model = it
                        }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = price,
                        label = { Text(text = "Price") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { it ->
                            price = it
                        }
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    ExposedDropdownMenu(stateHolder = stateHolder)
                }


            }

        }
    }
}

//@Composable
//fun ProfileCard(contact: Veiculo) {
//    var expandDetails by remember { mutableStateOf(false) }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(4.dp),
//        elevation = 4.dp
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            ImageCard(100.dp, Modifier.align(Alignment.CenterHorizontally))
//            Row(
//                modifier =
//                Modifier.align(Alignment.CenterHorizontally)
//            ) {
//                Text(
//                    text = contact.name,
//                    textAlign = TextAlign.Center,
//                    fontSize = 24.sp,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1,
//                    modifier = Modifier.widthIn(0.dp, 250.dp)
//                )
//                Text(
//                    text = if(expandDetails) "–" else "+",
//                    textAlign = TextAlign.Center,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF6E6E6E),
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp)
//                        .clickable {
//                            expandDetails = !expandDetails
//                        }
//                )
//            }
//            AnimatedVisibility(
//                visible = expandDetails,
//                enter = fadeIn(initialAlpha = 0f) + expandVertically(),
//                exit = fadeOut(animationSpec = tween(durationMillis = 250)) + shrinkVertically()
//            ) {
//                Text(
//                    text = stringResource(
//                        id = R.string.description_text,
//                        contact.name, contact.phone,
//                        contact.address, contact.description
//                    ),
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        }
//    }
//}


@Composable
fun ContactItemView(veiculo: Veiculo, onClick: () -> Unit) {
    Card(
        modifier =
        Modifier
            .padding(8.dp)
            .fillMaxWidth(),
//            .clickable {
//                onClick()
//            },
        elevation = 2.dp
    ) {
        Row(
            modifier =
            Modifier
                .padding(8.dp)
        ) {
            Text(
                text = veiculo.model,
                fontSize = 20.sp,
                modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ContactList(veiculos: List<Veiculo>, onClick: (veiculo: Veiculo) -> Unit) {
    LazyColumn {
        items(veiculos) { veiculo ->
            ContactItemView(veiculo) {
                onClick(veiculo)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BuildLayout()
}

@Composable
fun ExposedDropdownMenu(stateHolder: ExposedDropMenuStateHolder) {
    Column {
        Box {
            OutlinedTextField(
                value = stateHolder.value,
                onValueChange = {},
                label = { Text(text = "Label") },
                trailingIcon = {
                    Icon(painter = painterResource(id = stateHolder.icon),
                        contentDescription = null,
                        Modifier.clickable { stateHolder.onEnabled(!(stateHolder.enabled)) })
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