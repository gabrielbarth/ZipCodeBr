package com.gabrielbarth.zipcoderb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gabrielbarth.zipcoderb.data.repository.CepRepository
import com.gabrielbarth.zipcoderb.data.validator.CepValidator
import kotlinx.coroutines.launch

@Composable
fun CepView() {
    val cepViewModel: CepViewModel = remember {
        CepViewModel(
            cepRepository = CepRepository(),
            cepValidator = CepValidator()
        )
    }
    val cep by cepViewModel.cep.collectAsState()
    val isButtonEnabled by cepViewModel.isButtonEnabled.collectAsState()
    val endereco by cepViewModel.endereco.collectAsState()
    val error by cepViewModel.error.collectAsState()
    val isLoading by cepViewModel.isLoading.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error?.let {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = cep,
                    onValueChange = { cepViewModel.onCepChanged(it) },
                    label = { Text("Digite o CEP") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { cepViewModel.fetchCep() },
                    enabled = isButtonEnabled && !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colors.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Consultar CEP")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                endereco?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "CEP: ${it.cep}")
                            Text(text = "Logradouro: ${it.logradouro}")
                            Text(text = "Bairro: ${it.bairro}")
                            Text(text = "Cidade: ${it.localidade}")
                            Text(text = "Estado: ${it.uf}")
                            Text(text = "País: Brasil")
                        }
                    }
                }
                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error
                    )
                }
            }
        }
    )
}