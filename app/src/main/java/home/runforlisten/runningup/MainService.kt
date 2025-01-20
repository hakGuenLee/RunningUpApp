package home.runforlisten.runningup

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.IBinder


//메인 기능을 실시간 구현하는 백그라운드 서비스
//외부와 통신할 필요는 없기 때문에 로컬브로드캐스트 사용

class MainService: Service(), LocationListener {


    override fun onCreate() {
        super.onCreate()
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}