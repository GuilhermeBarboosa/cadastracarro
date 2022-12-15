package com.pdm.cadastracarro

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.estudo.provapdm.model.Veiculo
import com.estudo.provapdm.model.enum.TipoVeiculo
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
                "",
                0.0,
                TipoVeiculo.HATCH,
                false
            )
        )
    }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    val models = enumValues<TipoVeiculo>().toList()
    var selectedName by rememberSaveable() {
        mutableStateOf(TipoVeiculo.TRUCK)
    }
    val mContext = LocalContext.current

    val vehiclesList = remember {
        mutableStateListOf<Veiculo>()
    }
    var vehicleCreate by remember { mutableStateOf(Veiculo("", 0.0, TipoVeiculo.TRUCK, false)) }
    val focusManger = LocalFocusManager.current
    var value = 1;

    CadastracarroTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.onPrimary
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row() {
                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Model") },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManger.clearFocus()
                        }),
                    )
                }

                Row() {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price") },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManger.clearFocus()
                        }),
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Row {

                        Spinner(
                            itemList = models,
                            selectedItem = selectedName,
                            onItemSelected = { selectedName = it })
                    }
                }

                Row(modifier = Modifier.padding(10.dp)) {
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
//                            vehicleCreate = !!



                            if(registerVehicle(model, price, selectedName, mContext) != null){
                                vehiclesList.add(registerVehicle(model, price, selectedName, mContext)!!);
                            }
                        },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        )
                    ) {
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Add")
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }

                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    VeiculoList(vehiclesList) { veiculo ->
                        veiculoDisplayed = veiculo
                    }
                }

            }
        }
    }
}

fun registerVehicle(model: TextFieldValue, price: TextFieldValue, selectedName: TipoVeiculo, mContext: Context): Veiculo? {

    //FAZER VERIFICACAO FUNCIONAL
    if(model.text == ""){
        Toast.makeText(mContext, "Model empty", Toast.LENGTH_LONG).show()
        return null;
    }else if(price.text == ""){
        Toast.makeText(mContext, "Price empty", Toast.LENGTH_LONG).show()
        return null;
    }else if(model.text != "" && price.text != ""){
        var objectVeiculo = Veiculo(model.text, price.text.toDouble(), selectedName, false);
        return objectVeiculo;
    }
    return null;
}

@Composable
fun Spinner(
    itemList: List<TipoVeiculo>,
    selectedItem: TipoVeiculo,
    onItemSelected: (selectedItem: TipoVeiculo) -> Unit
) {
    var expanded by rememberSaveable() {
        mutableStateOf(false)
    }

    OutlinedButton(modifier = Modifier
        .padding(10.dp)
        .border(0.dp, Color.Black, shape = RoundedCornerShape(3.dp)), onClick = {
        expanded = true
    }) {
        Text(
            text = selectedItem.descricao,
            style = TextStyle(Color.Black),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(0.dp)
        )
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
    }

    DropdownMenu(expanded = expanded, onDismissRequest = {
        expanded = false
    }) {

        itemList.forEach {
            DropdownMenuItem(onClick = {
                expanded = false
                onItemSelected(it)
            }) {
                Text(text = it.descricao, style = TextStyle(Color.Black))
            }
        }
    }
}

@Composable
fun VehicleCard(veiculo: Veiculo) {
    var expandDetails by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                expandDetails = !expandDetails
            },
        elevation = 4.dp
    ) {

        Column( modifier = Modifier.background(Color.Green).height(10.dp)) {
            Text(text = "   ")
        }

        Row{


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        expandDetails = !expandDetails
                    }
            ) {
                Text(
                    text = "Model: " + veiculo.model,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .widthIn(0.dp, 250.dp)
                        .clickable {
                            expandDetails = !expandDetails
                        }
                )
                Row(
                    modifier =
                    Modifier.align(Alignment.CenterHorizontally)
                ) {
                }
                AnimatedVisibility(
                    visible = expandDetails,
                    enter = fadeIn(initialAlpha = 0f) + expandVertically(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 250)) + shrinkVertically()
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.description_text,
                            veiculo.model, veiculo.price,
                            veiculo.type.descricao, veiculo.sold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }



    }
}


@Composable
fun VeiculoList(veiculos: List<Veiculo>, onClick: (veiculo: Veiculo) -> Unit) {
    LazyColumn {
        items(veiculos) { veiculo ->
            VehicleCard(veiculo)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BuildLayout()
}

