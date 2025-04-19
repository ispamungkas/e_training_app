package com.maspam.etrain.training.core.presentation.component

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.maspam.etrain.ui.theme.ETrainingTheme

@Composable
fun OtpInputField(
    number: Int?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onBackPress: () -> Unit,
    modifier: Modifier = Modifier
) {

    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    index = if (number != null) 1 else 0
                )
            )
        )
    }

    var isFocus by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
            .background(color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { value ->
                val newValue = value.text
                if (newValue.length <= 1 && newValue.isDigitsOnly()) {
                    onNumberChanged(newValue.toIntOrNull())
                }
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            textStyle = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .padding(20.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocus = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent { event ->
                    val didPress = event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if (didPress && number == null) {
                        onBackPress()
                    }
                    false
                },
            decorationBox = { innerBox ->
                innerBox()
                if (!isFocus && number == null) {
                    Text(
                        text = "-",
                        style = TextStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }

            }
        )
    }

}

@Preview
@Composable
private fun OtpInputFieldPrev() {
    ETrainingTheme {
        OtpInputField(
            number = null,
            focusRequester = remember { FocusRequester() },
            onFocusChanged = { value -> },
            onNumberChanged = { value -> },
            onBackPress = {},
            modifier = Modifier.size(100.dp)
        )
    }
}