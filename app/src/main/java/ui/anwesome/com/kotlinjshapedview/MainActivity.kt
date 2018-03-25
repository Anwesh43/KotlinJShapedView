package ui.anwesome.com.kotlinjshapedview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.jshapedview.JShapedView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JShapedView.create(this)
    }
}
