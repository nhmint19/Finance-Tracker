package com.example.financetracker.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.financetracker.data.model.Transaction
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat

fun createExcel(context: Context, fileName:String, sheetName: String, dataList: List<Transaction>) {
    // Get App Directory
    val appDirectory = context.getExternalFilesDir(null)

    // Check App Directory whether it exists or not, create if not.
    appDirectory?.mkdirs()

    // Create excel file with extension .xlsx
    val excelFile = File(appDirectory,fileName)

    // Write workbook to file using FileOutputStream
    val workbook = createWorkbook(sheetName, dataList)
    try {
        val fileOutputStream = FileOutputStream(excelFile)
        workbook.write(fileOutputStream)
        fileOutputStream.close()
        Toast.makeText(context,"Export successfully!", Toast.LENGTH_LONG).show()
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun createWorkbook(sheetName: String, dataList: List<Transaction>): Workbook {
    // Creating excel workbook
    val workbook = HSSFWorkbook()

    // Creating transaction sheet inside workbook
    val sheet: Sheet = workbook.createSheet(sheetName)

    // Creating sheet header row
    createSheetHeaderRow(sheet)

    // Insert data to the sheet
    addData(dataList, sheet)

    return workbook
}

private fun addData(dataList : List<Transaction>, sheet: Sheet) {
    for (i in dataList.indices) {
        addRowData(i + 1, sheet, dataList[i])
    }
}

private fun addRowData(rowIndex: Int, sheet: Sheet, transaction: Transaction) {

    // Create row based on row index
    val row = sheet.createRow(rowIndex)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    // Add data to cells
    createCell(row, 0, transaction.name) // Name column
    createCell(row, 1, dateFormat.format(transaction.date)) // Date column
    createCell(row, 2, transaction.amount.toString()) // Amount column
    createCell(row, 3, transaction.category) // Category column
}

private fun createSheetHeaderRow(sheet: Sheet) {
    // Create sheet first row
    val row = sheet.createRow(0)

    // Header list
    val headerList = listOf("name", "date", "amount", "category")

    for ((index, value) in headerList.withIndex()) {
        // Draw column
        val columnWidth = (15 * 500)

        // Index represents the column number
        sheet.setColumnWidth(index, columnWidth)

        // Create cell
        val cell = row.createCell(index)
        cell?.setCellValue(value)
    }
}

private fun createCell(row: Row, columnIndex: Int, value: String?) {
    val cell = row.createCell(columnIndex)
    cell?.setCellValue(value)
}

fun getFileUri(context: Context, fileName: String?): Uri? {
    val file = File(context.getExternalFilesDir(null), fileName!!)
    if (file.exists()) {
        return FileProvider.getUriForFile(
            context, "com.example.financetracker.provider", file
        )
    }
    return null
}
