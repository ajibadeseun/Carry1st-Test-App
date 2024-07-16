package com.carry1st_shop.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carry1st_shop.R

typealias  NullableFunction = () -> Unit

@Composable
fun Carry1stTitleBar(
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color = Color.Blue,
    onBackNav: NullableFunction? = null,
    optionIcon: @Composable NullableFunction? = null,
    icon: @Composable NullableFunction? = null,
    optionClick: NullableFunction? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth().background(color = backgroundColor)
            .height(96.dp)

    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {

            IconButton(onClick = {
                onBackNav?.let {
                    it()
                }
            }, modifier = Modifier
                .padding(start = 16.dp)
                .testTag("back-btn")
                .semantics { contentDescription = "back button" }
            ) {
                if (icon == null) {
                    onBackNav?.let {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back_btn),
                            contentDescription = "back Btn",
                        )
                    }
                } else {
                    icon()
                }
            }
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp,
                    lineHeight = 21.78.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .weight(1f)
                    .offset(x = (-16).dp)
                    .testTag("title-text")
                    .semantics { contentDescription = "Title" }
            )

            optionClick?.let {
                IconButton(onClick = {
                    it()
                }, modifier = Modifier.size(30.dp)) {
                    optionIcon?.let { icon ->
                        icon()
                    }
                }
            } ?: Spacer(modifier = Modifier.width(24.dp))
        }
    }

}