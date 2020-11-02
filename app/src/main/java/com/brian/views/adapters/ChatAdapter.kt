package com.brian.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.Prefs
import com.brian.internals.Utils
import com.brian.models.AllMessagesDataItem
import com.brian.providers.resources.ResourcesProvider

class ChatAdapter(
    resourcesProvider: ResourcesProvider,
    chatList: ArrayList<AllMessagesDataItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chatList: ArrayList<AllMessagesDataItem>
    private val resourcesProvider: ResourcesProvider
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == -1) {

            SentMessageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycler_chat_receiver_item,
                    parent,
                    false
                )
            )
        } else {

            ReceivedMessageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycler_chat_sender_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == -1) {
            (holder as SentMessageHolder).setData(chatList[position])
        } else {
            (holder as ReceivedMessageHolder).setData(chatList[position])
        }
    }

    fun updateList(chatList: ArrayList<AllMessagesDataItem>) {
        this.chatList = chatList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {

        Log.e("UserId", "UserId${Prefs.init().userInfo?.id}")
        Log.e("SenderUserId", "SenderUserId${chatList[position].senderUserId}")
        Log.e("messge", "SenderUserId${chatList[position].message}")
        return if (Prefs.init().userInfo?.id ==
            chatList[position].senderUserId
        ) {
            Log.e("SEnderrr", "UserId${Prefs.init().userInfo?.id}")

            -1
        } else {
            position
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    internal inner class SentMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun setData(chatModel: AllMessagesDataItem) {
            var tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
            var tvTime = itemView.findViewById<TextView>(R.id.tvTime)

            tvMessage.setText(chatModel.message)
            if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
                tvTime.setText(Utils.init.get24HourTime(chatModel.createdAt.toString()))
            } else {
                tvTime.setText(Utils.init.get12HourTime(chatModel.createdAt.toString()))
            }
        }

        init {
            /*  ButterKnife.bind(this, itemView)
              itemView.setOnLongClickListener { v: View? ->
                  Utils.getInstance().copyMessage(tvMessage!!.text.toString(), context)
                  false
              }*/
        }
    }

    internal inner class ReceivedMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {


        fun setData(chatModel: AllMessagesDataItem) {
            var tvMessage = itemView.findViewById<TextView>(R.id.tvMessage)
            var tvTime = itemView.findViewById<TextView>(R.id.tvTime)

            tvMessage.setText(chatModel.message)
            if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
                tvTime.setText(Utils.init.get24HourTime(chatModel.createdAt.toString()))
            } else {
                tvTime.setText(Utils.init.get12HourTime(chatModel.createdAt.toString()))
            }
        }

        init {
            /* ButterKnife.bind(this, itemView)
             itemView.setOnLongClickListener { v: View? ->
                 Utils.getInstance().copyMessage(tvMessage!!.text.toString(), context)
                 false
             }*/
        }
    }

    init {
        this.chatList = chatList
        this.resourcesProvider = resourcesProvider
    }
}