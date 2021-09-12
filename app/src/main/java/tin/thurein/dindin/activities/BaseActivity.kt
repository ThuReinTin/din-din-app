package tin.thurein.dindin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hwangjr.rxbus.RxBus

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.get().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }
}