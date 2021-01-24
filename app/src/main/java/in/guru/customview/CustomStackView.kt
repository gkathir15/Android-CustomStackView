package `in`.guru.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.ScrollView


/**
 * Custom stack view that will hold up-to 3 stacks,
 * An header and a body Content view , the Header has ClickListener with hide and show on clicked.
 * The Custom view has three states,
 *  EXPANDED - All three stacks have their body view shown,
 *  COLLAPSED - All the three stacks have their body view hidden,
 *  EXPAND_FIRST - The first view is Expanded,The rest two views are hidden.
 *
 * Each Child/Stack has a state either Expanded or collapsed,specified by a Boolean.
 *
 *  Assumptions:
 *  - There will be always three stacks.
 *  - One stack or all three combined could be higher than the viewport, So wrapped in a ScrollView.
 *  - Layouts can be added via XML or programmatically.
 *  - No more than 3 stacks can be added,Since in case of uncertainty of the number of stacks,something like a collapsible recycleView can be used.
 *
 *
 */
class CustomStackView : RelativeLayout {


    private var inflater: LayoutInflater? = null
    private var parentLayout: ScrollView? = null

    private var viewState = ViewState.COLLAPSED

    private var childStack1: ChildStack? = null
    private var childStack2: ChildStack? = null
    private var childStack3: ChildStack? = null

    private var clickListener1: OnClickListener?=null
    private var clickListener2: OnClickListener?=null
    private var clickListener3: OnClickListener?=null


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initViews()
        initAttributes(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews()
        initAttributes(context, attrs)
    }

