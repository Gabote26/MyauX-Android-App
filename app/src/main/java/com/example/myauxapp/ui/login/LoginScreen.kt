package com.example.myauxapp.ui.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myauxapp.R
import com.example.myauxapp.ui.components.UnderlineTextField
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit
) {

    val PurpleDark = Color(0xFF4A148C)
    val TealGreen = Color(0xFF009688)
    val LightBackground = Color(0xFFF7F7F7)

    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.state

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    /* -------- Plantel Dropdown -------- */
    val planteles = listOf(
        "Selecciona un plantel",
        "CBTis123"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedPlantel by remember { mutableStateOf(planteles[0]) }

    val isFormValid =
        email.isNotBlank() &&
                password.isNotBlank() &&
                selectedPlantel != planteles[0]

    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "MyauX",
                color = PurpleDark,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontSize = 17.em
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        /* -------- Email -------- */
        UnderlineTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email"
        )

        Spacer(Modifier.height(16.dp))

        /* -------- Password -------- */
        UnderlineTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        imageVector =
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = PurpleDark
                    )
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        // Lista de Planteles (Solo CBTis123)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selectedPlantel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Plantel") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,

                    cursorColor = Color(0xFF4A148C),
                    focusedTextColor = Color(0xFF4A148C),
                    unfocusedTextColor = Color(0xFF4A148C),

                    focusedPlaceholderColor = Color(0xFF4A148C).copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = Color(0xFF4A148C).copy(alpha = 0.5f),
                    errorPlaceholderColor = Color.Red
                ),
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                planteles.forEach { plantel ->
                    DropdownMenuItem(
                        text = { Text(plantel) },
                        onClick = {
                            selectedPlantel = plantel
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        /* -------- States -------- */
        when (state) {

            is LoginState.Loading -> {
                CircularProgressIndicator(color = PurpleDark)
            }

            /*is LoginState.Idle -> {
                Button(
                    onClick = {

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                    ) {
                    Text("Login", color = Color.Black)
                }
            }*/

            is LoginState.Error -> {
                Text(
                    text = (state as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(8.dp))

                Button(onClick = {
                    viewModel.retry(email, password)
                }) {
                    Text("Reintentar")
                }
            }

            else -> {
                Button(
                    onClick = {
                        viewModel.login(email, password)
                    },
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login", color = Color.White)
                }
            }
        }

        Spacer(Modifier.weight(1f))
    }
}
