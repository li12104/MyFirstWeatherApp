package com.example.myfirstweatherapp.logic.model

data class WeatherResponse(
    val cityid: String,
    val date: String,
    val week: String,
    val update_time: String,
    val city: String,
    val cityEn: String,
    val country: String,
    val countryEn: String,
    val wea: String,
    val wea_img: String,
    val tem: String, // 温度可能是字符串形式，如果需要计算或比较，可以转换为数字类型
    val tem1: String, // 白天最高温度
    val tem2: String, // 夜间最低温度
    val win: String,
    val win_speed: String,
    val win_meter: String,
    val humidity: String,
    val visibility: String,
    val pressure: String,
    val air: String,
    val air_pm25: String,
    val air_level: String,
    val air_tips: String,
    val alarm: List<Any>,
    val rain_pcpn: String,
    val uvIndex: String,
    val uvDescription: String,
    val wea_day: String,
    val wea_day_img: String,
    val wea_night: String,
    val wea_night_img: String,
    val sunrise: String,
    val sunset: String,
    val hours: List<HourlyWeather>,
    val aqi :Aqi
) {
}

data class HourlyWeather(
    val hours: String,
    val wea: String,
    val wea_img: String,
    val tem: String,
    val win: String,
    val win_speed: String,
    val vis: String, // 能见度
    val aqinum: String, // 空气质量指数
    val aqi: String // 空气质量级别
)

data class Aqi(
    val update_time: String,
    val air: String, // 空气质量指数
    val air_level: String, // 空气质量级别
    val air_tips: String, // 空气质量提示
    val pm25: String, // PM2.5 浓度
    val pm25_desc: String, // PM2.5 描述
    val pm10: String, // PM10 浓度
    val pm10_desc: String, // PM10 描述
    val o3: String, // 臭氧浓度
    val o3_desc: String, // 臭氧描述
    val no2: String, // 二氧化氮浓度
    val no2_desc: String, // 二氧化氮描述
    val so2: String, // 二氧化硫浓度
    val so2_desc: String, // 二氧化硫描述
    val co: String, // 一氧化碳浓度
    val co_desc: String, // 一氧化碳描述
    val kouzhao: String, // 是否需要佩戴口罩的建议
    val yundong: String, // 是否适宜运动的建议
    val waichu: String, // 是否适宜外出的建议
    val kaichuang: String, // 是否适宜开窗的建议
    val jinghuaqi: String // 空气净化器是否需要打开的建议
)
