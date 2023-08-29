package com.target.supermarket.utilities

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.util.UUID

object InstallationUnique {
    private var sID:String? = null
    private const val INSTALLATION = "INSTALLATION"

    fun id(context: Context):String?{
        synchronized(this){
            if (sID == null){
                val installation = File(context.filesDir, INSTALLATION)
                try {

                    if (!installation.exists())
                        writeInstallationFile(installation)
                    sID = readInstallationFile(installation)

                }catch (e:Exception){
                    throw RuntimeException(e)
                }
            }
        }
        return sID
    }

    private fun readInstallationFile(installation: File): String {
        val file = RandomAccessFile(installation,"r")
        val bytes = ByteArray(file.length().toInt())
        file.readFully(bytes)
        file.close()
        return String(bytes)
    }

    private fun writeInstallationFile(installation: File) {
        val out = FileOutputStream(installation)
        val id = UUID.randomUUID().toString()
        out.write(id.toByteArray())
        out.close()
    }
}