package com.kavics.detail

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.kavics.R
import com.kavics.edit.EditKavicActivity
import com.kavics.model.OneTimeKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KAVIC_ITEM = "KAVIC_ITEM"
    }

    var startingTime = 0
    var currentTime = 0L
    lateinit var timer: CountDownTimer
    lateinit var kavicViewModel: KavicViewModel
    lateinit var kavic: OneTimeKavicItem

    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        kavicViewModel = KavicViewModel()
        kavic = intent.getSerializableExtra(EditKavicActivity.KAVIC_ITEM) as OneTimeKavicItem

        textViewTimer.text = kavic.howManySeconds.toString()

        startingTime = kavic.howManySeconds * 1000

        btnStart.setOnClickListener {
            startTimer()
            timerState = TimerState.Running
            updateButtons()

            btnStop.setOnClickListener {
                startingTime = currentTime.toInt()
                textViewTimer.text = startingTime.toString()
                timer.cancel()

                timer.cancel()
                timerState = TimerState.Paused
                updateButtons()
            }

            btnReset.setOnClickListener {
                textViewTimer.text = kavic.howManySeconds.toString()
                startingTime = kavic.howManySeconds
                timer.cancel()

                timer.cancel()
                onTimerFinished()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running) {
            timer.cancel()
            //TODO: start background timer and show notification
        } else if (timerState == TimerState.Paused) {
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)

    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)

    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textViewTimer.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"

    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                btnStart.isEnabled = false
                btnStop.isEnabled = true
                btnReset.isEnabled = true
            }
            TimerState.Stopped -> {
                btnStart.isEnabled = true
                btnStop.isEnabled = false
                btnReset.isEnabled = false
            }
            TimerState.Paused -> {
                btnStart.isEnabled = true
                btnStop.isEnabled = false
                btnReset.isEnabled = true
            }
        }
    }

}