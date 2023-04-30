package edu.udb.crud_retrofit_escuela

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class AlumnoAdapter(var context: Context, var listaalumnos: ArrayList<Alumno>): RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {
    private var onClick: OnItemClicked? = null
    // creamos una nueva instancia de un Alumno en una RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        // Creamos una vista para mostar los elementos en el RecyclerView
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_alumno, parent, false)
        return AlumnoViewHolder(vista)
    }

    // Con esta funcion se actualizan los datos de un alumno, dentro del RecyclerView
    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = listaalumnos.get(position)
        // Se actualizan las vistas para mostrar los datos de los alumnos
        holder.tvIdAlumno.text = alumno.idAlumno.toString()
        holder.tvNombre.text = alumno.nombre
        holder.tvEdad.text = alumno.edad
        // Con OnClickListener se llama al metodo editarAlumno cuando se presione el boton editar
        holder.btnEditar.setOnClickListener {
            onClick?.editarAlumno(alumno)
        }
        // Con OnClickListener se llama al metodo borrarAlumno cuando se presione el boton borrar
        holder.btnBorrar.setOnClickListener {
            onClick?.borrarAlumno(alumno.idAlumno)
        }
    }

    // Funcion que nos da el numero de alumnos que se mostraran en el RecyclerView
    override fun getItemCount(): Int {
        return listaalumnos.size
    }

    // Esta clase interna nos ayudara a formar los elementos de la vista
    // Incluye los datos del Alumno y los botones del CRUD
    inner class AlumnoViewHolder(itemView: View): ViewHolder(itemView) {
        val tvIdAlumno = itemView.findViewById(R.id.tvIdAlumno) as TextView
        val tvNombre = itemView.findViewById(R.id.tvNombre) as TextView
        val tvEdad = itemView.findViewById(R.id.tvEdad) as TextView
        val btnEditar = itemView.findViewById(R.id.btnEditar) as Button
        val btnBorrar = itemView.findViewById(R.id.btnBorrar) as Button
    }

    // Esto no ayudara a eliminar o editar a un alumno en especifico
    interface OnItemClicked {
        fun editarAlumno(alumno: Alumno)
        fun borrarAlumno(idAlumno: Int)
    }

    // Esta funcion nos ayudara a manejar los metodos de los botones editar y borrar
    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}