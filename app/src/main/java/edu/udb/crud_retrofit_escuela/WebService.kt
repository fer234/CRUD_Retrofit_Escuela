package edu.udb.crud_retrofit_escuela

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// La solicitudes HTTP a la API se enviaran a esta URL
object AppConstantes {
    const val BASE_URL = "http://192.168.1.9:3000"
}

interface WebService {
    // Aqui  definimos todas las peticiones que haremos a la API
    // Las respuestas seran encapsuladas en el objeto AlumnosResponse
    // suspend sirve para que el metodo se suspenda luego de ser ejecutado

    // Peticiones para alumnos
    @GET("/alumnos")
    suspend fun obtenerAlumnos(): Response<AlumnosResponse>

    @GET("/alumno/{idAlumno}")
    suspend fun obtenerAlumno(
        @Path("idAlumno") idAlumno: Int
    ): Response<Alumno>

    @POST("/alumno/add")
    suspend fun agregarAlumno(
        @Body alumno: Alumno
    ): Response<String>

    @PUT("/alumno/update/{idAlumno}")
    suspend fun actualizarAlumno(
        @Path("idAlumno") idAlumno: Int,
        @Body alumno: Alumno
    ): Response<String>

    @DELETE("/alumno/delete/{idAlumno}")
    suspend fun borrarAlumno(
        @Path("idAlumno") idAlumno: Int
    ): Response<String>

    // Peticiones para los profesores
    @GET("/profesores")
    suspend fun obtenerProfesores(): Response<ProfesoresResponse>

    @GET("/profesor/{idProfesor}")
    suspend fun obtenerProfesor(
        @Path("idProfesor") idProfesor: Int
    ): Response<Profesor>

    @POST("/profesor/add")
    suspend fun agregarProfesor(
        @Body profesor: Profesor
    ): Response<String>

    @PUT("/profesor/update/{idProfesor}")
    suspend fun actualizarProfesor(
        @Path("idProfesor") idProfesor: Int,
        @Body profesor: Profesor
    ): Response<String>

    @DELETE("/profesor/delete/{idProfesor}")
    suspend fun borrarProfesor(
        @Path("idProfesor") idProfesor: Int
    ): Response<String>
}

// RetrofitClient se utiliza para crear y configurar una instancia de Retrofit,
// que se utiliza para realizar solicitudes HTTP y recibir respuestas JSON de la API.
object RetrofitClient {
    // Con la instancia webService realizamos las solicitudes
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}