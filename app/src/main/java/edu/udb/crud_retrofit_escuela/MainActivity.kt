package edu.udb.crud_retrofit_escuela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.udb.crud_retrofit_escuela.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
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
}