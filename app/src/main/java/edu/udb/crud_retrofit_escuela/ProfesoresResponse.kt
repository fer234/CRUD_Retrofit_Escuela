package edu.udb.crud_retrofit_escuela

import com.google.gson.annotations.SerializedName

// Esta clase se utiliza para representar la respuesta de una solicitud a una API
// que devuelve una lista de objetos Profesor
data class ProfesoresResponse(
    @SerializedName("listaProfesores") var listaProfesores: ArrayList<Profesor>
)
