package deadCode.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import deadCode.timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private var START_TIME_IN_MILI: Long = 25 * 60 * 1000
        private var remainingTime = START_TIME_IN_MILI
    }

    var timer: CountDownTimer? = null
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        applyBinding()
    }

    // collect all function
    private fun applyBinding() {
        binding.apply {
            btnStart.setOnClickListener {
                if (!isTimerRunning) {
                    startTimer(START_TIME_IN_MILI)
                    tvTitle.text = resources.getText(R.string.keep_going)
                }

            }
        }
        resetTIMER()
    }

    // this fun created to START TIMER
    private fun startTimer(startTimer: Long) {
        timer = object : CountDownTimer(startTimer, 1000) {
            override fun onTick(timeLeft: Long) {
                remainingTime = timeLeft
                updatingTimerText()
                binding.progressBar.progress =
                    remainingTime.toDouble().div(START_TIME_IN_MILI.toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Finish", Toast.LENGTH_SHORT).show()
            }
        }.start()
        isTimerRunning = true

    }

    //this fun created to convert time from mileSecond To Minute and Second
    private fun updatingTimerText() {
        val minute = remainingTime.div(1000).div(60)
        val second = remainingTime.div(1000) % 60
        val formattedTimer = String.format("%02d:%02d", minute, second)
        binding.tvTimer.text = formattedTimer
    }

    //this fun created to RESET TIMER
    private fun resetTIMER() {
        binding.tvReset.setOnClickListener {
            timer?.cancel()
            remainingTime = START_TIME_IN_MILI
            updatingTimerText()
            binding.tvTitle.text = resources.getText(R.string.take_a_pomodoro)
            isTimerRunning = false
            binding.progressBar.progress = 100
            Toast.makeText(this, "Reset time", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("remainingTime", remainingTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTime = savedInstanceState.getLong("remainingTime")

        if (savedTime != START_TIME_IN_MILI)
            startTimer(savedTime)
    }

}