    /**
     * Method called on the constructor, Fetches the values from typed array specified in XML.
     * @param context view context passed on from the constructor
     * @param attrs AttributeSet passed on from the constructor.
     */
    private fun initAttributes(context: Context?, attrs: AttributeSet?) {
        context?.obtainStyledAttributes(attrs, R.styleable.CustomStackView).also {

            childStack1?.attachHeaderLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_header_layout1,
                    R.layout.header_lay
                )
            )?.setOnClickListener {clickListener1 }
            childStack1?.attachBodyLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_content_layout1,
                    R.layout.body_lay
                )
            )

            childStack2?.attachHeaderLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_header_layout2,
                    R.layout.header_lay
                )
            )?.setOnClickListener {
                clickListener2
            }
            childStack2?.attachBodyLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_content_layout2,
                    R.layout.body_lay
                )
            )

            childStack3?.attachHeaderLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_header_layout3,
                    R.layout.header_lay
                )
            )?.setOnClickListener {
                clickListener3
            }

            childStack3?.attachBodyLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_content_layout3,
                    R.layout.body_lay
                )
            )

            viewState = ViewState.values()[it!!.getInt(R.styleable.CustomStackView_ViewState, 0)]

            when (viewState) {
                ViewState.EXPANDED -> {
                    childStack1?.bodyView?.visibility = VISIBLE
                    childStack2?.bodyView?.visibility = VISIBLE
                    childStack3?.bodyView?.visibility = VISIBLE

                    childStack1?.isExpanded = true
                    childStack2?.isExpanded = true
                    childStack3?.isExpanded = true
                }
                ViewState.COLLAPSED -> {
                    childStack2?.bodyView?.visibility = GONE
                    childStack3?.bodyView?.visibility = GONE
                    childStack1?.bodyView?.visibility = GONE

                    childStack1?.isExpanded = false
                    childStack2?.isExpanded = false
                    childStack3?.isExpanded = false
                }
                ViewState.EXPAND_FIRST -> {
                    childStack2?.bodyView?.visibility = GONE
                    childStack3?.bodyView?.visibility = GONE
                    childStack1?.bodyView?.visibility = VISIBLE

                    childStack1?.isExpanded = true
                    childStack2?.isExpanded = false
                    childStack3?.isExpanded = false
                }
            }
        }
    }


    /**
     * init method to initialize view related, only called on the constructor.
     */
    private fun initViews() {


        inflater = LayoutInflater.from(context)
        parentLayout = inflater?.inflate(R.layout.custom_parent_lay, this) as ScrollView?

        childStack1 = ChildStack(
            parentLayout?.findViewById(R.id.header1),
            parentLayout?.findViewById(R.id.body1), false
        )

        childStack2 = ChildStack(
            parentLayout?.findViewById(R.id.header2),
            parentLayout?.findViewById(R.id.body2), false
        )

        childStack3 = ChildStack(
            parentLayout?.findViewById(R.id.header3),
            parentLayout?.findViewById(R.id.body3), false
        )



        clickListener1 = OnClickListener {
            changeState1(!childStack1!!.isExpanded)
        }
        clickListener1 = OnClickListener {
            changeState2(!childStack2!!.isExpanded)
        }
        clickListener1 = OnClickListener {
            changeState3(!childStack3!!.isExpanded)
        }

    }


    /**
     * changes state of the first stack
     * @param state specifies either VISIBLE or GONE
     */
     fun changeState1(state: Boolean) {
        childStack1?.also {
            it.isExpanded = state
            if (!state)
                it.bodyView?.visibility = GONE
            else {
                it.bodyView?.visibility = VISIBLE
                changeState2(false)
                changeState3(false)
                moveToTop()
            }
        }
    }

    /**
     * changes state of the second stack
     * @param state specifies either VISIBLE or GONE
     */
     fun changeState2(state: Boolean) {
        childStack2?.also {
            it.isExpanded = state
            if (!state)
                it.bodyView?.visibility=GONE
            else {
                it.bodyView?.visibility = VISIBLE
                changeState1(false)
                changeState3(false)
                moveToTop()
            }
        }
    }

    /**
     * changes state of the third stack
     * @param state specifies either VISIBLE or GONE
     */
     fun changeState3(state: Boolean) {
        childStack3?.also {
            it.isExpanded = state
            if (!state)
                it.bodyView?.visibility=GONE
            else {
                it.bodyView?.visibility = VISIBLE
                changeState1(false)
                changeState2(false)
                moveToTop()
            }
        }
    }

    /**
     * To set layout at Runtime.
     * sets the new layout to the ViewStub,and if incase of a header view, sets ClickListener to it.
     * @param layoutId R.id of the layout
     * @param isHeader to Check if the view is a header, in case it is, sets corresponding listener to it.
     * @param index index ranging from 1,2,3 to identify the stack.
     */
    fun setLayoutStack(layoutId:Int,isHeader:Boolean,index:Int)
    {
        when(index)
        {
            1->{
                if(isHeader)
                    childStack1?.headerView.setLayout(layoutId)?.setOnClickListener { clickListener1 }
                else
                    childStack1?.bodyView.setLayout(layoutId)
            }
            2->{
                if(isHeader)
                    childStack2?.headerView.setLayout(layoutId)?.setOnClickListener { clickListener2 }
                else
                    childStack2?.bodyView.setLayout(layoutId)
            }
            3->{
                if(isHeader)
                    childStack3?.headerView.setLayout(layoutId)?.setOnClickListener { clickListener3 }
                else
                    childStack3?.bodyView.setLayout(layoutId)
            }
        }

    }

    /**
     * Scrolls to top of the view when expanded.
     */
    fun moveToTop()
    {
        parentLayout?.scrollTo(0,0)
    }
}



class ChildStack(pHeaderView: ViewStub?, pBodyView: ViewStub?, isExpanded: Boolean) {
    var attachedHeaderView: View? = null
    var attachedBodyView: View? = null

    var headerView: View? = pHeaderView
    var bodyView: View? = pBodyView
    var isExpanded: Boolean = false

    fun attachHeaderLayout(layoutId: Int): View? {
        attachedHeaderView = headerView.setLayout(layoutId)
       return attachedHeaderView
    }

    fun attachBodyLayout(layoutId: Int) {
        attachedBodyView = bodyView.setLayout(layoutId)
    }
}

/**
 * Extension method to attach layout to the stub
 * ie replaces this StubbedView in its parent by the inflated layout resource..
 * @param layoutId R.id of the layout
 * @return View the inflated View.
 */
private fun View?.setLayout(layoutId: Int): View? {
    (this as ViewStub).also {
        it.layoutResource = layoutId
        return it.inflate()
    }
}

enum class ViewState {
    EXPANDED, COLLAPSED, EXPAND_FIRST,
}