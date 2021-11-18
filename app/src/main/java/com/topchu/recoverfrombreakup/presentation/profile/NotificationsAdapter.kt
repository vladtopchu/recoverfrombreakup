package com.topchu.recoverfrombreakup.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.graphics.rotationMatrix
import androidx.recyclerview.widget.RecyclerView
import com.topchu.recoverfrombreakup.data.local.entities.NotificationEntity
import com.topchu.recoverfrombreakup.databinding.ItemNotificationBinding
import com.topchu.recoverfrombreakup.utils.toTimeString

class NotificationsAdapter(): RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>()  {

    private var _notificationsList: List<NotificationEntity>? = null
    fun setNotifications(notificationList: List<NotificationEntity>?) {
        this._notificationsList = notificationList
    }

    private lateinit var _listener: OnItemClickListener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        _listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsViewHolder {
        lateinit var notificationsViewHolder: NotificationsViewHolder
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        notificationsViewHolder = NotificationsViewHolder(binding, _listener).apply {
            setIsRecyclable(false)
        }
        return notificationsViewHolder
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(_notificationsList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(_notificationsList == null) return 0
        return _notificationsList?.size!!
    }

    class NotificationsViewHolder(private val binding: ItemNotificationBinding, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationEntity){

            if(!notification.isActive) {
                binding.active.visibility = View.GONE
            }

            binding.title.text = notification.title
            binding.text.text = notification.text
            binding.timestamp.text = notification.timestamp.toTimeString()

            binding.notification.setOnClickListener {
                listener.onItemClick(notification)
                if(binding.textHolder.visibility == View.VISIBLE) {
                    binding.textHolder.visibility = View.GONE
                    binding.arrow.apply {
                        rotation = 0F
                        animate()
                    }
                } else {
                    binding.textHolder.visibility = View.VISIBLE
                    binding.arrow.apply {
                        rotation = 180F
                        animate()
                    }
                }
                if(notification.isActive && binding.active.visibility != View.GONE){
                    binding.active.visibility = View.GONE
                }
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(notification: NotificationEntity)
    }
}