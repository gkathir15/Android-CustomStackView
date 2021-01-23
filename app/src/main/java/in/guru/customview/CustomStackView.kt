package `in`.guru.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.RelativeLayout

/**
 * TODO: document your custom view class.
 */
class CustomStackView : RelativeLayout {


    var inflater: LayoutInflater? = null
    var parentLayout: View? = null

    private var viewState = ViewState.COLLAPSED

    private var childStack1: ChildStack? = null
    private var childStack2: ChildStack? = null
    private var childStack3: ChildStack? = null


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

    private fun initAttributes(context: Context?, attrs: AttributeSet?) {
        context?.obtainStyledAttributes(attrs, R.styleable.CustomStackView).also {

            childStack1?.attachHeaderLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_header_layout1,
                    R.layout.header_lay
                )
            )?.setOnClickListener {  changeState1(!childStack1!!.isExpanded) }
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
                changeState2(!childStack2!!.isExpanded)
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
                changeState3(!childStack2!!.isExpanded)
            }

            childStack3?.attachBodyLayout(
                it!!.getResourceId(
                    R.styleable.CustomStackView_content_layout3,
                    R.layout.body_lay
                )
            )

            viewState = ViewState.values()[it!!.getInt(R.styleable.CustomStackView_ViewState, 0)]

        }
    }


    fun initViews() {


        inflater = LayoutInflater.from(context)
        parentLayout = inflater?.inflate(R.layout.custom_parent_lay, this)

        childStack1 = ChildStack(
            parentLayout?.findViewById<ViewStub>(R.id.header1),
            parentLayout?.findViewById<ViewStub>(R.id.body1), false
        )

        childStack2 = ChildStack(
            parentLayout?.findViewById<ViewStub>(R.id.header2),
            parentLayout?.findViewById<ViewStub>(R.id.body2), false
        )

        childStack3 = ChildStack(
            parentLayout?.findViewById<ViewStub>(R.id.header3),
            parentLayout?.findViewById<ViewStub>(R.id.body3), false
        )


        childStack1?.headerView?.setOnClickListener {

           changeState1(!childStack1!!.isExpanded)
        }

        childStack2?.headerView?.setOnClickListener {
            changeState2(!childStack2!!.isExpanded)
        }

        childStack3?.headerView?.setOnClickListener {
            changeState3(!childStack3!!.isExpanded)
        }


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


    private fun changeState1(state: Boolean) {
        childStack1?.also {
            it.isExpanded = state
            if (!state)
                it.bodyView?.visibility = GONE
            else {
                it.bodyView?.visibility = VISIBLE
                changeState2(false)
                changeState3(false)
            }
        }
    }

    private fun changeState2(state: Boolean) {
        childStack2?.also {
            it.isExpanded = state
            if (!state)
                it.bodyView?.visibility = GONE
            else {
                it.bodyView?.visibility = VISIBLE
                changeState1(false)
                changeState3(false)
            }
        }
    }

    private fun changeState3(state: Boolean) {
        childStack3?.also {
            it.isExpanded = state
            if (!state)
                it.bodyView?.visibility = GONE
            else {
                it.bodyView?.visibility = VISIBLE
                changeState1(false)
                changeState2(false)
            }
        }
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

private fun View?.setLayout(layoutId: Int): View? {
    (this as ViewStub).also {
        it.layoutResource = layoutId
        return it.inflate()
    }
}

enum class ViewState {
    EXPANDED, COLLAPSED, EXPAND_FIRST,
}