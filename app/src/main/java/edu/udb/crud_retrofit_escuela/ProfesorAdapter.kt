package edu.udb.crud_retrofit_escuela

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ProfesorAdapter(var context: Context, var listaprofesores: ArrayList<Profesor>): RecyclerView.Adapter<ProfesorAdapter.ProfesorViewHolder>() {
    private var onClick: OnItemClicked? = null
    // creamos una nueva instancia de un Profesor en una RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorViewHolder {
        // Creamos una vista para mostar los elementos en el RecyclerView
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_profesor, parent, false)
        return ProfesorViewHolder(vista)
    }

    // Con esta funcion se actualizan los datos de un profesor, dentro del RecyclerView
    override fun onBindViewHolder(holder: ProfesorViewHolder, position: Int) {
        val profesor = listaprofesores.get(position)
        // Se actualizan las vistas para mostrar los datos de los profesores
        holder.tvIdProfesor.text = profesor.idProfesor.toString()
        holder.tvNombre.text = profesor.nombre
        holder.tvEmail.text = profesor.email
        // Con OnClickListener se llama al metodo editarProfesor cuando se presione el boton editar
        holder.btnEditar.setOnClickListener {
            onClick?.editarProfesor(profesor)
        }
        // Con OnClickListener se llama al metodo borrarProfesor cuando se presione el boton borrar
        holder.btnBorrar.setOnClickListener {
            onClick?.borrarProfesor(profesor.idProfesor)
        }
    }

    // Funcion que nos da el numero de profesores que se mostraran en el RecyclerView
    override fun getItemCount(): Int {
        return listaprofesores.size
    }

    // Esta clase interna nos ayudara a formar los elementos de la vista
    // Incluye los datos del Profesor y los botones del CRUD
    inner class ProfesorViewHolder(itemView: View): ViewHolder(itemView) {
        val tvIdProfesor = itemView.findViewById(R.id.tvIdProfesor) as TextView
        val tvNombre = itemView.findViewById(R.id.tvNombre) as TextView
        val tvEmail = itemView.findViewById(R.id.tvEmail) as TextView
        val btnEditar = itemView.findViewById(R.id.btnEditar) as Button
        val btnBorrar = itemView.findViewById(R.id.btnBorrar) as Button
    }

    // Esto no ayudara a eliminar o editar a un profesor en especifico
    interface OnItemClicked {
        fun editarProfesor(profesor: Profesor)
        fun borrarProfesor(idProfesor: Int)
    }

    // Esta funcion nos ayudara a manejar los metodos de los botones editar y borrar
    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}