package com.example.mortgagecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mortgagecalculator.ui.theme.MortgageCalculatorTheme
import java.text.NumberFormat
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MortgageCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MortgageCalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun MortgageCalculatorLayout() {

    var amountInput by remember { mutableStateOf("") }
    var rateInput by remember { mutableStateOf("") }
    var yearsInput by remember { mutableStateOf("") }


    val amount = amountInput.toIntOrNull() ?: 0
    val rate = rateInput.toDoubleOrNull() ?: 0.0
    val years = yearsInput.toIntOrNull() ?: 0


    val payment = calculateMortgage(amount, rate, years)

    Column(
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )

        EditNumberField(
            label = R.string.amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())

        EditNumberField(
            label = R.string.rate,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = rateInput,
            onValueChange = { rateInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())

        EditNumberField(
            label = R.string.years,
            leadingIcon = R.drawable.calendar,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = yearsInput,
            onValueChange = { yearsInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth())

        Text(
            text = stringResource(id = R.string.payment, payment),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier)
{
    TextField(
        value = value,
        label = { Text(stringResource(label))},
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), contentDescription = null) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        modifier = modifier
    )

}

private fun calculateMortgage(p: Int, r: Double, n: Int): String {
    var mortgage = p * ( (r*(1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1))
    if (mortgage.isNaN() || mortgage.isInfinite()) mortgage = 0.0
    return  NumberFormat.getCurrencyInstance().format(mortgage)
}

@Preview(showBackground = true)
@Composable
fun MortgageCalculatorPreview(){
    MortgageCalculatorTheme {
        MortgageCalculatorLayout()
    }
}