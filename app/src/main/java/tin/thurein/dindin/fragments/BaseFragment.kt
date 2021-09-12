package tin.thurein.dindin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hwangjr.rxbus.RxBus

open class BaseFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.get().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }
}