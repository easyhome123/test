<template>
  <div class="main">
    <el-row :gutter="20" style=" margin: 5px;">
      <el-col :span="8">
        <el-card style="height: 100px;text-align: center;">
          <p>当前日期</p>
          <div class="countdown-footer">{{ value }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card style="height: 100px;text-align: center;">
          <p>当前时间</p>
          <div id="time">{{ getDate() }}</div>
        </el-card>


      </el-col>
      <el-col :span="8">
        <el-card style="height: 100px;text-align: center;">
          <el-countdown format="DD [days] HH:mm:ss" :value="value2">
            <template #title>
              <div style="display: inline-flex; align-items: center">
                <el-icon style="margin-right: 4px" :size="12">
                  <Calendar />
                </el-icon>
                <p>距离下个月的时间</p>
              </div>
            </template>
          </el-countdown>

        </el-card>

      </el-col>

    </el-row>

    <el-row :gutter="20" style="margin: 5px;">
      <el-col :span="16">
        <el-card>
          <div style="width:100%;height: 400px;" id="main">
          </div>
        </el-card>

      </el-col>

      <el-col :span="8">
        <el-card>
          <div style="height: 400px;" class="text">
            <p>公告通知</p>
            <ul>
              <li>我们将监测酒店的统计数据，并将发送数据分析通知，以便酒店管理层制定正确的决策和战略。</li>
              <li>将监测客房的状态，如是否可用、是否已被预订等。并将发送通知给相关部门，以确保客人能够顺利入住，并及时为客人提供服务。</li>
              <li>将接收到客人预订的订单信息，包括客房类型、入住日期、离店日期、入住人数和价格等。并将发送订单确认信息，以确保客人收到订单并保留房间。</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>



    <el-row :gutter="20" style="margin:5px;" class="el-row">
      <el-col :span="4" class="el-col">
        <el-card style="background-color:#409EFF;height: 180px;">
          <div class="grid-content ep-bg-purple">
            酒店会员总数
            <p> {{ membertotal }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" class="el-col">
        <el-card style="background-color: #F56C6C;height: 180px;">
          <div class="grid-content ep-bg-purple">
            酒店房间总数
            <p>{{ roomTotal }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" class="el-col">
        <el-card style="background-color: #E6A23C; height: 180px;">
          <div class="grid-content ep-bg-purple">
            酒店预订总数
            <p>{{ reserveTotal }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4" class="el-col">
        <el-card style="background-color:#67C23A;height: 180px;">
          <div class="grid-content ep-bg-purple">
            酒店入住总数
            <p>{{ checkinTotal }}</p>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card style="height: 180px;">
          <img src="@/assets/cexia.png" style="width: 100%;height: 100%;">
        </el-card>
      </el-col>
    </el-row>



  </div>
</template>
<script setup lang="ts">
import { getCheckinListApi } from "@/api/hotel/checkin/checkin"
import { getRoomListApi } from "@/api/hotel/list/list"
import { getMemberListApi } from "@/api/hotel/member/member"
import { getReserveListApi } from "@/api/hotel/reserve/reserve"
import { onMounted, onUpdated, reactive, ref, watch } from "vue"
import dayjs from 'dayjs';
import echarts from "@/utils/echarts"
import { getRoomTypeListApi } from "@/api/hotel/roomtype/roomType"

let roomlist: any[] = []
let roomSumlist: any[] = []
const membertotal = ref(0)
const roomTotal = ref(0)
const reserveTotal = ref(0)
const checkinTotal = ref(0)
const params = {
  'pageIndex': 1,
  'pageSize': 10,
  'searchValue': ''
}


const loadData = async () => {
  const [memberData, roomData, reserveData, checkinData] = await Promise.all([
    getMemberListApi(params),
    getRoomListApi(params),
    getReserveListApi(params),
    getCheckinListApi(params)
  ]);
  membertotal.value = memberData.data.totalElements;
  roomTotal.value = roomData.data.result.totalElements;
  reserveTotal.value = reserveData.data.totalElements;
  checkinTotal.value = checkinData.data.totalElements;
};

const roomTypeEcharts = async () => {
  const { data } = await getRoomTypeListApi(params)
  for (const key in data.content) {
    roomlist[key] = data.content[key].typeName
  }
  for (const i in data.content) {
    roomSumlist[i] = data.content[i].sum
  }
  vchart()
}
//时间模块
const value1 = ref("")
const value = ref(dayjs().format('YYYY-MM-DD'))
const value2 = ref(dayjs().add(1, 'month').startOf('month'))
const getDate = () => {
  let hours = dayjs().hour() < 10 ? "0" + dayjs().hour() : dayjs().hour()
  let minutes = dayjs().minute() < 10 ? "0" + dayjs().minute() : dayjs().minute()
  let seconds = dayjs().second() < 10 ? "0" + dayjs().second() : dayjs().second()
  value1.value = hours + ":" + minutes + ":" + seconds
  return value1.value
}
setInterval(() => {
  getDate()
}, 1000)

onMounted(() => {
  loadData()
})
roomTypeEcharts()



//echarts 画图模块
const vchart = () => {
  let chartDom = document.getElementById('main');
  let myChart = echarts.init(chartDom);
  let option = {
    title: {
      text: '各类客房预订情况'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      data: roomlist
    },
    yAxis: {

    },
    series: [{
      name: '数量',
      type: 'bar',
      data: roomSumlist
    }]

  }
  myChart.setOption(option, true)
}
</script>

<style scoped>
.main {
  width: 100%;
  background-image: url("@/assets/login/login_bg.svg");
  background-size: cover;
}

.text {
  background-image: linear-gradient(#ffffff, rgb(220, 220, 196), rgb(206, 219, 224));
}

.text p {
  display: block;
  text-align: center;
  color: #333;
  font-size: 25px;
}

.text li {
  display: block;
  text-align: center;
  font-size: larger;
  margin: 20px;
  color: #706e6e;
}


.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
  width: 100%;
  height: 100%;
  text-align: center;
  font-size: 20px;
  color: black
}

.grid-content p {
  margin: 20px;
  display: block;
  color: whitesmoke;
  ;
  font-size: 40px;
}


.countdown-footer {
  margin-top: 8px;
  color: aqua;
  font-size: 25px;
}

#time {
  font-size: 25px;
  color: greenyellow;
  margin-top: 8px;
}</style>
