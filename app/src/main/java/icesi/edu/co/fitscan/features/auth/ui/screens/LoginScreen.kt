package icesi.edu.co.fitscan.features.auth.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.auth.ui.model.LoginUiState
import icesi.edu.co.fitscan.features.auth.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    // Asumiendo que greenLess viene de tu Theme o lo defines aquí
    greenLess: Color = MaterialTheme.colorScheme.primary, // Ejemplo, ajusta según tu Theme
    loginViewModel: LoginViewModel = viewModel(), // Inyecta el ViewModel
    onLoginSuccess: () -> Unit, // Callback para navegar en éxito (a Dashboard)
    onNavigateToRegister: () -> Unit, // Callback para ir a registro
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Observar el estado de la UI desde el ViewModel
    val uiState by loginViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() } // Para mostrar mensajes
    val scope = rememberCoroutineScope() // Para lanzar coroutines en el Composable

    // Efecto para manejar cambios de estado (éxito/error) y mostrar mensajes
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                // Muestra mensaje y navega
                scope.launch {
                    snackbarHostState.showSnackbar("¡Bienvenido!") // Mensaje corto de éxito
                }
                onLoginSuccess()
            }
            is LoginUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
                loginViewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_login),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f), // Ocupa espacio flexible
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_fitscan), // [cite: 5]
                        contentDescription = "Logo",
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "FitScan",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Inicia tu transformación física hoy",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Button(
//                        onClick = onGoogleLoginClick,
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//                        shape = RoundedCornerShape(8.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp),
//                        enabled = uiState != LoginUiState.Loading
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_google),
//                                contentDescription = "Google icon",
//                                tint = Color.Unspecified,
//                                modifier = Modifier.size(24.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(text = "Continuar con Google", color = Color.Black)
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Divider [cite: 14]
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        HorizontalDivider(modifier = Modifier.weight(1f), color = greenLess)
//                        Text(text = "  o  ", color = greenLess, fontSize = 14.sp)
//                        HorizontalDivider(modifier = Modifier.weight(1f), color = greenLess)
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))

                    // Email input [cite: 18]
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = greenLess,
                            unfocusedBorderColor = greenLess,
                            focusedLabelColor = greenLess,
                            unfocusedLabelColor = greenLess,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = greenLess,
                            focusedLeadingIconColor = Color.White,
                            unfocusedLeadingIconColor = Color.White
                        ),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail),
                                contentDescription = "Mail icon",
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        enabled = uiState != LoginUiState.Loading
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", color = Color.White) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // [cite: 26]
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = greenLess,
                            unfocusedBorderColor = greenLess,
                            focusedLabelColor = greenLess,
                            unfocusedLabelColor = greenLess,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = greenLess,
                            focusedTrailingIconColor = Color.White,
                            unfocusedTrailingIconColor = Color.White
                        ),
                        trailingIcon = { // [cite: 29]
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.ic_eyeopen else R.drawable.ic_eyeclosed),
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",

                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        enabled = uiState != LoginUiState.Loading
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            loginViewModel.login(email, password)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = greenLess),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = uiState != LoginUiState.Loading
                    ) {
                        if (uiState == LoginUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(text = "Iniciar sesión", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "¿No tienes una cuenta?",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = onNavigateToRegister) {
                            Text(
                                text = "Regístrate",
                                color = greenLess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FitScanTheme {
        LoginScreen(
            onLoginSuccess = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {},
            onGoogleLoginClick = {}
        )
    }
}
*/