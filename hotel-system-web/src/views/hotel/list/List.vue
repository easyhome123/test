<template>
<!--  顶部区域-->
  <TopHeader/>


<!--搜素框-->
  <Search/>

<!--  中间内容，列表类型-->
  <div class="hotel-list-filter">
    <div class="hotel-container">
      <el-card>
        <template #header>
          <div class="card-header">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{path:'/'}">酒店首页</el-breadcrumb-item>
              <el-breadcrumb-item :to="{path:'/hotel/list'}">类别</el-breadcrumb-item>
              <el-breadcrumb-item><cite>全部</cite></el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </template>

        <div class="hotel-card-body">
<!--          房间类型-->
          <div class="room-cat-item">
            <span><el-icon style="margin-right: 12px;"><ArrowRight/></el-icon>类别:</span>
            <ul>

              <li :class="{'active':activeStatus==0}">
                <a href="javascript:void(0)" @click="searchRoomType(null)">全部</a>
<!--                <router-link to=""/>-->
              </li>

              <li :class="{'active':activeStatus==item.id}" v-for="item in roomTypeList">
                <a href="javascript:void(0)" @click="searchRoomType(item.id)">{{item.typeName}}</a>
              </li>
            </ul>
          </div>
<!--          楼层-->
          <div class="room-cat-item color-list">
            <span><el-icon style="margin-right: 12px;"><Memo/></el-icon>楼层:</span>
            <ul>
              <li style="background:#f2f2f2;width: 20px;" title="全部">
                <a href="javascript:void(0)" @click="searchFloor(null)">
                  <img src="@/assets/hotel/images/all_bg.jpg" alt="">
                  <el-icon v-show="floorStatus==0"><Select/></el-icon>
                </a>
              </li>

              <li :title="item.floorName" v-for="(item,index) in floorList"  @click="searchFloor(item.id)">
                <a href="javascript:void(0);">
                   {{item.floorName}}
                </a>
              </li>
            </ul>
          </div>
          <p class="filter-end">筛选出<span>{{total}}</span>个</p>
        </div>
      </el-card>
    </div>

  </div>

<!--  房间列表内容-->
  <div class="hotel-room-list">
    <div class="hotel-room-normal">
      <div class="hotel-container">
        <el-row :gutter="20">
          <template v-for="item in roomList">
            <el-col :span="8" style="padding: 10px;">
              <router-link target="_blank" class="room-list-box" :to="'/hotel/details/'+item.id">
                <img :src="url+'uploadFile/'+item.coverImg" class="room-list-cover">
                <h2>{{item.roomName}}</h2>
                <div><label class="room-list-pay">${{item.memberPrice}}</label>
                <div class="room-list-colorbar">
                  <span class="room-color-bar"
                        style="border-color: #009688;border-width: 1px;border-style: solid;background-color:white;text-align: center;">
                    NO.{{item.roomNumber}}</span>
                  <span class="room-color-bar"
                        style="border-color: skyblue;border-width: 1px;border-style: solid;background-color:white;text-align: center">
                    {{item.hotelRoomType.typeName}}</span>
                  <span class="room-color-bar"
                        style="border-color: orange;border-width: 1px;border-style: solid;background-color:white;text-align: center">
                    {{item.hotelFloor.floorNo}}</span>
                </div>
                </div>
              </router-link>
            </el-col>
          </template>
        </el-row>
      </div>
    </div>

<!--    分页-->
    <el-pagination background layout="total,prev,pager,next,jumper" :total="total"
                   v-model:page-size="pageSize" @current-change="changePage"/>
  </div>
  <!--  底部区域-->
  <Footer/>

</template>
<script setup lang="ts">

import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import {ArrowRight, Memo, Select} from "@element-plus/icons-vue";
import {onMounted, ref, watch} from "vue";
import {getAllRoomTypeAndFloorApi, getRoomListApi} from "@/api/hotel/list/list";
import {useRoute} from "vue-router";

//房间类型列表
const roomTypeList = ref<Object[]>([])
//楼层信息列表
const floorList =  ref<Object[]>([])
//服务器路径
const url = import.meta.env.VITE_APP_BASE_API

