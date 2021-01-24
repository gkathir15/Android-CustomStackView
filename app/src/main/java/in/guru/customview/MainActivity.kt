package `in`.guru.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {
    private var stackView:CustomStackView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stackView = findViewById(R.id.stackView)



//        stackView?.also { // methods exposed by the custom View
//            it.moveToTop()
//            it.changeState1(true)
//            it.changeState1(true)
//            it.changeState1(true)
//            it.setLayoutStack(R.layout.header_lay,true,1)
//            it.getView(true,2)
//
//        }



    }
}