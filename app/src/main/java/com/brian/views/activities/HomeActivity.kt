package com.brian.views.activities

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Gravity.LEFT
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.R
import com.brian.base.ScopedActivity
import com.brian.databinding.ActivityMainBinding
import com.brian.views.NavigationItem
import com.brian.views.adapters.NavigationItemAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class HomeActivity : ScopedActivity(), NavController.OnDestinationChangedListener {

    lateinit var mBinding: ActivityMainBinding
    lateinit var navController: NavController
    val mClickHandler by lazy { ClickHandler() }
    var itemsList = ArrayList<NavigationItem>()
    var itemAdapter: NavigationItemAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.clickHandler = mClickHandler
        navController = Navigation.findNavController(this, R.id.main_dash_fragment)
        navController.addOnDestinationChangedListener(this)
        setAdapter()


    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        if (destination.id == R.id.pitcherFragment || destination.id == R.id.questionsFragment || destination.id == R.id.gameSummaryFragment
            || destination.id == R.id.buzzFeedDetailsFragment || destination.id == R.id.usersFragment) {
            mBinding.toolbar.visibility = GONE
        } else {
            mBinding.toolbar.visibility = View.VISIBLE

        }

        if(destination.id == R.id.myFriendsFragment){
            mBinding.toolbar.iAdd.visibility= VISIBLE

        }else{
            mBinding.toolbar.iAdd.visibility= GONE

        }

    }


    fun setAdapter() {

        mBinding.recycler.layoutManager = LinearLayoutManager(this)

        //Add Items
        itemsList.add(
            NavigationItem(
                R.drawable.ic_defensive,
                getString(R.string.defensive_situation),
                true
            )
        )
        itemsList.add(
            NavigationItem(
                R.drawable.ic_youtube1,
                getString(R.string.training_videos),
                false
            )
        )
        itemsList.add(NavigationItem(R.drawable.ic_buzz, getString(R.string.buzz_feed), false))
        itemsList.add(
            NavigationItem(
                R.drawable.ic_my_friends2,
                getString(R.string.my_friends),
                false
            )
        )
        itemsList.add(NavigationItem(R.drawable.ic_message1, getString(R.string.mesages), false))
        itemsList.add(
            NavigationItem(
                R.drawable.ic_defensive,
                getString(R.string.challenges),
                false
            )
        )
        itemsList.add(NavigationItem(R.drawable.ic_teams1, getString(R.string.teams), false))
        itemsList.add(NavigationItem(R.drawable.ic_stats2, getString(R.string.mystats), false))
        itemsList.add(
            NavigationItem(
                R.drawable.ic_leaderboard1,
                getString(R.string.leaderboards),
                false
            )
        )
        itemsList.add(NavigationItem(R.drawable.ic_phone1, getString(R.string.contact_us), false))
        itemsList.add(
            NavigationItem(
                R.drawable.ic_my_profile1,
                getString(R.string.my_profile),
                false
            )
        )


        itemAdapter = NavigationItemAdapter(R.layout.navigation_item, this)
        mBinding.recycler.adapter = itemAdapter
        itemAdapter!!.listener = this.mClickHandler
        itemAdapter!!.addNewItems(itemsList)


    }


    inner class ClickHandler : NavigationItemAdapter.onClickItem {

        fun onClickNavigation() {
            mBinding.drawerLayout.openDrawer(LEFT)

        }

        fun onClickAdd() {
            if(navController.currentDestination?.id == R.id.myFriendsFragment){
                navController.navigate(R.id.usersFragment)

            }

        }

        override fun onClick(position: Int, item: NavigationItem) {

            Log.e("OnClick", "OnClick")

            for (i in 0 until itemsList.size) {
                itemsList[i].isSelected = i == position
            }
            itemAdapter!!.notifyDataSetChanged()


            navigateFragments(item.text)


        }


    }

    private fun navigateFragments(text: String) {
        drawerLayout.closeDrawer(Gravity.LEFT)
        mBinding.tvTitle.setText(text)
        when (text) {
            getString(R.string.defensive_situation) -> navController.navigate(R.id.homeFragment)
            getString(R.string.training_videos) -> navController.navigate(R.id.trainingVideosFragment)
            getString(R.string.buzz_feed) -> navController.navigate(R.id.buzzFeedFragment)
            getString(R.string.my_friends) -> navController.navigate(R.id.myFriendsFragment)
            getString(R.string.mesages) -> navController.navigate(R.id.messagesFragment)
        }
    }


    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            finish()
        }

    }
}



