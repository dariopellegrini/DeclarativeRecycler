package com.dariopellegrini.declarativerecyclerdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import com.dariopellegrini.declarativerecycler.BasicRow
import com.dariopellegrini.declarativerecycler.RecyclerManager
import com.dariopellegrini.declarativerecyclerdemo.rows.ProgressRow
import com.dariopellegrini.declarativerecyclerdemo.rows.ResponseRow
import com.dariopellegrini.declarativerecyclerdemo.rows.UserRow
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_card_cell_left.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var isLoading = false

    lateinit var recyclerManager: RecyclerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        recyclerManager = RecyclerManager(recyclerView, layoutManager)

        sendButton.setOnClickListener(View.OnClickListener {
            val message = messageEditText.text.toString().trim()
            if (message.length > 0 && isLoading == false) {
                messageEditText.text = null

                // Pushing a row with a right aligned message (for user message).
                recyclerManager.push(
                        UserRow(
                                message = message,
                                clicked = {
                                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                                },
                                longClicked = { position ->
                                    recyclerManager.remove(position, true, false)
                                })
                        , true, true)

                randomResponse()
            }
        })

        // Add welcome message
        addWelcomeMessage()
    }

    fun addWelcomeMessage() {
        Thread {
            Thread.sleep(1000)

            runOnUiThread {
                val message = "Write whatever you want. I'll write whatever I want."
                // Adding a basic row
                recyclerManager.push(BasicRow(
                        layoutID = R.layout.layout_card_cell_left,
                        configuration = {
                            itemView, position ->
                            itemView.leftMessageTextView.text = message
                            itemView.leftDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"
                        },
                        onClick = {
                            itemView, position ->
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        },
                        onLongClick = {
                            itemView, position ->
                            recyclerManager.remove(position, true, false)
                        }), true, true)
            }

        }.start()
    }

    // Random response
    fun randomResponse() {
        // Pushing a row showing progress indicator.
        recyclerManager.push(ProgressRow(), true)

        isLoading = true

        Thread {
            Thread.sleep(3000)
            runOnUiThread {
                val speech = "Random response nr. ${(Math.random() * 100).toInt()}"

                // Popping last added row (the progress indicator row).
                recyclerManager.pop(true, false)

                // Pushing a row with a left aligned message (for random message).
                recyclerManager.push(
                        ResponseRow(
                                message = speech,
                                onClick = {
                                    Toast.makeText(this, speech, Toast.LENGTH_LONG).show()
                                },
                                onLongClick = { position ->
                                    recyclerManager.remove(position, true, false)
                                }), true, true)
                isLoading = false
            }
        }.start()
    }
}
