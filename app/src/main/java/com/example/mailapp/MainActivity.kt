package com.example.mailapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var emails: MutableList<Email>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Status bar wasn't visible, so this worked to fix it.
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // This is to define clear limits for the app. So it doesn't go below the button or above the camera.
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Lookup the RecyclerView in activity layout
        val emailsRv = findViewById<RecyclerView>(R.id.emailsRv)
        // Fetch the list of emails
        emails = EmailFetcher.getEmails()
        // Create adapter passing in the list of emails
        val adapter = EmailAdapter(emails)
        // Attach the adapter to the RecyclerView to populate items
        emailsRv.adapter = adapter
        // Set layout manager to position the items
        emailsRv.layoutManager = LinearLayoutManager(this)

        // Fetch next 5 emails when "Load More" button is clicked
        val loadMoreBtn = findViewById<Button>(R.id.loadMoreBtn)
        loadMoreBtn.setOnClickListener {
            val previousSize = emails.size // Grab current size before adding new items
            val newEmails = EmailFetcher.getNext5Emails()
            emails.addAll(newEmails)
            adapter.notifyItemRangeInserted(previousSize, newEmails.size)

            val text = "5 New Emails Loaded"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this, text, duration)
            toast.show()

            // With the code provided on Codelab, they wanted to use notifyDataSetChanged()
            // Which according to Android Studio is inefficient, and it prefers using this.
            // I did notice this approach looked smoother visually.
        }
    }
}