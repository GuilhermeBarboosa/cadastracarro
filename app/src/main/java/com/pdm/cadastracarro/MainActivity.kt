package com.pdm.cadastracarro

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.estudo.provapdm.model.Veiculo
import com.estudo.provapdm.model.enum.TipoVeiculo
import com.pdm.cadastracarro.ui.theme.CadastracarroTheme
import java.text.NumberFormat
import java.util.*


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


    CadastracarroTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.onPrimary
        ) {
             MainScreen()
        }
    }
}

@Composable
public fun viewScreen(vehiclesList: SnapshotStateList<Veiculo>) {

    var veiculoDisplayed by remember {
        mutableStateOf(
            Veiculo(
                "",
                0.0,
                0,
                TipoVeiculo.HATCH,
                false
            )
        )
    }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(("")) }
    val models = enumValues<TipoVeiculo>().toList()
    var selectedName by rememberSaveable() {
        mutableStateOf(TipoVeiculo.TRUCK)
    }
    val mContext = LocalContext.current
    val focusManger = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row() {
            OutlinedTextField(
                onValueChange = { model = it },
                label = { Text("Model") },
                value = model,
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
                onValueChange = { year = it },
                label = { Text("Year") },
                value = year,
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

        Row() {
            OutlinedTextField(
                label = { Text("Price") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                value = price,
                onValueChange = { newText: String ->
                    if (newText.length <= Long.MAX_VALUE.toString().length && newText.isDigitsOnly()) {
                        price = newText
                        //onValueChange(newText)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                visualTransformation = NumberCommaTransformation(),
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
                    if (registerVehicle(model, price, year, selectedName, mContext) != null) {
                        vehiclesList.add(
                            registerVehicle(
                                model,
                                price,
                                year,
                                selectedName,
                                mContext
                            )!!
                        );
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

@Composable
fun statisticsScreen(vehiclesList: SnapshotStateList<Veiculo>) {
    var sold  = 0;
    var available  = 0;

        vehiclesList.forEach { veiculo ->
            if(veiculo.sold){
                sold += 1;
            }else if(!veiculo.sold){
                available+=1;
            }
        }

        Column() {
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(5.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Cars sold: ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                            ) {
                                append(" " + sold)
                            }
                        }
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(5.dp),
                elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Available cars")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                            ) {
                                append(" " + available)
                            }
                        }
                    )
                }
            }
        }
}

fun registerVehicle(
    model: TextFieldValue,
    price: String,
    year: TextFieldValue,
    selectedName: TipoVeiculo,
    mContext: Context
): Veiculo? {
    if (model.text == "") {
        Toast.makeText(mContext, "Model empty", Toast.LENGTH_LONG).show()
        return null;
    } else if (price == "") {
        Toast.makeText(mContext, "Price empty", Toast.LENGTH_LONG).show()
        return null;
    }else if (year.text == "") {
        Toast.makeText(mContext, "Year empty", Toast.LENGTH_LONG).show()
        return null;
    } else if (model.text != "" && price != "" && year.text != "") {
        var objectVeiculo = Veiculo(model.text, price.toDouble(), year.text.toInt(), selectedName, false);
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


// format long to 123,456,789,9
class NumberCommaTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return text.text.toLongOrNull().formatWithComma().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }
}

fun Long?.formatWithComma(): String =
    NumberFormat.getNumberInstance(Locale.US).format(this ?: 0)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VehicleCard(veiculo: Veiculo) {
    var expandDetails by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf(Color.Green) }

    if (!veiculo.sold) {
        color = Color.Green;
    } else {
        color = Color.Red;
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    expandDetails = !expandDetails
                },
                onLongClick = {
                    if (veiculo.sold) {
                        veiculo.setSold(false);
                    } else {
                        veiculo.setSold(true);
                    }
                }
            ),
        elevation = 4.dp
    ) {

        Column( modifier = Modifier
            .background(
                if (veiculo.sold) {
                    Color.Red
                } else {
                    Color.Green
                }
            )
            .height(10.dp)) {
            Text(text = "   ")
        }

        Row {
            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .combinedClickable(
                        onClick = {
                            expandDetails = !expandDetails
                        },
                        onLongClick = {
                            if (veiculo.sold) {
                                veiculo.setSold(false);
                            } else {
                                veiculo.setSold(true);
                            }
                        }
                    )

            ) {
                Text(
                    text = "Model: " + veiculo.model,
                    style =   if (veiculo.sold) {
                        TextStyle(textDecoration = TextDecoration.LineThrough)
                    } else {
                        TextStyle(textDecoration = TextDecoration.None)
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .widthIn(0.dp, 250.dp)
                        .combinedClickable(
                            onClick = {
                                expandDetails = !expandDetails
                            },
                            onLongClick = {
                                if (veiculo.sold) {
                                    veiculo.setSold(false);
                                } else {
                                    veiculo.setSold(true);
                                }
                            }
                        )
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
                            veiculo.model,
                            veiculo.year,
                            "R$" + veiculo.price,
                            veiculo.type.descricao,

                            if (veiculo.sold) {
                                "Yes"
                            } else {
                                "No"
                            }

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

