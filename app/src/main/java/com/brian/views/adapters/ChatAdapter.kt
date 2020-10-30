package com.brian.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brian.R

class ChatAdapter(
    context: Context,
    chatList: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chatList: List<String>
    private val context: Context
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
//            (holder as SentMessageHolder).setData(chatList[position])
        } else {
//            (holder as ReceivedMessageHolder).setData(chatList[position])
        }
    }

    fun updateList(chatList: List<String>) {
        this.chatList = chatList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return  position
     /*   return if (PreferencesHelper.getInstance().getUserID().equalsIgnoreCase(
                String.valueOf(
                    chatList[position].getSenderID()
                )
            )
        ) {
            -1
        } else {
            position
        }*/
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    internal inner class SentMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
      /*  @BindView(R.id.tvMessage)
        var tvMessage: TextView? = null
        @BindView(R.id.tvTime)
        var tvTime: TextView? = null

        fun setData(chatModel: ChatModel) {
            tvMessage.setText(chatModel.getContent())
            if (DateFormat.is24HourFormat(context)) {
                tvTime.setText(Utils.get24HourTime(chatModel.getCreated()))
            } else {
                tvTime.setText(Utils.get12HourTime(chatModel.getCreated()))
            }
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnLongClickListener { v: View? ->
                Utils.getInstance().copyMessage(tvMessage!!.text.toString(), context)
                false
            }
        }*/
    }

    internal inner class ReceivedMessageHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
   /*     @BindView(R.id.tvMessage)
        var tvMessage: TextView? = null
        @BindView(R.id.tvTime)
        var tvTime: TextView? = null
        @BindView(R.id.tvSender)
        var tvSender: TextView? = null

        fun setData(chatModel: ChatModel) {
            tvMessage.setText(chatModel.getContent())
            if (DateFormat.is24HourFormat(context)) {
                tvTime.setText(Utils.get24HourTime(chatModel.getCreated()))
            } else {
                tvTime.setText(Utils.get12HourTime(chatModel.getCreated()))
            }
            tvSender.setText(chatModel.getSenderName())
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnLongClickListener { v: View? ->
                Utils.getInstance().copyMessage(tvMessage!!.text.toString(), context)
                false
            }
        }*/
    }

    init {
        this.chatList = chatList
        this.context = context
    }
}