<template>
<!--  顶部-->
  <TopHeader/>

<!--  主体-->
<div class="hotel-room-nav hotel-index">
  <Search/>

<!--  幻灯片-->
  <div class="hotel-banner">
    <div class="hotel-container">
      <div class="product-list">
        <dl>
          <dt><router-link to="/hotel/list">房间分类</router-link></dt>
          <dd v-for="item in indexRoomTypeList">
            <router-link :to="{path:'/hotel/list',query:{id:item.id}}">
              {{item.typeName}}
            </router-link>
          </dd>
        </dl>
      </div>
    </div>
      <el-carousel height="490px">
        <el-carousel-item>
          <img src="@/assets/hotel/images/banner2.jpg" alt="酒店管理">
        </el-carousel-item>
        <el-carousel-item>
          <img src="@/assets/hotel/images/banner3.jpg" alt="酒店管理">
        </el-carousel-item>
        <el-carousel-item>
          <img src="@/assets/hotel/images/banner4.jpg" alt="酒店管理">
        </el-carousel-item>
        <el-carousel-item>
          <img src="@/assets/hotel/images/banner5.jpg" alt="酒店管理">
        </el-carousel-item>
      </el-carousel>
  </div>
</div>
  <div class="hotel-temp">
    <template v-for="(item,index) in hotelFloorList">
      <div v-if="index%2==0" class="temp-hot">
        <div class="hotel-container">
          <p class="temp-title-cn">
            <span></span>
            <router-link :to="{path:'/hotel/list',query:{floorId:item.id}}">
              {{item.floorName}}
            </router-link>
            <span></span>
          </p>
          <el-row :gutter="20">
            <template v-for="room in item.roomList">
              <el-col :span="8">
                <router-link :to="'/hotel/details/'+room.id" target="_blank" class="template store-list-box">
                  <img :src="url+'uploadFile/'+room.coverImg" class="store-list-cover"/>
                  <h2>{{room.roomName}}</h2>
                  <p class="price">
                    <span title="会员价">{{room.memberPrice}}$</span>
                    <span title="房间号"
                          style="color: white;background:#0e88cc;
                          padding: 3px;text-align: center;
                          border: 1px solid skyblue;
                          font-size: 13px">
                      N0.{{room.roomNumber}}</span>
                  </p>
                </router-link>
              </el-col>
            </template>
          </el-row>
        </div>
      </div>

      <div v-else class="temp-normal">
        <div class="hotel-container">
          <p class="temp-title-cn">
            <span></span>
            <router-link :to="{path:'/hotel/list',query:{floorId:item.id}}">
              {{item.floorName}}
            </router-link>
            <span></span>
          </p>

          <el-row :gutter="20">
            <template v-for="room in item.roomList">
              <el-col :span="8">
                <router-link :to="'/hotel/details/'+room.id" target="_blank" class="template store-list-box">
                  <img :src="url+'uploadFile/'+room.coverImg" class="store-list-cover"/>
                  <h2>{{room.roomName}}</h2>
                  <div>
                    <label class="store-list-pay">${{room.memberPrice}}</label>
                    <div class="store-list-colorbar" title="">
                      <span class="store-color-bar"
                            style="color: white;
                            background:#cc9812;
                            padding: 3px;text-align: center;">
                        NO.{{room.roomNumber}}
                      </span>
                    </div>
                  </div>
                </router-link>
              </el-col>
            </template>
          </el-row>
        </div>
      </div>
    </template>
  </div>
<!--  底部-->
  <Footer/>
</template>
<script setup lang="ts">
import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import {onMounted, ref} from "vue";
import {getIndexApi} from "@/api/hotel/index";

//服务器路径
const url = import.meta.env.VITE_APP_BASE_API

const indexRoomTypeList = ref<object[]>([])
const hotelFloorList = ref<object[]>([])
const getIndex =async ()=>{
  const { data } = await  getIndexApi()
  indexRoomTypeList.value = data.result.indexRoomTypeList
  hotelFloorList.value = data.result.hotelFloorList
}
onMounted(()=>{
  getIndex()
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
.hotel-room-nav .hotel-index{
  padding-bottom: 0;
}

.hotel-room-nav .hotel-banner{
  margin-top: 70px;
  background:skyblue;
  text-align: center;
}
.hotel-room-nav .product-list{
  position: absolute;
  left: 0;
  width: 220px;
  z-index: 100;
  top: -60px;
  background: white;
  box-shadow: 0 1px 5px rgb(0 0 0/15%);
}

.hotel-room-nav .product-list dl dt{
  font-size: 16px;
  background: #2fa7b9;
  line-height: 60px;
}

.hotel-room-nav .product-list dl dt a{
  color: white;
}
.hotel-room-nav .product-list dl dd{
  display: block;
  width: 100%;
  margin: 0;
  line-height: 70px;
  border: none;
  border-bottom: 1px solid #f0f0f0;
}
.hotel-temp .temp-hot{
  padding-top: 80px;
  padding-bottom: 80px;
  background: white;
  text-align: center;
}

.hotel-temp .temp-hot p{
  padding-bottom: 10px;
  text-align: center;
  font-size: 16px;
}
.temp-title-cn{
  margin-bottom: 50px;
  color: #333;
  font-size: 25px!important;
  line-height: 40px;
  position: relative;
  text-align: center;
  font-weight: 300;
}

.temp-title-cn span{
  width: 35px;
  height: 2px;
  background: #333;
  display: inline-block;
  position: absolute;
  top: 15px;
  left: 50%;
  margin-left: -110px;
}

.temp-title-cn span:last-child{
  margin-left: 70px;
}
.store-list-box{
  display: block;
  background-color: #FFF;
  transition: all .5s;
  -webkit-transition: all .5s;
  padding-bottom: 6px;
}

.store-list-box .store-list-cover{
  height: 200px;
  width: 100%;
}

.store-list-box h2{
  font-size: 18px;
  padding: 20px;
  line-height: 25px;
  text-align: center;
}

.hotel-temp .temp-hot p.price span{
  line-height: 15px;
  color: #f44e2d;
  margin-left: 15px;
}

.hotel-temp .temp-normal{
  padding-top: 60px;
  padding-bottom: 60px;
}

.store-list-box>div{
  position: relative;
  padding: 0 15px 15px;
}

.store-list-pay{
  border-color: #FF5722;
  color: #FF5722;
}

.store-list-colorbar{
  position: absolute;
  right: 15px;
  top: -1px;
}
.store-color-bar{
  display: inline-block;
  width: 70px;
  height: 20px;
}
</style>