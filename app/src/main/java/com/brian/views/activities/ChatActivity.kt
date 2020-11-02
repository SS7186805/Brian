package com.brian.views.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brian.R
import com.brian.base.MainApplication
import com.brian.base.ScopedActivity
import com.brian.databinding.ChatFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.SendMessageParams
import com.brian.viewModels.messages.MessagesViewModel
import com.brian.viewModels.messages.MessagesViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.util.*


class ChatActivity : ScopedActivity(), KodeinAware {
    override val kodein by lazy { (applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MessagesViewModelFactory by instance()
    lateinit var mBinding: ChatFragmentBinding
    lateinit var mViewModel: MessagesViewModel
    var chatRoomId = 0
    var otherUserId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setupViewModel()
        mBinding = DataBindingUtil.setContentView(this, R.layout.chat_fragment)
        mBinding.apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = intent.getStringExtra(getString(R.string.user))

        chatRoomId = intent.getIntExtra(getString(R.string.chat_room_id), 0)
        otherUserId = intent.getIntExtra(getString(R.string.other_user_id), 0)
        mViewModel.createChatRoomParams.other_user_id = otherUserId



        mBinding.chatRecycler.adapter = mViewModel.chatAdapter


        mBinding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }


        mViewModel.createChatRoom()


        setupObserver()
    }


    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(MessagesViewModel::class.java)
    }

    inner class ClickHandler {


        fun onSendMessage() {

            if (MainApplication.hasNetwork()) {
                val message: String = mBinding.etMessage.getText().toString().trim()
                if (!TextUtils.isEmpty(message)) {
                    val messageParams =
                        SendMessageParams()
                    messageParams.message = message
                    messageParams.chat_room_id = chatRoomId
                    messageParams.other_user_id = otherUserId
                    mViewModel.sendMessage(messageParams)

                } else {
                    showToast(getString(R.string.please_enter_message))
                }
                mBinding.etMessage.setText("")
            } else {
                showToast(getString(R.string.noInternetErr))
            }


        }


    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(this@ChatActivity, Observer {
                if (!TextUtils.isEmpty(it)) {
                    showToast(it)
                    showMessage.postValue("")
                }
            })

            resCreateChatRoomParams.observe(this@ChatActivity, Observer {
                if (it.result?.contains(getString(R.string.success))!!) {
                    mViewModel.getAllChatParams.chat_room_id = it.data?.id
                    chatRoomId = mViewModel.getAllChatParams.chat_room_id?.toInt()!!
                    mViewModel.getAllMessages()
                }
            })

            sendMessageResponse.observe(this@ChatActivity, Observer {
                if (it.result?.contains(getString(R.string.success))!!) {
                    if (mViewModel.allMessages.value?.size != 0) {
                        mViewModel.allMessages.value?.add(
                            mViewModel.allMessages.value!!.size,
                            it.data!!
                        )
                    } else {
                        mViewModel.allMessages.value?.add(
                            it.data!!
                        )
                    }

                    mBinding.chatRecycler.scrollToPosition(mViewModel.allMessages.value?.size!! - 1)
                }

            })

            allMessages.observe(this@ChatActivity, Observer {

                Collections.reverse(it)
                mViewModel.chatAdapter.updateList(it)
                mBinding.chatRecycler.scrollToPosition(mViewModel.allMessages.value?.size!! - 1);


            })

            showLoading.observe(this@ChatActivity, Observer {
                if (it) showProgress(this@ChatActivity) else hideProgress()
            })
        }
    }

}