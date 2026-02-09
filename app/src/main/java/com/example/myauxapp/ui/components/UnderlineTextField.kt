package com.example.myauxapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var focused by remember { mutableStateOf(false) }

    val indicatorColor by animateColorAsState(
        targetValue = when {
            isError -> Color.Red
            focused -> Color(0xFF4A148C)
            else -> Color(0xFF4A148C).copy(alpha = 0.5f)
        },
        animationSpec = tween(250),
        label = "indicatorColor"
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,

            focusedIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            errorIndicatorColor = Color.Red,

            cursorColor = Color(0xFF4A148C),
            focusedTextColor = Color(0xFF4A148C),
            unfocusedTextColor = Color(0xFF4A148C),

            focusedPlaceholderColor = Color(0xFF4A148C).copy(alpha = 0.6f),
            unfocusedPlaceholderColor = Color(0xFF4A148C).copy(alpha = 0.5f),
            errorPlaceholderColor = Color.Red
        )
    )
}
