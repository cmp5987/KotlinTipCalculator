package edu.rit.cmp5987.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    //variables used when saving and restoring the state
    private var currentBillTotal = 0.00; //bill ammount entered bt the user
    private var currentCustomPercent = 0 //tip % set with the seek bar

    companion object {
        //constants used when saving / restoring state
        //any class can import a companion object
        private val BILL_TOTAL = "BILL_TOTAL"
        private val CUSTOM_PERCENT = "CUSTOM_PERCENT"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if app just started or is being restored from memory
        if (savedInstanceState == null){
            //app just started running
            //initialize the currentBillTotal and customPerecnt
            currentBillTotal = 0.00
            currentCustomPercent = 18
        }else{
            //app is being restored from memory
            //initialized currentBillTotal and customPercent to the saved amounts
            currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL)
            currentCustomPercent = savedInstanceState.getInt(CUSTOM_PERCENT)
        }

        //billEditTextWatcher handles billEditText's onTextChangeEvent
        billTotalEditText.addTextChangedListener(billEditTextWatcher)

        //get the seek bar value when it changes
        customSeekBar.setOnSeekBarChangeListener(customSeekBarLister)

    } //onCreate

    //update the 10,15,20 percent editTexts
    private fun updateStandard(){

        //calculate build total for 10% tip
        val tenPercentTip = currentBillTotal * .1
        val tenPercentTotal = currentBillTotal + tenPercentTip

        //update the gui
        tipTenEditText.setText(String.format("%.02f", tenPercentTip))
        totalTenEditText.setText(String.format("%.02f", tenPercentTotal))

        //calculate build total for 10% tip
        val fifteenPercentTip = currentBillTotal * .15
        val fifteenPercentTotal = currentBillTotal + fifteenPercentTip

        //update the gui
        tipFifteenEditText.setText(String.format("%.02f", fifteenPercentTip))
        totalFifteenEditText.setText(String.format("%.02f", fifteenPercentTotal))

        //calculate build total for 10% tip
        val twentyPercentTip = currentBillTotal * .20
        val twentyPercentTotal = currentBillTotal + twentyPercentTip

        //update the gui
        tipTwentyEditText.setText(String.format("%.02f", twentyPercentTip))
        totalTwentyEditText.setText(String.format("%.02f", twentyPercentTotal))


    }//updateStandard

    //calculate the custom tip and editTexts
    private fun updateCustom(){
        //set customTipTextView's text to match the position of the seek bar
        customTextView.text = currentCustomPercent.toString() + "%";

        //calculate custom tip amount
        val customTipAmount = currentBillTotal * currentCustomPercent.toDouble() * .01
        
        //calculate the total bill including custom tip
        
        val customTotalAmount = currentBillTotal + customTipAmount

        //update the gui
        tipEditText.setText(String.format("%.02f", customTipAmount))
        totalEditText.setText(String.format("%.0f", customTotalAmount))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //save values of billEditText and customSeekBar
        outState.putDouble(BILL_TOTAL, currentBillTotal)
        outState.putInt(CUSTOM_PERCENT, currentCustomPercent)
    }

    //called when user changes the position of seekbar
    private val customSeekBarLister = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            //update currentCustomPercent, then call updateCustom
            currentCustomPercent = progress // returns int in range 0-100
            updateCustom()
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
            //ignore we aren't using
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            //ignore we aren't using it
        }

    }//customSeekBarListener

    //handles billEditText's events
    private val billEditTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            //ignore this
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //ignore this
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //Called when user enters a number

            //Convert total edit text to double
            try {
                currentBillTotal = s.toString().toDouble()
            }
            catch(e: NumberFormatException){
                currentBillTotal = 0.00 //default if exception occurs
            }

            updateStandard()
            updateCustom()

        }//onTextChanged

    }//billEditTextWatcher
}//activity