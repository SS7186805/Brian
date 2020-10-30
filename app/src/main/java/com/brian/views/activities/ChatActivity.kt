package com.brian.views.activities

import android.os.Bundle
import android.view.View
import com.brian.R
import com.brian.base.ScopedActivity

class ChatActivity : ScopedActivity() {

    private var postId: String? = null
    //    private var chatAdapter: ChatAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_fragment)
//        showProgressDialog(this)
        setUpUI()
    }

    private fun setUpUI() {
        /*      ButterKnife.bind(this)
              chatList = ArrayList<ChatModel>()
              chatAdapter = ChatAdapter(this, ArrayList<E>())
              layoutManager = LinearLayoutManager(this)
              layoutManager!!.stackFromEnd = true
              tvTitle.setText(R.string.txt_chat)
              tvTitle!!.isAllCaps = false
              ivToolbarLogo!!.visibility = View.GONE
              rvChatContainer!!.layoutManager = layoutManager
              rvChatContainer!!.adapter = chatAdapter
              if (getIntent().hasExtra(getString(R.string.key_post_id))) {
                  postId = getIntent().getStringExtra(getString(R.string.key_post_id))
                  dbReference = FirebaseDatabase.getInstance().getReference()
                  chatReference = dbReference.child("chat/$postId/messages")
                  chatReference.addValueEventListener(this)
              }*/
    }

    fun onSendClick(view: View?) {
        /*     if (BaseApplication.hasNetwork()) {
                 val message = etMessage!!.text.toString().trim { it <= ' ' }
                 if (!TextUtils.isEmpty(message)) {
                     val messageHashMap: MutableMap<String, Any> =
                         HashMap()
                     messageHashMap["content"] = message
                     messageHashMap["senderID"] = PreferencesHelper.getInstance().getUserID()
                     messageHashMap["senderName"] = PreferencesHelper.getInstance().getUserName()
                     messageHashMap["created"] = Utils.getInstance().getTime()
                     chatReference.push().setValue(messageHashMap)
                 } else {
                     ToastUtils.makeToast("Please enter message.")
                 }
                 etMessage!!.setText("")
             } else {
                 ToastUtils.makeToast(getString(R.string.txt_please_check_internet))
             }*/
    }

    override fun onBackPressed() {
        /* etMessage!!.clearFocus()
         ivSend!!.requestFocus()
         super.onBackPressed()*/
    }


}