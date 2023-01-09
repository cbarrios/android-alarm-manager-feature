package com.lalosapps.alarmmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lalosapps.alarmmanager.data.AlarmItem
import com.lalosapps.alarmmanager.data.AndroidAlarmScheduler
import com.lalosapps.alarmmanager.ui.theme.AlarmManagerTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scheduler = AndroidAlarmScheduler(this)
        var alarmItem: AlarmItem? = null
        setContent {
            AlarmManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var secondsText by remember {
                        mutableStateOf("")
                    }
                    var message by remember {
                        mutableStateOf("")
                    }
                    var secondsError by remember {
                        mutableStateOf(false)
                    }
                    val focusManager = LocalFocusManager.current
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = secondsText,
                            onValueChange = { secondsText = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(text = "Trigger alarm in seconds")
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = secondsError
                        )
                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(text = "Message")
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = {
                                try {
                                    alarmItem = AlarmItem(
                                        time = LocalDateTime.now()
                                            .plusSeconds(secondsText.toLong()),
                                        message = message
                                    )
                                    alarmItem?.let(scheduler::schedule)
                                    secondsText = ""
                                    message = ""
                                    secondsError = false
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    secondsError = true
                                }
                            }) {
                                Text(text = "Schedule")
                            }
                            Button(onClick = {
                                alarmItem?.let(scheduler::cancel)
                            }) {
                                Text(text = "Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}