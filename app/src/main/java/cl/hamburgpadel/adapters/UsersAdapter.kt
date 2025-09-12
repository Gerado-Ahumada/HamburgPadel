package cl.hamburgpadel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cl.hamburgpadel.R
import cl.hamburgpadel.data.models.Player

/**
 * Adaptador para el RecyclerView de usuarios
 */
class UsersAdapter(
    private var users: List<Player> = emptyList(),
    private val onEditClick: (Player) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFullName: TextView = itemView.findViewById(R.id.textViewFullName)
        val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)
        val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
        val textViewCategory: TextView = itemView.findViewById(R.id.textViewCategory)
        val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        val buttonEdit: Button = itemView.findViewById(R.id.buttonEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        
        holder.textViewFullName.text = user.fullName
        holder.textViewUsername.text = "@${user.username}"
        holder.textViewEmail.text = user.email
        holder.textViewCategory.text = user.playerCategory
        holder.textViewStatus.text = user.status
        
        // Configurar colores según el estado
        when (user.status) {
            "ACTIVE" -> {
                holder.textViewStatus.setBackgroundResource(R.drawable.status_active_background)
            }
            "INACTIVE" -> {
                holder.textViewStatus.setBackgroundResource(R.drawable.status_inactive_background)
            }
            else -> {
                holder.textViewStatus.setBackgroundResource(R.drawable.status_badge_background)
            }
        }
        
        // Configurar colores según la categoría
        when (user.playerCategory) {
            "SENIOR" -> {
                holder.textViewCategory.setBackgroundResource(R.drawable.category_senior_background)
            }
            "JUNIOR" -> {
                holder.textViewCategory.setBackgroundResource(R.drawable.category_junior_background)
            }
            "AMATEUR" -> {
                holder.textViewCategory.setBackgroundResource(R.drawable.category_amateur_background)
            }
            else -> {
                holder.textViewCategory.setBackgroundResource(R.drawable.category_badge_background)
            }
        }
        
        // Configurar click del botón editar
        holder.buttonEdit.setOnClickListener {
            onEditClick(user)
        }
    }

    override fun getItemCount(): Int = users.size

    /**
     * Actualiza la lista de usuarios
     */
    fun updateUsers(newUsers: List<Player>) {
        users = newUsers
        notifyDataSetChanged()
    }

    /**
     * Limpia la lista de usuarios
     */
    fun clearUsers() {
        users = emptyList()
        notifyDataSetChanged()
    }
}