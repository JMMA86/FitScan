package icesi.edu.co.fitscan.features.auth.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icesi.edu.co.fitscan.R
import icesi.edu.co.fitscan.features.auth.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(greenLess: Color, registerViewModel: RegisterViewModel = viewModel()) {
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.img_login),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo content
            Column(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fitscan),
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
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )
            }

            // Register content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenLess,
                        unfocusedBorderColor = greenLess,
                        focusedLabelColor = greenLess,
                        unfocusedLabelColor = greenLess,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            if (it.isEmpty() || it.toInt() < 100) {
                                age = it
                            }
                        }
                    },
                    label = { Text("Edad") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenLess,
                        unfocusedBorderColor = greenLess,
                        focusedLabelColor = greenLess,
                        unfocusedLabelColor = greenLess,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenLess,
                        unfocusedBorderColor = greenLess,
                        focusedLabelColor = greenLess,
                        unfocusedLabelColor = greenLess,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            phone = it
                        }
                    },
                    label = { Text("Número telefónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenLess,
                        unfocusedBorderColor = greenLess,
                        focusedLabelColor = greenLess,
                        unfocusedLabelColor = greenLess,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = { termsAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = greenLess,
                            uncheckedColor = Color.White
                        )
                    )
                    Text(
                        text = "Acepto los",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    TextButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.padding(start = 4.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Términos y condiciones",
                            color = greenLess,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        registerViewModel.register(email, fullName, age, phone)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = greenLess),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Completar Registro", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    FitScanTheme {
        RegisterScreen(Color(0xFF4CAF50))
    }
}
