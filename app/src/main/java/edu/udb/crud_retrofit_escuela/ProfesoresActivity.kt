package edu.udb.crud_retrofit_escuela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import edu.udb.crud_retrofit_escuela.databinding.ActivityProfesoresBinding
import kotlinx.coroutines.*

class ProfesoresActivity : AppCompatActivity(), ProfesorAdapter.OnItemClicked {
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
    lateinit var binding: ActivityProfesoresBinding
    lateinit var adatador: ProfesorAdapter
    var listaProfesores = arrayListOf<Profesor>()
    var profesor = Profesor(-1, "","")
    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfesoresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvProfesores.layoutManager = LinearLayoutManager(this)
        setupRecyclerView()
        obtenerProfesores()
        binding.btnAddUpdate.setOnClickListener {
            var isValido = validarCampos()
            if (isValido) {
                if (!isEditando) {
                    agregarProfesor()
                } else {
                    actualizarProfesor()
                }
            } else {
                Toast.makeText(this, "Se deben llenar los campos", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun setupRecyclerView() {
        adatador = ProfesorAdapter(this, listaProfesores)
        adatador.setOnClick(this@ProfesoresActivity)
        binding.rvProfesores.adapter = adatador

    }

    fun validarCampos(): Boolean {
        return !(binding.etNombre.text.isNullOrEmpty() || binding.etEmail.text.isNullOrEmpty())
    }

    fun obtenerProfesores() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerProfesores()
            runOnUiThread {
                if (call.isSuccessful) {
                    listaProfesores = call.body()!!.listaProfesores
                    setupRecyclerView()
                } else {
                    Toast.makeText(this@ProfesoresActivity, "ERROR CONSULTAR TODOS", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun agregarProfesor() {
        this.profesor.idProfesor = -1
        this.profesor.nombre = binding.etNombre.text.toString()
        this.profesor.email = binding.etEmail.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarProfesor(profesor)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@ProfesoresActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerProfesores()
                    limpiarCampos()
                    limpiarObjeto()

                } else {
                    Toast.makeText(this@ProfesoresActivity, "ERROR ADD", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun actualizarProfesor() {
        this.profesor.nombre = binding.etNombre.text.toString()
        this.profesor.email = binding.etEmail.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.actualizarProfesor(profesor.idProfesor, profesor)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@ProfesoresActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerProfesores()
                    limpiarCampos()
                    limpiarObjeto()
                    binding.btnAddUpdate.setText("Agregar Profesor")
                    binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.green)
                    isEditando = false
                }
            }
        }
    }

    fun limpiarCampos() {
        binding.etNombre.setText("")
        binding.etEmail.setText("")
    }

    fun limpiarObjeto() {
        this.profesor.idProfesor = -1
        this.profesor.nombre = ""
        this.profesor.email = ""
    }

    override fun editarProfesor(profesor: Profesor) {
        binding.etNombre.setText(profesor.nombre)
        binding.etEmail.setText(profesor.email)
        binding.btnAddUpdate.setText("Actualizar Profesor")
        binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.purple_500)
        this.profesor = profesor
        isEditando = true
    }

    override fun borrarProfesor(idProfesor: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarProfesor(idProfesor)
            runOnUiThread {
                if (call.isSuccessful) {
                    Toast.makeText(this@ProfesoresActivity, call.body().toString(), Toast.LENGTH_LONG).show()
                    obtenerProfesores()
                }
            }
        }
    }
}