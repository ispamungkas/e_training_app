package com.maspam.etrain.training.core.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronUp
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide
import com.maspam.etrain.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    valueInput: String? = "",
    readOnly: Boolean = false,
    label: String,
    errorMessage: String? = "",
    hint: String? = "",
    onCompletedText: (String) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val textFieldIsFocused = interactionSource.collectIsFocusedAsState()
    val value = remember { mutableStateOf(valueInput ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            value = value.value,
            readOnly = readOnly,
            colors = AppTextInputColors,
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(
                    text = hint ?: label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    )
                )
            },
            onValueChange = {
                value.value = it
                onCompletedText(it)
            },
            label = {
                val style =
                    if (textFieldIsFocused.value) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light
                    )
                Text(text = label, style = style)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onCompletedText(value.value)
                }
            )
        )
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
fun CustomTextPasswordField(
    modifier: Modifier = Modifier,
    label: String,
    errorMessage: String? = "",
    hint: String? = "",
    onCompletedText: (String) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val textFieldIsFocused = interactionSource.collectIsFocusedAsState()
    val value = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    var hidePassword by remember { mutableStateOf(true) }

    val visualTransformation =
        if (hidePassword) PasswordVisualTransformation() else VisualTransformation.None
    val trailingIcon = if (!hidePassword) Lucide.EyeOff else Lucide.Eye

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            value = value.value,
            colors = AppTextInputColors,
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(
                    text = hint ?: label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    )
                )
            },
            onValueChange = {
                value.value = it
                onCompletedText(it)
            },
            label = {
                val style =
                    if (textFieldIsFocused.value) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Light
                    )
                Text(text = label, style = style)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onCompletedText(value.value)
                }
            ),
            trailingIcon = {
                IconButton(onClick = { hidePassword = !hidePassword }) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = if (hidePassword) "Show password" else "Hide password"
                    )
                }
            },
            visualTransformation = visualTransformation
        )
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
fun CustomNumberTextField(
    modifier: Modifier = Modifier,
    label: String,
    errorMessage: String? = "",
    hint: String? = "",
    valueInput: String? = "",
    onCompletedText: (String) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val textFieldIsFocused = interactionSource.collectIsFocusedAsState()
    var value by remember { mutableStateOf(valueInput ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            value = value,
            colors = AppTextInputColors,
            shape = RoundedCornerShape(10.dp),
            placeholder = {
                Text(
                    text = hint ?: label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                    )
                )
            },
            onValueChange = {
                value = it
                onCompletedText(it)
            },
            label = {
                val style =
                    if (textFieldIsFocused.value) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium
                Text(text = label, style = style)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onCompletedText(value)
                },
            )
        )
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
fun CustomEmailTextField(
    modifier: Modifier = Modifier,
    valueInput: String? = "",
    label: String,
    onCompletedText: (String) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val textFieldIsFocused = interactionSource.collectIsFocusedAsState()
    val value = remember { mutableStateOf(valueInput ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        interactionSource = interactionSource,
        modifier = modifier,
        value = value.value,
        colors = AppTextInputColors,
        shape = RoundedCornerShape(10.dp),
        onValueChange = {
            value.value = it
        },
        placeholder = {
            Text(
                text = stringResource(R.string.input_email),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                )
            )
        },
        label = {
            val style =
                if (textFieldIsFocused.value) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium
            Text(text = label, style = style)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        isError = value.value.isEmpty() && value.value.contains("@.com"),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onCompletedText(value.value)
            }
        )
    )
}

@Composable
inline fun <reified T : Enum<T>> CustomFormChoice(
    label: String,
    initialValue: String? = "",
    crossinline onReceive: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isOpen by remember { mutableStateOf(false) }
    var position by remember { mutableIntStateOf(0) }
    val listData = enumValues<T>().map {
        it.javaClass.getMethod("getValue").invoke(it) as? String ?: it.name
    }
    var value by remember { mutableStateOf(initialValue) }

    Box(
        modifier = modifier
            .clickable {
                isOpen = true
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value ?: listData[0],
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Normal
                    )
                )
                Icon(
                    imageVector = if (!isOpen) Lucide.ChevronUp else Lucide.ChevronDown,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = isOpen,
            onDismissRequest = {
                isOpen = !isOpen
            }
        ) {
            listData.forEachIndexed { index, data ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = data,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    },
                    onClick = {
                        position = index
                        if (position > 0) {
                            value = listData[position]
                            onReceive(listData[position])
                        }
                        isOpen = !isOpen
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerField(
    value: String? = "",
    label: String,
    onCLick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable {
                onCLick()
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value ?: stringResource(R.string.input_date),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Normal
                    )
                )
                Icon(
                    imageVector = Lucide.ChevronUp,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


private val AppTextInputColors: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorBorderColor = MaterialTheme.colorScheme.onBackground,
        errorTextColor = MaterialTheme.colorScheme.onBackground,
        errorLeadingIconColor = MaterialTheme.colorScheme.onBackground,
        errorTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorLabelColor = MaterialTheme.colorScheme.onBackground,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
    )
