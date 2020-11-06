package com.brian.views.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.Prefs
import com.brian.internals.Utils
import com.brian.models.AllMessagesDataItem
import com.brian.providers.resources.ResourcesProvider
import com.bumptech.glide.Glide

class ChatAdapter(
    resourcesProvider: ResourcesProvider,
    chatList: ArrayList<AllMessagesDataItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chatList: ArrayList<AllMessagesDataItem>
    private val resourcesProvider: ResourcesProvider
    var listener: onClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == -1) {

            SentMessageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycler_chat_sender_item,
                    parent,
                    false
                )
            )
        } else {

            ReceivedMessageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.recycler_chat_receiver_item,
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

        /*  Log.e("USerIddd", Prefs.init().userInfo?.id.toString())
          Log.e("chatList[position].senderUserId", chatList[position].senderUserId.toString())*/

        return if (Prefs.init().userInfo?.id.toString().equals(chatList[position].senderUserId.toString())

        ) {
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
            var videoImage = itemView.findViewById<CardView>(R.id.videoImage)
            var ivVideo = itemView.findViewById<ImageView>(R.id.ivVideo)

            if (chatModel.typeOfFile?.toString().equals("video")) {
                tvMessage.visibility = GONE
                videoImage.visibility = VISIBLE
                Glide.with(videoImage.context).load(chatModel.fileName).centerCrop()
                    .placeholder(R.drawable.ic_use_r)
                    .into(ivVideo)


            } else {
                tvMessage.setText(chatModel.message)
                tvMessage.visibility = VISIBLE
                videoImage.visibility = GONE

            }



            if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
                tvTime.setText(Utils.init.get24HourTime(chatModel.createdAt.toString()))
            } else {
                tvTime.setText(Utils.init.get12HourTime(chatModel.createdAt.toString()))
            }


            itemView.setOnClickListener {
                if (chatModel.typeOfFile?.toString().equals("video")) {
                    itemView.isEnabled = false
                    Handler().postDelayed({

                        itemView.isEnabled = true

                    }, 1000)

                    listener?.onVideoMessageClick(position, chatModel.fileName.toString())
                }
            }

            itemView.setOnLongClickListener { v: View? ->
                Utils.init.copyMessage(tvMessage.text.toString(), resourcesProvider.getContext())
                false
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
            var videoImage = itemView.findViewById<CardView>(R.id.videoImage)
            var ivVideo = itemView.findViewById<ImageView>(R.id.ivVideo)

            if (chatModel.typeOfFile?.toString().equals("video")) {
                tvMessage.visibility = GONE
                videoImage.visibility = VISIBLE
                Glide.with(videoImage.context).load(chatModel.fileName).centerCrop()
                    .placeholder(R.drawable.ic_use_r)
                    .into(ivVideo)


            } else {
                tvMessage.setText(chatModel.message)
                tvMessage.visibility = VISIBLE
                videoImage.visibility = GONE

            }



            if (android.text.format.DateFormat.is24HourFormat(resourcesProvider.getContext())) {
                tvTime.setText(Utils.init.get24HourTime(chatModel.createdAt.toString()))
            } else {
                tvTime.setText(Utils.init.get12HourTime(chatModel.createdAt.toString()))
            }
            itemView.setOnClickListener {
                if (chatModel.typeOfFile?.toString().equals("video")) {
                    itemView.isEnabled = false
                    Handler().postDelayed({

                        itemView.isEnabled = true

                    }, 1000)

                    listener?.onVideoMessageClick(position, chatModel.fileName.toString())
                }
            }

            itemView.setOnLongClickListener { v: View? ->
                Utils.init.copyMessage(tvMessage.text.toString(), resourcesProvider.getContext())
                false
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

    fun setNewItems(newItems: ArrayList<AllMessagesDataItem>) {
        this.chatList = newItems
        notifyDataSetChanged()
    }

    interface onClick {
        fun onVideoMessageClick(position: Int, url: String)
    }
}