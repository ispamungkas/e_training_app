package com.maspam.etrain.training.core.presentation.utils

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.AArrowDown
import com.composables.icons.lucide.AArrowUp
import com.composables.icons.lucide.AlignHorizontalDistributeCenter
import com.composables.icons.lucide.AlignHorizontalDistributeEnd
import com.composables.icons.lucide.AlignHorizontalDistributeStart
import com.composables.icons.lucide.Bold
import com.composables.icons.lucide.Codepen
import com.composables.icons.lucide.Italic
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Save
import com.composables.icons.lucide.Underline
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.maspam.etrain.training.domain.model.UserModel
import com.mohamedrejeb.richeditor.model.RichTextState
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateTimeFormatter(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("d - M - yyyy", Locale.getDefault())
    return format.format(date)
}

fun Long.toDateTimeVersion2Formatter(): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat("dd - MM - yyyy", Locale.getDefault())
    return format.format(date)
}

fun generateUserTrainingReport(users: List<UserModel>, outputStream: OutputStream, tahun: Int): Boolean {
    return try {
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        document.add(
            Paragraph("Laporan Pelatihan Guru")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(24f)
                .setBold()
        )
        document.add(
            Paragraph("Tahun $tahun")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setBold()
        )
        document.add(Paragraph("\n"))

        users.forEach { user ->
            document.add(Paragraph("Detail Guru:").setBold().setFontSize(16f))
            document.add(Paragraph("Nama: ${user.name}"))
            if (user.nip != null && user.nip != "0000" && user.nip != "superuser") {
                document.add(Paragraph("NIP: ${user.nip}"))
            }
            document.add(Paragraph("\n"))

            if (!user.enrolls.isNullOrEmpty()) {
                document.add(Paragraph("Pelatihan:").setBold().setFontSize(14f))

                user.enrolls.forEach { enrollment ->
                    val trainingDetail = enrollment.trainingDetail
                    if (trainingDetail != null) {
                        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f, 3f)))
                            .useAllAvailableWidth()

                        table.addHeaderCell(Paragraph("Detail").setBold())
                        table.addHeaderCell(Paragraph("Keterangan").setBold())

                        table.addCell("Nama Pelatihan:")
                        table.addCell(trainingDetail.name)

                        table.addCell("Status:")
                        table.addCell(enrollment.status?.replaceFirstChar { it.uppercase() } ?: "-")

                        table.addCell("Kehadiran:")
                        table.addCell(if (enrollment.attandance == true) "Hadir" else "Tidak Hadir")

                        table.addCell("Total JP (Pelatihan):")
                        table.addCell(enrollment.totalJp.toString())

                        document.add(table)
                        document.add(Paragraph("\n"))
                    }
                }
            } else {
                document.add(Paragraph("Tidak ada pelatihan yang diikuti.\n"))
            }

            document.add(Paragraph("---").setTextAlignment(TextAlignment.CENTER))
            document.add(Paragraph("\n"))
        }

        document.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorControls(
    modifier: Modifier = Modifier,
    state: RichTextState,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderlineClick: () -> Unit,
    onTitleClick: () -> Unit,
    onSubtitleClick: () -> Unit,
    onTextColorClick: () -> Unit,
    onStartAlignClick: () -> Unit,
    onEndAlignClick: () -> Unit,
    onCenterAlignClick: () -> Unit,
    onExportClick: () -> Unit,
    valueSave: (String) -> Unit
) {
    var boldSelected by rememberSaveable { mutableStateOf(false) }
    var italicSelected by rememberSaveable { mutableStateOf(false) }
    var underlineSelected by rememberSaveable { mutableStateOf(false) }
    var titleSelected by rememberSaveable { mutableStateOf(false) }
    var subtitleSelected by rememberSaveable { mutableStateOf(false) }
    var textColorSelected by rememberSaveable { mutableStateOf(false) }
    var alignmentSelected by rememberSaveable { mutableStateOf(0) }

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ControlWrapper(
            selected = boldSelected,
            onChangeClick = { boldSelected = it },
            onClick = onBoldClick
        ) {
            Icon(
                imageVector = Lucide.Bold,
                contentDescription = "Bold Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = italicSelected,
            onChangeClick = { italicSelected = it },
            onClick = onItalicClick
        ) {
            Icon(
                imageVector = Lucide.Italic,
                contentDescription = "Italic Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = underlineSelected,
            onChangeClick = { underlineSelected = it },
            onClick = onUnderlineClick
        ) {
            Icon(
                imageVector = Lucide.Underline,
                contentDescription = "Underline Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = titleSelected,
            onChangeClick = { titleSelected = it },
            onClick = onTitleClick
        ) {
            Icon(
                imageVector = Lucide.AArrowUp,
                contentDescription = "Title Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = subtitleSelected,
            onChangeClick = { subtitleSelected = it },
            onClick = onSubtitleClick
        ) {
            Icon(
                imageVector = Lucide.AArrowDown,
                contentDescription = "Subtitle Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = textColorSelected,
            onChangeClick = { textColorSelected = it },
            onClick = onTextColorClick
        ) {
            Icon(
                imageVector = Lucide.Codepen,
                contentDescription = "Text Color Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = alignmentSelected == 0,
            onChangeClick = { alignmentSelected = 0 },
            onClick = onStartAlignClick
        ) {
            Icon(
                imageVector = Lucide.AlignHorizontalDistributeStart,
                contentDescription = "Start Align Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = alignmentSelected == 1,
            onChangeClick = { alignmentSelected = 1 },
            onClick = onCenterAlignClick
        ) {
            Icon(
                imageVector = Lucide.AlignHorizontalDistributeCenter,
                contentDescription = "Center Align Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = alignmentSelected == 2,
            onChangeClick = { alignmentSelected = 2 },
            onClick = onEndAlignClick
        ) {
            Icon(
                imageVector = Lucide.AlignHorizontalDistributeEnd,
                contentDescription = "End Align Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        ControlWrapper(
            selected = true,
            selectedColor = MaterialTheme.colorScheme.tertiary,
            onChangeClick = {
                valueSave(state.toHtml())
            },
            onClick = onExportClick
        ) {
            Icon(
                imageVector = Lucide.Save,
                contentDescription = "Export Control",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ControlWrapper(
    selected: Boolean,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.inversePrimary,
    onChangeClick: (Boolean) -> Unit,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 6.dp))
            .clickable {
                onClick()
                onChangeClick(!selected)
            }
            .background(
                if (selected) selectedColor
                else unselectedColor
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .padding(all = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = android.icu.text.SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun getFileSizeInMB(context: Context, uri: Uri): Double {
    return try {
        val sizeInBytes = context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
            pfd.statSize
        } ?: 0L

        sizeInBytes.toDouble() / (1024 * 1024) // konversi ke MB
    } catch (e: Exception) {
        e.printStackTrace()
        0.0
    }
}