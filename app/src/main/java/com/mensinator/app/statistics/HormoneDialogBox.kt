package com.mensinator.app.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mensinator.app.R
import com.mensinator.app.ui.theme.appDRed

@Composable
fun ScrollableDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Define your text styles locally since they're not in MaterialTheme
    val textAppStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.textfont)),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    val subTextAppStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.secfont, FontWeight.SemiBold)),
        fontSize = 18.sp
    )

    val hormoneNameStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.textfont)),
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = appDRed
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Centered heading
                Text(
                    text = "HORMONE CYCLE INFORMATION",
                    style = textAppStyle,
                    color = appDRed,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Hormone information sections
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Estrogen
                    Text(
                        text = "Estrogen",
                        style = hormoneNameStyle.copy(color = Color(0xFF7F1F0E)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Estrogen rises in the first half of the cycle and helps the lining of the uterus grow. It makes you feel more energetic, improves mood, and gets the body ready to release an egg during ovulation. It also helps the skin and hair look healthier. Estrogen peaks just before ovulation.",
                        style = subTextAppStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Progesterone
                    Text(
                        text = "Progesterone",
                        style = hormoneNameStyle.copy(color = Color(0xFFDAC181)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Progesterone rises after ovulation in the second half of the cycle. It calms the body, helps with sleep, and keeps the uterus lining stable. If levels drop, it can cause mood swings, bloating, or tiredness. When there's no egg to support, progesterone falls and the period starts.",
                        style = subTextAppStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // LH
                    Text(
                        text = "LH",
                        style = hormoneNameStyle.copy(color = Color(0xFF3D0A05)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Luteinizing Hormone suddenly rises in the middle of the cycle and causes the egg to be released from the ovary. This is called ovulation and it usually happens around day 14. LH also helps form the corpus luteum, which makes progesterone. It works with FSH to keep the cycle in sync.",
                        style = subTextAppStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // FSH
                    Text(
                        text = "FSH",
                        style = hormoneNameStyle.copy(color = Color(0xFF003049)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Follicle-Stimulating Hormone rises in the beginning of the cycle and helps eggs grow inside the ovaries. It tells the follicles to mature, and one of them will become the egg that’s released. FSH also helps control how long the cycle is and when ovulation will happen.",
                        style = subTextAppStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Testosterone
                    Text(
                        text = "Testosterone",
                        style = hormoneNameStyle.copy(color = Color(0xFF669BBC)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Testosterone is a small but important hormone in women that rises around ovulation. It boosts sex drive, energy, and focus, and can help you feel more confident. It also supports the growth of the egg and keeps muscles and bones strong.",
                        style = subTextAppStyle,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}

// Extension function to display color hex codes
fun Color.toHexColor(): String {
    val argb = this.toArgb()
    return String.format("#%06X", 0xFFFFFF and argb)
}