//获取所有楼层信息和房间类型
const getAllRoomTypeAndFloor = async  ()=>{
  const  { data } = await getAllRoomTypeAndFloorApi()
  roomTypeList.value = data.result.roomTypeList
  floorList.value = data.result.floorList
}

const total = ref(0)
const roomList = ref<Object[]>([])

const getRoomList = async ()=>{
  const params = {
    'roomTypeId':roomTypeId.value,
    'floorId':floorId.value,
    'searchValue':searchValue.value,
    'pageIndex':pageIndex.value,
    'pageSize':pageSize.value,
  }
  const { data } = await getRoomListApi(params)
  roomList.value = data.result.content
  total.value = data.result.totalElements
}
//分页
const pageSize = ref(12)
const pageIndex = ref(1)

const changePage = (val:number)=>{
  pageIndex.value = val
  getRoomList()
}
//房间类型状态
const activeStatus:any = ref(0)
const roomTypeId = ref()
const searchRoomType = (val:number) => {
  roomTypeId.value = val
  if(val == null ){
    activeStatus.value = 0
  }else {
    activeStatus.value = val
  }
  getRoomList()
}
//楼层状态
const floorStatus:any = ref(0)
const floorId = ref()
const searchFloor = (val:number) => {
  floorId.value = val
  if(val == null){
    floorStatus.value = 0
  }else {
    floorStatus.value = val
  }
  getRoomList()
}
//路由参数对象
const  route = useRoute()
//监听搜素关键字
const searchValue = ref()
watch(()=>route.query.keywords,(newSearchValue)=>{
  searchValue.value = newSearchValue
  getRoomList()
})

onMounted(()=>{
  if(route.query.id){
    roomTypeId.value = route.query.id
    activeStatus.value = route.query.id
  }

  //楼层id参数
  if(route.query.floorId){
    floorId.value = route.query.floorId
    floorStatus.value = route.query.floorId
  }
  //
  searchValue.value = route.query.keywords
  getAllRoomTypeAndFloor()
  getRoomList()
})
</script>
<style scoped>
.hotel-list-filter{
  margin-top: 30px;
}

.hotel-container{
  width: 1300px;
  position: relative;
  padding: 15px;
  margin: 0 auto;
  box-sizing: border-box;
}
.hotel-list-filter .hotel-card-body{
  padding-left: 25px;
  padding-top: 15px;
}
.room-cat-item{
  display: flex;
  padding-bottom: 30px;
  font-size: 16px;
  line-height: 26px;
}
.hotel-list-filter .room-cat-item span{
  width: 100px;
  color: #555;
  margin-top: 1px;
}
.room-cat-item ul{
  width: 80%;
  vertical-align: top;
}
.room-cat-item ul li{
  margin-right: 25px;
  cursor: pointer;
  display: inline-block;
  vertical-align: middle;
}
.room-cat-item.color-list li{
  width: 100px;
  height: 12px;
  line-height: 12px;
  margin-right: 14px;
  position: relative;
}
.room-cat-item.color-list li i{
  position: absolute;
  top: 1px;
  left: 4px;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

/*房间列表*/
.hotel-room-list .hotel-room-normal{
  padding-top: 45px;
  padding-bottom: 60px;
}
.room-list-box{
  display: block;
  background-color: white;
  transition: all .5s;
  -webkit-transition: all .5s;
  padding-bottom: 6px;
  cursor: pointer;
}
.room-list-box .room-list-cover{
  height: 200px;
  width: 100%;
}
.room-list-box h2{
  font-size: 18px;
  padding: 20px 15px;
  line-height: 26px;
  text-align: center;
}
.room-list-box>div{
  position: relative;
  padding: 0 15px 15px;
}
.room-list-pay{
  border-color: #FF5722;
  color: #FF5722;
  height: 18px;
  line-height: 18px;
  border-width: 1px;
  border-style: solid;
  padding: 3px 6px;
  font-size: 12px;
}
.room-list-colorbar{
  position: absolute;
  right: 15px;
  top: -1px;
}
.room-color-bar{
  display: inline-block;
  width: 65px;
  height: 20px;
}
a {
  color: #666;
  text-decoration: none;
}

a:hover {
  color: #2fa7b9;
}
</style>