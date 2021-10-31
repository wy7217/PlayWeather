package com.zj.weather.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.weather.MainViewModel
import com.zj.weather.ui.view.weather.*
import com.zj.weather.utils.IconUtils
import com.zj.weather.utils.ImageLoader
import com.zj.weather.utils.showToast

@Composable
fun WeatherPage(mainViewModel: MainViewModel, cityListClick: () -> Unit) {
    val context = LocalContext.current
    val weatherNow by mainViewModel.weatherNow.observeAsState()
    val airNowBean by mainViewModel.airNowBean.observeAsState(listOf())
    val hourlyBeanList by mainViewModel.hourlyBeanList.observeAsState(listOf())
    val dayBeanList by mainViewModel.dayBeanList.observeAsState(listOf())
    val scrollState = rememberScrollState()
    val fontSize = (100f / (scrollState.value / 2) * 70).coerceAtLeast(25f).coerceAtMost(45f).sp
    scrollState.interactionSource
    Box(modifier = Modifier.fillMaxSize()) {
        ImageLoader(
            modifier = Modifier.fillMaxSize(),
            data = IconUtils.getWeatherBack(context, weatherNow?.icon)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { translationY = (scrollState.value / 2).toFloat() },
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start), onClick = cityListClick
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add"
                    )
                }
            }

            Text(
                text = "北京市",
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 30.sp,
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "${weatherNow?.text}  ${weatherNow?.temp}℃",
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                fontSize = fontSize,
                color = MaterialTheme.colors.primary
            )

            // 天气动画
            WeatherAnimation(weatherNow?.icon)

            Spacer(modifier = Modifier.height(10.dp))

            // 当前空气质量
            AirQuality(airNowBean)

            Spacer(modifier = Modifier.height(10.dp))

            // 未来24小时天气预报
            HourWeather(hourlyBeanList)

            Spacer(modifier = Modifier.height(10.dp))

            // 未来7天天气预报
            DayWeather(dayBeanList)

            // 当天具体天气数值
            DayWeatherContent(weatherNow)
        }

    }
}