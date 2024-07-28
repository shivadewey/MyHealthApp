package com.shiva.myhealth.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Woman
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.shiva.myhealth.R
import com.shiva.myhealth.data.Person
import com.shiva.myhealth.models.AccountViewModel
import com.shiva.myhealth.ui.theme.MyHealthTheme
import com.shiva.myhealth.utility.parseInt
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Account(
    model: AccountViewModel = hiltViewModel()
) {
    val registrationDialog = model.registrationDialog.observeAsState()
    val person = model.person.observeAsState()
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        if (registrationDialog.value == true) {
            RegistrationDialog(model::showRegDialog, model::setPersonDate)
        } else {
            AccountSection(person)
            Button({
                model.inSystem = MutableStateFlow(false)
                model.person.value?.id?.let { model.delPerson(it) }
                model.showRegDialog()
            }) {
                Text(stringResource(R.string.del_btn))
            }
        }
    }


}

@Composable
fun AccountSection(person: State<Person?>) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = .50f
                ), shape = RoundedCornerShape(8.dp)
            ), horizontalArrangement = Arrangement.Center
    ) {
        val icon =
            remember { mutableStateOf(if (person.value?.sex == "Male") Icons.Default.Man else Icons.Default.Woman) }
        Icon(icon.value, "", Modifier.size(128.dp))

        Column(Modifier.padding(8.dp)) {
            person.value?.name?.let {
                OutlinedTextField(
                    it,
                    {},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = false,
                    label = { Text(stringResource(R.string.name)) })
            } // имя

            OutlinedTextField(person.value?.age.toString(),
                {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = false,
                label = { Text(stringResource(R.string.Age)) }) // возраст

            OutlinedTextField(person.value?.weight.toString(),
                {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = false,
                label = { Text(stringResource(R.string.weight)) }) // вес

            OutlinedTextField(person.value?.heigth.toString(),
                {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = false,
                label = { Text(stringResource(R.string.height)) }) // рост

            OutlinedTextField(person.value?.caloriesGoal.toString(),
                {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = false,
                label = { Text(stringResource(R.string.goal_calories)) }) // калории

            OutlinedTextField(person.value?.sleepGoal.toString(),
                {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = false,
                label = { Text(stringResource(R.string.goal_sleep)) }) // калории
        }
    }
}

@Composable
fun RegistrationDialog(
    showDialog: (Boolean) -> Unit,
    onAcceptRegistration: (Person) -> Unit,
) {
    val toastShow = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val age = remember { mutableStateOf(0) }
    val sex = remember {
        mutableStateMapOf(
            "Male" to true,
            "Female" to false,
        )
    }
    val weight = remember { mutableStateOf(0) }
    val heigth = remember { mutableStateOf(0) }

    Dialog(
        onDismissRequest = { showDialog(false) }, DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        if (toastShow.value) {
            Toast.makeText(LocalContext.current, R.string.toast_add, Toast.LENGTH_LONG).show()
            toastShow.value = false
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(stringResource(R.string.reg_title), Modifier.padding(8.dp), textAlign = TextAlign.Center)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.sex) + ": ")
                    sex["Male"]?.let {
                        InputChip(
                            it,
                            onClick = {
                                sex["Male"] = true
                                sex["Female"] = false
                            },
                            label = { Text(stringResource(R.string.male)) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    sex["Female"]?.let {
                        InputChip(
                            it,
                            onClick = {
                                sex["Male"] = false
                                sex["Female"] = true
                            },
                            label = { Text(stringResource(R.string.female)) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
                OutlinedTextField(name.value,
                    {
                        if (it != "" && it != "0") {
                            name.value = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = { Text(stringResource(R.string.name)) }) // имя

                OutlinedTextField(age.value.toString(),
                    {
                        if (it != "" && it != "0") {
                            age.value = it.parseInt(it)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.Age)) }) // возраст

                OutlinedTextField(weight.value.toString(),
                    {
                        if (it != "" && it != "0") {
                            weight.value =  it.parseInt(it)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.weight)) }) // вес

                OutlinedTextField(heigth.value.toString(),
                    {
                        if (it != "" && it != "0") {
                            heigth.value =  it.parseInt(it)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(stringResource(R.string.height)) }) // рост

                Button({
                    showDialog(false)
                    onAcceptRegistration(
                        Person(
                            0,
                            name.value,
                            age.value,
                            sex.filter { it.value }.keys.first(),
                            weight.value,
                            heigth.value
                        )
                    )
                }, Modifier.padding(8.dp)) {
                    Text(stringResource(R.string.add_sleep_btn))
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountPreview() {
    MyHealthTheme {
        //Account("Account")
    }
}