package icesi.edu.co.fitscan.features.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.math.min

object ImageUtils {
      fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            android.util.Log.e("ImageUtils", "Error converting URI to bitmap: ${e.message}")
            null
        }
    }
    
    fun cropAndResizeTo512x512(bitmap: Bitmap): Bitmap {
        // Calculate the center crop dimensions
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val size = min(originalWidth, originalHeight)
        
        // Calculate starting positions for center crop
        val startX = (originalWidth - size) / 2
        val startY = (originalHeight - size) / 2
        
        // Create center-cropped square bitmap
        val croppedBitmap = Bitmap.createBitmap(bitmap, startX, startY, size, size)
        
        // Resize to 512x512
        return Bitmap.createScaledBitmap(croppedBitmap, 512, 512, true)
    }
    
    fun bitmapToBase64(bitmap: Bitmap, quality: Int = 85): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    fun processImageForAI(context: Context, uri: Uri): String? {
        val bitmap = uriToBitmap(context, uri) ?: return null
        val processedBitmap = cropAndResizeTo512x512(bitmap)
        return bitmapToBase64(processedBitmap)
    }
}
