package com.brian.views.activities


import android.os.Bundle
import android.view.Gravity
import android.view.Gravity.LEFT
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedActivity
import com.brian.databinding.ActivityMainBinding
import com.brian.views.NavigationItem
import com.brian.views.adapters.NavigationItemAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.header_layout.*


class HomeActivity : ScopedActivity(), NavController.OnDestinationChangedListener {

    lateinit var mBinding: ActivityMainBinding
    lateinit var navController: NavController
    val mClickHandler by lazy { ClickHandler() }
    var itemsList = ArrayList<NavigationItem>()
    var itemAdapter: NavigationItemAdapter? = null
    var ivProfile: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.clickHandler = mClickHandler
        navController = Navigation.findNavController(this, R.id.main_dash_fragment)
        navController.addOnDestinationChangedListener(this)
        setAdapter()
        setDrawer()
        ivProfile =
            mBinding.navigationView.getHeaderView(0).findViewById<ImageView>(R.id.profilePic)
        ivProfile?.let {
            Glide.with(this@HomeActivity).load(Prefs.init().userInfo?.profilePicture)
                .error(R.drawable.ic_use_r).into(it)
        }
    }


    private fun setDrawer() {
        val toggel = object : ActionBarDrawerToggle(
            this,
            mBinding.drawerLayout,
            R.string.app_name,
            R.string.app_name
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val moveFactor: Float = mBinding.recycler.getWidth() * slideOffset
                mBinding.frameLayout.setTranslationX(moveFactor)
                ivProfile?.let {
                    Glide.with(this@HomeActivity).load(Prefs.init().userInfo?.profilePicture)
                        .error(R.drawable.ic_use_r).into(it)
                }
            }
        }

        mBinding.drawerLayout.setDrawerListener(toggel)

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        if (destination.id == R.id.videoViewFragment || destination.id == R.id.pitcherFragment || destination.id == R.id.homeFragment || destination.id == R.id.trainingVideosFragment || destination.id == R.id.questionsFragment || destination.id == R.id.gameSummaryFragment
            || destination.id == R.id.buzzFeedDetailsFragment || destination.id == R.id.usersFragment
            || destination.id == R.id.createChallengeFragment || destination.id == R.id.userProfileFragment
            || destination.id == R.id.challenegeFragment || destination.id == R.id.createTeamFragment
            || destination.id == R.id.changePasswordFragment || destination.id == R.id.myChallengesFragment
            || destination.id == R.id.challengeType || destination.id == R.id.myTeams || destination.id == R.id.chatFragment || destination.id == R.id.register || destination.id == R.id.myTeamfragment
        ) {
            mBinding.toolbar.visibility = GONE
        } else {
            mBinding.toolbar.visibility = View.VISIBLE
        }

        if (destination.id == R.id.videoViewFragment) {
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        }

        if (destination.id == R.id.myFriendsFragment || destination.id == R.id.messagesFragment || destination.id == R.id.challenegesFragment || destination.id == R.id.teamFragment) {
            mBinding.toolbar.iAdd.visibility = VISIBLE

        } else {
            mBinding.toolbar.iAdd.visibility = GONE

        }
        setDestinationName(destination.id)
    }


    fun setDestinationName(id: Int) {
        when (id) {
            R.id.mainScreenFragment -> mBinding.toolbar.tvTitle.text =
                getString(R.string.home_toolbar)
            R.id.trainingVideosFragment -> mBinding.toolbar.tvTitle.text =
                getString(R.string.training_videos)
            R.id.buzzFeedFragment -> mBinding.toolbar.tvTitle.text =
                getString(R.string.buzz_feed_title)
            R.id.myFriendsFragment -> mBinding.toolbar.tvTitle.text = getString(R.string.my_friends)
            R.id.messagesFragment -> mBinding.toolbar.tvTitle.text = getString(R.string.mesages)
            R.id.challenegesFragment -> mBinding.toolbar.tvTitle.text =
                getString(R.string.challenges)
            R.id.teamFragment -> mBinding.toolbar.tvTitle.text = getString(R.string.teams)
            R.id.myStatsFragment -> mBinding.toolbar.tvTitle.text = getString(R.string.mystats)
            R.id.leaderBoards -> mBinding.toolbar.tvTitle.text = getString(R.string.leaderboards)
            R.id.contactUsFragment -> mBinding.toolbar.tvTitle.text = getString(R.string.contact_us)
            R.id.myProfileFragment -> mBinding.toolbar.tvTitle.text = getString(R.string.my_profile)
        }
    }


    private fun setAdapter() {
        mBinding.recycler.layoutManager = LinearLayoutManager(this)
        val userInfo = Prefs.init().userInfo

        //Add Items
        itemsList.add(
            NavigationItem(
                R.drawable.ic_defensive,
                getString(R.string.Home),
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
        itemsList.add(
            NavigationItem(
                R.drawable.ic_message1,
                getString(R.string.mesages),
                false
            )
        )
        itemsList.add(
            NavigationItem(
                R.drawable.ic_challenge,
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
        itemsList.add(
            NavigationItem(
                R.drawable.ic_phone1,
                getString(R.string.contact_us),
                false
            )
        )
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
            mBinding.root.let {
                hideKeyboard(it)
            }
            val login = Prefs.init().userInfo
            name.text = login?.name
            mBinding.drawerLayout.openDrawer(LEFT)
        }

        fun onClickAdd() {
            if (navController.currentDestination?.id == R.id.myFriendsFragment) {
                navController.navigate(
                    R.id.usersFragment,
                    bundleOf(getString(R.string.challenge_type) to getString(R.string.no))
                )

            }
            if (navController.currentDestination?.id == R.id.challenegesFragment) {
                navController.navigate(
                    R.id.createChallengeFragment, bundleOf(
                        getString(
                            R.string.new_challenge
                        ) to getString(R.string.yes)
                    )
                )

            }

            if (navController.currentDestination?.id == R.id.teamFragment) {
                navController.navigate(R.id.createTeamFragment)

            }
            if (navController.currentDestination?.id == R.id.messagesFragment) {
                navController.navigate(
                    R.id.usersFragment,
                    bundleOf(
                        getString(R.string.register_message) to getString(R.string.yes),
                        getString(R.string.no) to getString(R.string.no)
                    )
                )

            }

        }

        override fun onClick(position: Int, item: NavigationItem) {
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
            getString(R.string.home_toolbar) -> navController.navigate(R.id.mainScreenFragment)
            getString(R.string.training_videos) -> navController.navigate(R.id.getStartTrainingFragment)
            getString(R.string.buzz_feed) -> navController.navigate(R.id.buzzFeedFragment)
            getString(R.string.my_friends) -> navController.navigate(R.id.myFriendsFragment)
            getString(R.string.mesages) -> navController.navigate(R.id.messagesFragment)
            getString(R.string.challenges) -> navController.navigate(R.id.challenegesFragment)
            getString(R.string.my_profile) -> navController.navigate(R.id.myProfileFragment)
            getString(R.string.teams) -> navController.navigate(R.id.teamFragment)
            getString(R.string.mystats) -> navController.navigate(R.id.myStatsFragment)
            getString(R.string.contact_us) -> navController.navigate(R.id.contactUsFragment)
            getString(R.string.leaderboards) -> navController.navigate(R.id.leaderBoards)
        }
    }


    override fun onBackPressed() {


        if (navController.currentDestination?.id == R.id.createChallengeFragment) {
            getIntent()?.removeExtra("id")
            getIntent()?.removeExtra("key")
        }
        if (navController.currentDestination?.id == R.id.createTeamFragment) {
            getIntent()?.removeExtra("key")
        }
        if (navController.currentDestination?.id == R.id.gameSummaryFragment) {
            navController.navigate(R.id.homeFragment)
        } else if (navController.currentDestination?.id == R.id.mainScreenFragment) {
            finish()
        } else if (navController.currentDestination?.id == R.id.homeFragment || navController.currentDestination?.id == R.id.getStartTrainingFragment || navController.currentDestination?.id == R.id.myFriendsFragment ||
            navController.currentDestination?.id == R.id.buzzFeedFragment || navController.currentDestination?.id == R.id.messagesFragment || navController.currentDestination?.id == R.id.challenegesFragment ||
            navController.currentDestination?.id == R.id.teamFragment || navController.currentDestination?.id == R.id.myStatsFragment || navController.currentDestination?.id == R.id.leaderBoards ||
            navController.currentDestination?.id == R.id.contactUsFragment || navController.currentDestination?.id == R.id.myProfileFragment
        ) {

            itemAdapter?.notifyDataSetChanged()
            navController.navigate(R.id.mainScreenFragment)

        } else if (!navController.navigateUp()) {
            finish()
        }

        if (navController.currentDestination?.id == R.id.createTeamFragment) {
            getIntent()?.removeExtra("id")
            getIntent()?.removeExtra("key")
        }
    }

}



