package  com.gabrielbarth.zipcoderb.data.validator

class CepValidator {
    fun verificarCep(cep: String): Boolean {
        // Verifica se o CEP tem 8 dígitos e se todos são numéricos
        return cep.length == 8 && cep.all { it.isDigit() }
    }
}