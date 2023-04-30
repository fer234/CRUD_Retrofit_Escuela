package edu.udb.crud_retrofit_escuela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import edu.udb.crud_retrofit_escuela.databinding.ActivityAlumnosBinding
import kotlinx.coroutines.*

class AlumnosActivity : AppCompatActivity(), AlumnoAdapter.OnItemClicked {
    // Agregamos el codigo del menu de opciones para tener buena navegacion
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuopciones,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.telefono) {
            Toast.makeText(this, getString(R.string.mensaje_icono_telefono), Toast.LENGTH_LONG).show();
            val intent = Intent(this, AlumnosActivity::class.java)
            startActivity(intent)
        }
        if (id == R.id.camara) {
            Toast.makeText(this, getString(R.string.mensaje_icono_camara), Toast.LENGTH_LONG).show();
            val intent = Intent(this, ProfesoresActivity::class.java)
            startActivity(intent)
        }
        if (id == R.id.alumnos) {
            Toast.makeText(this, getString(R.string.mensaje_1), Toast.LENGTH_LONG).show();
            val intent = Intent(this, AlumnosActivity::class.java)
            startActivity(intent)
        }
        if (id == R.id.profesores) {
            Toast.makeText(this, getString(R.string.mensaje_2), Toast.LENGTH_LONG).show();
            val intent = Intent(this, ProfesoresActivity::class.java)
            startActivity(intent)
        }
        if(id == R.id.action_sign_out){
            FirebaseAuth.getInstance().signOut().also {
                Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Aqui comienza el codigo para los mantenimientos del CRUD
    lateinit var binding: ActivityAlumnosBinding
    lateinit var adatador: AlumnoAdapter
    var listaAlumnos = arrayListOf<Alumno>()
    var alumno = Alumno(-1, "","")
    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlumnosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvAlumnos.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()
        obtenerAlumnos()
        binding.btnAddUpdate.setOnClickListener {
            var isValido = validarCampos()
            if (isValido) {
                if (!isEditando) {
                    agregarAlumno()
                } else {
                    actualizarAlumno()
                }
            } else {
                Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun setupRecyclerView() {
        adatador = AlumnoAdapter(this, listaAlumnos)
        adatador.setOnClick(this@AlumnosActivity)
        binding.rvAlumnos.adapter = adatador
    }

    fun validarCampos(): Boolean {
        return !(binding.etNombre.text.isNullOrEmpty() || binding.etEdad.text.isNullOrEmpty())
    }

    fun obtenerAlumnos() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerAlumnos()
            runOnUiThread {
                if (call.isSuccessful) {
                    listaAlumnos = call.body()!!.listaAlumnos
                    setupRecyclerView()
                } else {
                    Toast.makeText(this@AlumnosActivity, "ERROR CONSULTAR TODOS", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun agregarAlumno() {
        this.alumno.idAlumno = -1
        this.alumno.nombre = binding.etNombre.text.toString()
        this.alumno.edad = binding.etEdad.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarAlumno(alumno)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@AlumnosActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerAlumnos()
                    limpiarCampos()
                    limpiarObjeto()
                } else {
                    Toast.makeText(this@AlumnosActivity, "ERROR ADD", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun actualizarAlumno() {
        this.alumno.nombre = binding.etNombre.text.toString()
        this.alumno.edad = binding.etEdad.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.actualizarAlumno(alumno.idAlumno, alumno)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@AlumnosActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerAlumnos()
                    limpiarCampos()
                    limpiarObjeto()
                    binding.btnAddUpdate.setText("Agregar Alumno")
                    binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.green)
                    isEditando = false
                }
            }
        }
    }

    fun limpiarCampos() {
        binding.etNombre.setText("")
        binding.etEdad.setText("")
    }

    fun limpiarObjeto() {
        this.alumno.idAlumno = -1
        this.alumno.nombre = ""
        this.alumno.edad = ""
    }

    override fun editarAlumno(alumno: Alumno) {
        binding.etNombre.setText(alumno.nombre)
        binding.etEdad.setText(alumno.edad)
        binding.btnAddUpdate.setText("Actualizar Alumno")
        binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.purple_500)
        this.alumno = alumno
        isEditando = true
    }

    override fun borrarAlumno(idAlumno: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarAlumno(idAlumno)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@AlumnosActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerAlumnos()
                }
            }
        }
    }
}