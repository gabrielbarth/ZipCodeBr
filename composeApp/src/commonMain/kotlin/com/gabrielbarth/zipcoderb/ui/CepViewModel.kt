package com.gabrielbarth.zipcoderb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielbarth.zipcoderb.data.model.Endereco
import com.gabrielbarth.zipcoderb.data.repository.CepRepository
import com.gabrielbarth.zipcoderb.data.validator.CepValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CepViewModel(
    private val cepRepository: CepRepository,
    private val cepValidator: CepValidator
) : ViewModel() {

    private val _cep = MutableStateFlow("")
    val cep: StateFlow<String> = _cep

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled

    private val _endereco = MutableStateFlow<Endereco?>(null)
    val endereco: StateFlow<Endereco?> = _endereco

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onCepChanged(newCep: String) {
        if (newCep.length <= 8) {
            if (newCep.all { it.isDigit() }) {
                _cep.value = newCep
                _isButtonEnabled.value = cepValidator.verificarCep(newCep)
                _error.value = null
            } else {
                setError("Digite apenas números")
            }
        }
    }

    private fun setError(errorMessage: String) {
        _error.value = errorMessage
    }

    fun fetchCep() {
        if (_isButtonEnabled.value) {
            _isLoading.value = true
            _endereco.value = null
            viewModelScope.launch {
                try {
                    val result = cepRepository.buscarCep(_cep.value)

                    if (result?.cep?.isNotBlank() == true) {
                        _endereco.value = result
                    } else {
                        setError("CEP não encontrado")
                    }
                } catch (e: Exception) {
                    setError("Erro ao buscar o CEP")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}