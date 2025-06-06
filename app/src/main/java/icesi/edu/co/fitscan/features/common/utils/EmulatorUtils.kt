package icesi.edu.co.fitscan.features.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object EmulatorUtils {
    
    /**
     * Creates a sample bitmap for testing AI functionality on emulators
     * This generates a simple colored rectangle to simulate a person silhouette
     */
    fun createSamplePersonBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Fill background
        canvas.drawColor(Color.WHITE)
        
        val paint = Paint().apply {
            color = Color.parseColor("#FFB74D") // Light skin tone
            isAntiAlias = true
        }
        
        // Draw simple person silhouette
        // Head (circle)
        canvas.drawCircle(256f, 150f, 50f, paint)
        
        // Body (rectangle)
        canvas.drawRect(206f, 200f, 306f, 400f, paint)
        
        // Arms (rectangles)
        canvas.drawRect(156f, 220f, 206f, 350f, paint) // Left arm
        canvas.drawRect(306f, 220f, 356f, 350f, paint) // Right arm
        
        // Legs (rectangles)
        canvas.drawRect(216f, 400f, 256f, 500f, paint) // Left leg
        canvas.drawRect(256f, 400f, 296f, 500f, paint) // Right leg
        
        return bitmap
    }
    
    /**
     * Saves a bitmap to internal storage and returns the URI
     * Useful for testing when camera is not available
     */
    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, filename: String = "test_person.jpg"): Uri? {
        return try {
            val file = File(context.filesDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()
            Uri.fromFile(file)
        } catch (e: IOException) {
            Log.e("EmulatorUtils", "Error saving bitmap: ${e.message}")
            null
        }
    }
    
    /**
     * Creates a test image and returns its URI for AI testing
     */
    fun createTestImageForAI(context: Context): Uri? {
        val bitmap = createSamplePersonBitmap()
        return saveBitmapToInternalStorage(context, bitmap, "ai_test_person.jpg")
    }
    
    /**
     * Checks if the app is running on an emulator
     */
    fun isEmulator(): Boolean {
        return (android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.FINGERPRINT.startsWith("unknown")
                || android.os.Build.MODEL.contains("google_sdk")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic"))
                || "google_sdk" == android.os.Build.PRODUCT)
    }
    
    /**
     * Logs system information useful for debugging emulator issues
     */
    fun logSystemInfo() {
        Log.d("EmulatorUtils", "=== System Information ===")
        Log.d("EmulatorUtils", "Device: ${android.os.Build.DEVICE}")
        Log.d("EmulatorUtils", "Model: ${android.os.Build.MODEL}")
        Log.d("EmulatorUtils", "Product: ${android.os.Build.PRODUCT}")
        Log.d("EmulatorUtils", "Manufacturer: ${android.os.Build.MANUFACTURER}")
        Log.d("EmulatorUtils", "Brand: ${android.os.Build.BRAND}")
        Log.d("EmulatorUtils", "Fingerprint: ${android.os.Build.FINGERPRINT}")
        Log.d("EmulatorUtils", "Is Emulator: ${isEmulator()}")
        Log.d("EmulatorUtils", "==========================")
    }
}
