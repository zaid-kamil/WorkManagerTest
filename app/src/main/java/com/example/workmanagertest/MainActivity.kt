package com.example.workmanagertest

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.workmanagertest.ui.theme.WorkManagerTestTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkManagerTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val notifPermState =
                        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
                    if (!notifPermState.status.isGranted) {
                        LaunchedEffect(key1 = true){
                            notifPermState.launchPermissionRequest()
                        }
                    } else {
                        LaunchedEffect(key1 = Unit) {
                            val workRequest = OneTimeWorkRequestBuilder<CustomWorker1>().build()
                            val workManager = WorkManager.getInstance(applicationContext)
                            workManager.enqueue(workRequest)
                        }
                    }
                }
            }
        }
    }
}


class CustomWorker1(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            delay(10000)
            // show notification
            showNotification(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

fun showNotification(context: Context) {
    val notification = NotificationCompat.Builder(
        context, "work_manager_test"
    ).setContentTitle("Work Manager Test")
        .setContentText("Work Manager Test")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(1, notification)
}
