<template>
  <TopHeader/>



  <div class="hotel-room-nav">
    <Search/>
  </div>

  <div class="hotel-container hotel-data">
    <el-card class="hotel-data-intro">
      <template #header>
        <div class="hotel-card-header">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path :'/index'}">酒店首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="{ path:'/hotel/list',query:{floorId:roomInfo.floorId}}">
              {{roomInfo.hotelFloor!=null ? roomInfo.hotelFloor.floorName : '酒店一楼'}}
            </el-breadcrumb-item>
            <el-breadcrumb-item>房间详情</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="12">
          <div class="intro-img photos">
            <img :src="url+'uploadFile/'+roomInfo.coverImg" id="coverImg" alt="roomInfo.roomName">
          </div>
        </el-col>

        <el-col :span="12">
          <div class="intro-txt">
            <h1 class="title">{{roomInfo.roomName}}</h1>
            <div class="store-attrs">
              <div class="summary">
                <p class="reference"><span>房间号</span><span>{{roomInfo.roomNumber}}</span></p>
                <p class="reference"><span>床型</span><span>{{roomInfo.bedType}}</span></p>
                <p class="reference"><span>宽带</span><span>{{roomInfo.broadband}}</span></p>
                <p class="reference"><span>标准价</span>$<span>{{roomInfo.standardPrice}}</span></p>
                <p class="reference"><span>会员价</span>
                  <strong class="price">
                    <i>$</i>
                    <span>{{roomInfo.memberPrice}}</span>
                  </strong>
                </p>
                <p class="reference" v-if="roomInfo.roomStatus == 0"><span>房间状态</span>空闲</p>
                <p class="reference" v-else-if="roomInfo.roomStatus == 1"><span>房间状态</span>已被预订</p>
                <p class="reference" v-else-if="roomInfo.roomStatus == 2"><span>房间状态</span>已有人入住</p>
                <p class="reference" v-else><span>房间状态</span>退房但未打扫</p>
              </div>
            </div>

            <p class="store-detail-active" v-if="roomInfo.roomStatus == 0">
              <el-button @click="reserveRoom(roomInfo.id,roomInfo.roomNumber)" color="skyblue"
                         style="width: 200px; line-height: 50px;font-size: 20px;height: 50px;color:white;">
                <el-icon size="30" class="hotel-icon"><Stopwatch/></el-icon>
                预订
              </el-button>
            </p>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card>
      <div class="detail-body">
        <div class="hotel-elem-quote">每天14：00入住，次日14：00之前退房</div>
        <div v-html="roomInfo.remarks"></div>
      </div>
    </el-card>
  </div>

  <Footer/>

<!--  预定房间信息弹出框-->
  <el-dialog align-center v-model="reserveRoomDialogFormVisible" width="50%" destroy-on-close>
    <template #header>
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h2>{{title}}</h2>
      </div>
    </template>
    <ReserveRoom :roomInfo="roomInfo" @closeAddReserveRoomForm="closeAddReserveRoomForm" @success="success"/>
  </el-dialog>
</template>
<script setup lang="ts">

import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import {EditPen, Stopwatch} from "@element-plus/icons-vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {getRoomDetailApi} from "@/api/hotel/detail/detail";
import ReserveRoom from "@/views/hotel/details/components/ReserveRoom.vue";

//服务器路径
const url = import.meta.env.VITE_APP_BASE_API
const router = useRouter()
const route = useRoute()

//房间信息
const roomId:any = route.params.id
const roomInfo = ref('')

const getRoomDetail = async ()=>{
  const { data } = await getRoomDetailApi(roomId)
  roomInfo.value = data.result
}

const title = ref('预定房间')
const reserveRoomDialogFormVisible = ref(false)
const reserveRoom = (roomId:number,roomNo:number)=>{
  title.value = `你正在预定房间号为${roomNo}的房间`
  reserveRoomDialogFormVisible.value = true
}
//关闭表单
const closeAddReserveRoomForm = ()=>{
  reserveRoomDialogFormVisible.value = false
}

const success = ()=>{
  reserveRoomDialogFormVisible.value = false
}

onMounted(()=>{
  getRoomDetail()
})

</script>
<style scoped>
.hotel-container{
  width: 1300px;
  position: relative;
  margin: 0 auto;
  box-sizing: border-box;
}
.hotel-room-nav{
  background-color: white;
}
.hotel-data-intro{
  margin: 30px 0;
  background-color: #fff;
}
.hotel-data-intro .intro-img{
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 270px;
  text-align: center;
  background-color: #fbfbfb;
  margin: 15px;
}
.hotel-data-intro .intro-img img{
  max-width: 100%;
}
.hotel-data-intro .intro-txt{
  padding: 10px;
}
.hotel-data-intro .intro-txt title{
  line-height: 35px;
  padding: 0 0 20px;
  font-size: 30px;
  color: #323232;
}
.store-attrs .summary{
  width: 100%;
  height: 250px;
  background: #f5f5f5;
  padding-top: 5px;
}
.store-attrs .summary p{
  line-height: 40px;
  padding-left: 35px;
  color: #888;
}
.store-attrs .summary p span{
  padding-right: 40px;
}
.store-detail-active{
  padding: 30px 0 30px;
}
.hotel-icon{
  margin-right: 10px;
}
.detail-body{
  margin: 20px 0 0;
  min-height: 310px;
  line-height: 25px;
  font-size: 15px;
  color: #333;
  word-wrap: break-word;
}

.hotel-elem-quote{
  margin-bottom: 10px;
  padding: 15px;
  line-height: 20px;
  border-left: 5px solid skyblue;
  border-radius: 0 2px 2px 0;
  background-color: #f2f2f2;
}
.my-header{
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
</style>