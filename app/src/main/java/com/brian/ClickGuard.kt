//
//package com.brian
//
//import android.os.Build
//import android.os.Handler
//import android.os.Looper
//import android.view.View
//import com.brian.ClickGuard.GuardedOnClickListener
//import java.lang.reflect.Field
//
//class Guard {
//}
//
//
///**
// * Class used to guard a view to avoid multiple rapid clicks.
// *
// *
// * Guarding a view is as easy as:
// * <pre>`
// * ClickGuard.guard(view);
//`</pre> *
// *
// *
// * Or:
// * <pre>`
// * ClickGuard.newGuard().add(view);
//`</pre> *
// *
// *
// * When a guarded view is clicked, the view will be watched for a period of time from that moment.
// * All the upcoming click events will be ignored until the watch period ends.
// *
// *
// * By default, watch period is 1000 milliseconds. You can create a ClickGuard using specify watch
// * period like this:
// * <pre>`
// * ClickGuard.newGuard(600); // Create a ClickGuard with 600ms watch period.
//`</pre> *
// *
// *
// * Multiple views can be guarded by a ClickGuard simultaneously:
// * <pre>`
// * ClickGuard.guard(view1, view2, view3);
//`</pre> *
// *
// *
// * When multiple views are guarded by one ClickGuard, the first click on a view will trigger this
// * ClickGuard to watch. And all upcoming clicks on any of the guarded views will be ignored until
// * the watch period ends.
// *
// *
// * Another way to guard a view is using a [GuardedOnClickListener]
// * instead of [OnClickListener][android.view.View.OnClickListener]:
// * <pre>`
// * button.setOnClickListener(new GuardedOnClickListener() {
// * @Override
// * public boolean onClicked() {
// * // React to button click.
// * return true;
// * }
// * });
//`</pre> *
// *
// *
// * Using static [wrap][.wrap] method can simply make an
// * exist [OnClickListener][android.view.View.OnClickListener] to be a [ ]:
// * <pre>`
// * button.setOnClickListener(ClickGuard.wrap(onClickListener));
//`</pre> *
// */
//abstract class ClickGuard private constructor() {
//    // ---------------------------------------------------------------------------------------------
//    //                                  Utility methods end
//    // ---------------------------------------------------------------------------------------------
//    /**
//     * Let a view to be guarded by this ClickGuard.
//     *
//     * @param view The view to be guarded.
//     * @return This ClickGuard instance.
//     * @see .addAll
//     */
//    fun add(view: View?): ClickGuard {
//        requireNotNull(view) { "View shouldn't be null!" }
//        val listener = retrieveOnClickListener(view)
//            ?: throw IllegalStateException(
//                "Haven't set an OnClickListener to View (id: 0x"
//                        + Integer.toHexString(view.id) + ")!"
//            )
//        view.setOnClickListener(wrapOnClickListener(listener))
//        return this
//    }
//
//    /**
//     * Like [.add]. Let a series of views to be guarded by this ClickGuard.
//     *
//     * @param view   The view to be guarded.
//     * @param others More views to be guarded.
//     * @return This ClickGuard instance.
//     * @see .add
//     */
//    fun addAll(view: View?, vararg others: View?): ClickGuard {
//        add(view)
//        for (v in others) {
//            add(v)
//        }
//        return this
//    }
//
//    /**
//     * Like [.add]. Let a series of views to be guarded by this ClickGuard.
//     *
//     * @param views The views to be guarded.
//     * @return This ClickGuard instance.
//     * @see .add
//     */
//    fun addAll(views: Iterable<View?>): ClickGuard {
//        for (v in views) {
//            add(v)
//        }
//        return this
//    }
//
//    /**
//     * Let the provided [android.view.View.OnClickListener] to be a [GuardedOnClickListener]
//     * which will be guarded by this ClickGuard.
//     *
//     * @param onClickListener onClickListener
//     * @return A GuardedOnClickListener instance.
//     */
//    fun wrapOnClickListener(onClickListener: View.OnClickListener?): GuardedOnClickListener {
//        requireNotNull(onClickListener) { "onClickListener shouldn't be null!" }
//        require(onClickListener !is GuardedOnClickListener) { "Can't wrap GuardedOnClickListener!" }
//        return InnerGuardedOnClickListener(onClickListener, this)
//    }
//
//    /**
//     * Let the Guard to start watching.
//     */
//    abstract fun watch()
//
//    /**
//     * Let the Guard to have a rest.
//     */
//    abstract fun rest()
//
//    /**
//     * Determine whether the Guard is on duty.
//     *
//     * @return Whether the Guard is watching.
//     */
//    abstract val isWatching: Boolean
//
//    private abstract class ClickGuardImpl internal constructor(private val mWatchPeriodMillis: Long) :
//        ClickGuard() {
//        private val mHandler = Handler(Looper.getMainLooper())
//        override fun watch() {
//            mHandler.sendEmptyMessageDelayed(WATCHING, mWatchPeriodMillis)
//        }
//
//        override fun rest() {
//            mHandler.removeMessages(WATCHING)
//        }
//
//        override fun isWatching(): Boolean {
//            return mHandler.hasMessages(WATCHING)
//        }
//
//        companion object {
//            private const val WATCHING = 0
//        }
//    }
//
//    /**
//     * OnClickListener which can avoid multiple rapid clicks.
//     */
//    abstract class GuardedOnClickListener internal constructor(
//        private val mWrapped: View.OnClickListener?,
//        val clickGuard: ClickGuard
//    ) :
//        View.OnClickListener {
//        @JvmOverloads
//        constructor(watchPeriodMillis: Long = DEFAULT_WATCH_PERIOD_MILLIS) : this(
//            newGuard(
//                watchPeriodMillis
//            )
//        ) {
//        }
//
//        constructor(guard: ClickGuard) : this(null, guard) {}
//
//        override fun onClick(v: View) {
//            if (clickGuard.isWatching) {
//                // Guard is guarding, can't do anything.
//                onIgnored()
//                return
//            }
//            // Guard is relaxing. Run!
//            mWrapped?.onClick(v)
//            if (onClicked()) {
//                // Guard becomes vigilant.
//                clickGuard.watch()
//            }
//        }
//
//        /**
//         * Called when a click is allowed.
//         *
//         * @return If `true` is returned, the host view will be guarded. All click events in
//         * the upcoming watch period will be ignored. Otherwise, the next click will not be ignored.
//         */
//        abstract fun onClicked(): Boolean
//
//        /**
//         * Called when a click is ignored.
//         */
//        open fun onIgnored() {}
//
//    }
//
//    // Inner GuardedOnClickListener implementation.
//    internal class InnerGuardedOnClickListener(
//        onClickListener: View.OnClickListener?,
//        guard: ClickGuard
//    ) :
//        GuardedOnClickListener(onClickListener, guard) {
//        override fun onClicked(): Boolean {
//            return true
//        }
//
//        override fun onIgnored() {}
//    }
//
//    /**
//     * Class used for retrieve OnClickListener from a View.
//     */
//    internal abstract class ListenerGetter {
//        companion object {
//            private var IMPL: ListenerGetter? = null
//            operator fun get(view: View?): View.OnClickListener {
//                return IMPL!!.getOnClickListener(view)
//            }
//
//            fun getField(clazz: Class<*>, fieldName: String): Field {
//                return try {
//                    clazz.getDeclaredField(fieldName)
//                } catch (ignored: NoSuchFieldException) {
//                    throw RuntimeException("Can't get " + fieldName + " of " + clazz.name)
//                }
//            }
//
//            fun getField(className: String, fieldName: String): Field {
//                return try {
//                    getField(Class.forName(className), fieldName)
//                } catch (ignored: ClassNotFoundException) {
//                    throw RuntimeException("Can't find class: $className")
//                }
//            }
//
//            fun getFieldValue(field: Field, `object`: Any?): Any? {
//                try {
//                    return field[`object`]
//                } catch (ignored: IllegalAccessException) {
//                }
//                return null
//            }
//
//            init {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                    IMPL = ListenerGetterIcs()
//                } else {
//                    IMPL = ListenerGetterBase()
//                }
//            }
//        }
//
//        abstract fun getOnClickListener(view: View?): View.OnClickListener
//        private class ListenerGetterBase internal constructor() : ListenerGetter() {
//            private val mOnClickListenerField: Field
//            override fun getOnClickListener(view: View?): View.OnClickListener? {
//                return getFieldValue(mOnClickListenerField, view) as View.OnClickListener?
//            }
//
//            init {
//                mOnClickListenerField = getField(View::class.java, "mOnClickListener")
//            }
//        }
//
//        private class ListenerGetterIcs internal constructor() : ListenerGetter() {
//            private val mListenerInfoField: Field
//            private val mOnClickListenerField: Field
//            override fun getOnClickListener(view: View?): View.OnClickListener? {
//                val listenerInfo = getFieldValue(mListenerInfoField, view)
//                return if (listenerInfo != null) getFieldValue(
//                    mOnClickListenerField,
//                    listenerInfo
//                ) as View.OnClickListener? else null
//            }
//
//            init {
//                mListenerInfoField = getField(View::class.java, "mListenerInfo")
//                mListenerInfoField.isAccessible = true
//                mOnClickListenerField =
//                    getField("android.view.View\$ListenerInfo", "mOnClickListener")
//            }
//        }
//    }
//
//    companion object {
//        /**
//         * Default watch period in millis.
//         */
//        const val DEFAULT_WATCH_PERIOD_MILLIS = 1000L
//        /**
//         * Utility method. Create a ClickGuard with specific watch period: `watchPeriodMillis`.
//         *
//         * @return The created ClickGuard instance.
//         */
//        // ---------------------------------------------------------------------------------------------
//        //                                  Utility methods start
//        // ---------------------------------------------------------------------------------------------
//        /**
//         * Utility method. Create a ClickGuard with default watch period: [.DEFAULT_WATCH_PERIOD_MILLIS].
//         *
//         * @return The created ClickGuard instance.
//         */
//        @JvmOverloads
//        fun newGuard(watchPeriodMillis: Long = DEFAULT_WATCH_PERIOD_MILLIS): ClickGuard {
//            return ClickGuardImpl(watchPeriodMillis)
//        }
//
//        /**
//         * Utility method. Let the provided [OnClickListener][android.view.View.OnClickListener]
//         * to be a [GuardedOnClickListener]. Use a new guard with default
//         * watch period: [.DEFAULT_WATCH_PERIOD_MILLIS].
//         *
//         * @param onClickListener The listener to be wrapped.
//         * @return A GuardedOnClickListener instance.
//         */
//        fun wrap(onClickListener: View.OnClickListener?): GuardedOnClickListener {
//            return wrap(newGuard(), onClickListener)
//        }
//
//        /**
//         * Utility method. Let the provided [OnClickListener][android.view.View.OnClickListener]
//         * to be a [GuardedOnClickListener]. Use a new guard with specific
//         * watch period: `watchPeriodMillis`.
//         *
//         * @param watchPeriodMillis The specific watch period.
//         * @param onClickListener   The listener to be wrapped.
//         * @return A GuardedOnClickListener instance.
//         */
//        fun wrap(
//            watchPeriodMillis: Long,
//            onClickListener: View.OnClickListener?
//        ): GuardedOnClickListener {
//            return newGuard(watchPeriodMillis).wrapOnClickListener(onClickListener)
//        }
//
//        /**
//         * Utility method. Let the provided [OnClickListener][android.view.View.OnClickListener]
//         * to be a [GuardedOnClickListener]. Use specific ClickGuard:
//         * `guard`.
//         *
//         * @param guard           The specific ClickGuard.
//         * @param onClickListener The listener to be wrapped.
//         * @return A GuardedOnClickListener instance.
//         */
//        fun wrap(
//            guard: ClickGuard,
//            onClickListener: View.OnClickListener?
//        ): GuardedOnClickListener {
//            return guard.wrapOnClickListener(onClickListener)
//        }
//
//        /**
//         * Utility method. Use a new ClickGuard with default watch period [.DEFAULT_WATCH_PERIOD_MILLIS]
//         * to guard View(s).
//         *
//         * @param view   The View to be guarded.
//         * @param others More views to be guarded.
//         * @return The created ClickedGuard.
//         */
//        fun guard(view: View?, vararg others: View?): ClickGuard {
//            return guard(DEFAULT_WATCH_PERIOD_MILLIS, view, *others)
//        }
//
//        /**
//         * Utility method. Use a new ClickGuard with specific guard period `watchPeriodMillis` to
//         * guard View(s).
//         *
//         * @param watchPeriodMillis The specific watch period.
//         * @param view              The View to be guarded.
//         * @param others            More Views to be guarded.
//         * @return The created ClickedGuard.
//         */
//        fun guard(watchPeriodMillis: Long, view: View?, vararg others: View?): ClickGuard {
//            return guard(newGuard(watchPeriodMillis), view, *others)
//        }
//
//        /**
//         * Utility method. Use a specific ClickGuard `guard` to guard View(s).
//         *
//         * @param guard  The ClickGuard used to guard.
//         * @param view   The View to be guarded.
//         * @param others More Views to be guarded.
//         * @return The given ClickedGuard itself.
//         */
//        fun guard(guard: ClickGuard, view: View?, vararg others: View?): ClickGuard {
//            return guard.addAll(view, *others)
//        }
//
//        /**
//         * Utility method. Use a new ClickGuard with default watch period [.DEFAULT_WATCH_PERIOD_MILLIS]
//         * to guard a series of Views.
//         *
//         * @param views The Views to be guarded.
//         * @return The created ClickedGuard.
//         */
//        fun guardAll(views: Iterable<View?>): ClickGuard {
//            return guardAll(DEFAULT_WATCH_PERIOD_MILLIS, views)
//        }
//
//        /**
//         * Utility method. Use a new ClickGuard with specific guard period `watchPeriodMillis` to
//         * guard a series of Views.
//         *
//         * @param watchPeriodMillis The specific watch period.
//         * @param views             The Views to be guarded.
//         * @return The created ClickedGuard.
//         */
//        fun guardAll(watchPeriodMillis: Long, views: Iterable<View?>): ClickGuard {
//            return guardAll(newGuard(watchPeriodMillis), views)
//        }
//
//        /**
//         * Utility method. Use a specific ClickGuard `guard` to guard a series of Views.
//         *
//         * @param guard The ClickGuard used to guard.
//         * @param views The Views to be guarded.
//         * @return The given ClickedGuard itself.
//         */
//        fun guardAll(guard: ClickGuard, views: Iterable<View?>): ClickGuard {
//            return guard.addAll(views)
//        }
//
//        /**
//         * Utility method. Get the ClickGuard from a guarded View.
//         *
//         * @param view A View guarded by ClickGuard.
//         * @return The ClickGuard which guards this View.
//         */
//        operator fun get(view: View): ClickGuard {
//            val listener = retrieveOnClickListener(view)
//            if (listener is GuardedOnClickListener) {
//                return listener.clickGuard
//            }
//            throw IllegalStateException("The view (id: 0x" + view.id + ") isn't guarded by ClickGuard!")
//        }
//
//        /**
//         * Utility method. Retrieve [OnClickListener][android.view.View.OnClickListener] from
//         * a View.
//         *
//         * @param view The View used to retrieve.
//         * @return The retrieved [OnClickListener][android.view.View.OnClickListener].
//         */
//        fun retrieveOnClickListener(view: View?): View.OnClickListener {
//            if (view == null) {
//                throw NullPointerException("Given view is null!")
//            }
//            return ListenerGetter[view]
//        }
//    }
//}